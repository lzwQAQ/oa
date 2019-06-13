$(function () {


    var gridTable = $('#table').jqGrid({
        url: Util.getPath() + "/role/list",
        multiselect: true,
        colModel: [{
            label: '角色名称',
            name: 'name',
            formatter: function (value, options, row) {
                return editAble ? ('<a href="javascript:void(0)" class="edit-item" data-id="' + row.id + '">' + value + '</a>') : value;
            }
        }, {
            label: '角色代码',
            name: 'code'
        }, {
            label: '系统内置',
            name: 'isSys',
            width: 70,
            formatter: function (value, options, row) {
                return value === "1" ? "是" : "否";
            }
        }, {
            label: '备注',
            name: 'mark'
        }, {
            label: "操作",
            name: "id",
            width: 100,
            fixed: true,
            hidden: editAble ? false : true,
            formatter: function (value, options, row) {
                var actions = [];
                actions.push('<a href="javascript:void(0)" class="actions edit-item" data-id="' + row.id + '" title="编辑" ><i class="fa fa-pencil"></i></a>');
                actions.push('<a href="javascript:void(0)" class="actions delete-item" data-id="' + row.id + '" title="删除"><i class="fa fa-trash-o"></i></a>');
                actions.push('<a href="javascript:void(0)" class="actions permission-setting" data-id="' + row.id + '" title="权限设置"><i class="fa fa-expeditedssl"></i></a>');
                return actions.join("");
            }
        }]
    }).enableAutoHeight(".content", ".other-items");

    // 编辑
    $("#table").on("click", ".edit-item", function () {
        var id = $(this).attr("data-id");
        Util.innerIframe(Util.getPath() + "/role/form/" + id);
    });

    // 删除
    $("#table").on("click", ".delete-item", function () {
        deletes($(this).attr("data-id"));
    });

    //设置权限
    $("#table").on("click", ".permission-setting", function () {
        setPermissions($(this).attr("data-id"));
    });

    //查询
    $("#btn-search").click(function () {
        gridTable.setGridParam({
            postData: {
                name: $("#name").val(),
                code: $("#code").val()
            }
        }).trigger("reloadGrid", {
            page: 1
        });
    });

    //重置
    $("#btn-reset").click(function () {
        $("#search input").val("");
        $("#btn-search").click();
    });

    //新增
    $("#btn-add").click(function () {
        Util.innerIframe(Util.getPath() + "/role/form");
    });

    //批量删除
    $("#btn-delete").click(function () {
        var selectedRow = $("#table").jqGrid('getGridParam', 'selarrrow');
        if (selectedRow.length === 0) {
            Util.msgWarning("请选择需要删除的数据！");
            return;
        }
        deletes(selectedRow.join(","));
    });

    function deletes(ids) {
        Util.ask("确定删除？", function (index) {
            Util.closeLayer(index);
            Util.postLoading(Util.getPath() + "/role/deletes", {ids: ids}, function (res) {
                if (res.code === 200) {
                    Util.msgOk("删除成功！");
                    gridTable.trigger("reloadGrid");
                } else {
                    Util.msgError("删除失败！");
                }
            });
        });
    }

    var setting = {
        view: {
            showLine: false,
            showIcon: false
        },
        data: {
            simpleData: {
                enable: true
            }
        },
        check: {
            enable: true,
            chkboxType: {"Y": "ps", "N": "ps"}
        }
    };

    /**
     * 设置权限
     * @param id
     */
    function setPermissions(id) {
        var treeObj;
        top.layer.open({
            type: 1,
            content: $('.menus-div').html(),
            area: ['500px', '300px'],
            btn: ['确定', '取消'],
            success: function (layero, index) {
                var $menus = layero.find("#menus");
                $.post(Util.getPath() + "/role/allpermission/" + id, function (res) {
                    if (res.code === 200) {
                        //当父类菜单设置为隐藏的时候，其子类菜单不做显示
                        for (var i = 0; i < res.data.menus.length; i++) {
                            var remove = false;
                            var pId = res.data.menus[i].pId;
                            if (pId) {
                                remove = true;
                                $.each(res.data.menus, function (i, v) {
                                    if (pId === v.id) {
                                        remove = false;
                                        return false;
                                    }
                                });
                            }
                            if (remove) {
                                res.data.menus.splice(i, 1);
                                i--;
                            }
                        }
                        treeObj = $.fn.zTree.init($menus, setting, res.data.menus);
                        $.each(treeObj.transformToArray(treeObj.getNodes()), function (i, node) {
                            $.each(res.data.permissions, function (i, v) {
                                if (v.menuId === node.id) {
                                    treeObj.checkNode(node, true, false);
                                    return false;
                                }
                            });

                        });
                    }
                }, "JSON");
            },
            yes: function (index, layero) {
                if (treeObj) {
                    var nodes = treeObj.getCheckedNodes(true);
                    var menus = [];
                    $.each(nodes, function (i, v) {
                        menus.push(v.id);
                    });
                    $.post(Util.getPath() + "/role/permissions/save", {
                        roleId: id,
                        menus: menus.join(",")
                    }, function (res) {
                        if (res.code === 200) {
                            Util.closeLayer(index);
                            Util.msgOk("提交成功");
                        } else {
                            Util.msgError("提交失败");
                        }
                    }, "JSON");
                } else {
                    Util.closeLayer(index);
                }
            }
        });
    }


});

function reloadGrid() {
    $('#table').trigger("reloadGrid");
}