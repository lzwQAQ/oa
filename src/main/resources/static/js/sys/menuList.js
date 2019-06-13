$(function () {

    var gridTable = $('#table').jqGrid({
        url: Util.getPath() + "/menu/tree/list",
        treeGrid: true,
        colModel: [{
            label: '菜单名称',
            name: 'name',
            formatter: function (value, options, row) {
                var arry = [];
                arry.push('<i class="menu_icon ' + row.menuIcon + '" style="color: #86888a;margin-right: 3px"></i>&nbsp;');
                arry.push(editAble ? ('<a href="javascript:void(0)" class="edit-item" data-id="' + row.id + '">' + value + '</a>') : value);
                return arry.join("");
            }
        }, {
            label: '菜单链接',
            name: 'href'
        }, {
            label: '权限标识',
            name: 'permissions'
        }, {
            label: '是否显示',
            name: 'menuStatus',
            width: 75,
            fixed: true,
            formatter: function (value, options, row) {
                return "1" === value ? "显示" : "隐藏";
            }
        }, {
            label: '菜单类型',
            name: 'type',
            width: 75,
            fixed: true,
            formatter: function (value, options, row) {
                return "1" === value ? "权限" : "菜单";
            }
        }, {
            label: '更新时间',
            name: 'updateDate',
            width: 150,
            fixed: true
        }, {
            label: '序号',
            name: 'sort',
            width: 60,
            fixed: true
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
                actions.push('<a href="javascript:void(0)" class="actions add-child-item" data-id="' + row.id + '" title="新增下级菜单"><i class="fa fa-plus-square"></i></a>');
                return actions.join("");
            }
        }]
    }).enableAutoHeight(".content", ".other-items");

    // 编辑
    $("#table").on("click", ".edit-item", function () {
        var id = $(this).attr("data-id");
        Util.innerIframe(Util.getPath() + "/menu/form/" + id);
    });

    // 删除
    $("#table").on("click", ".delete-item", function () {
        deletes($(this).attr("data-id"));
    });

    // 新增下级菜单
    $("#table").on("click", ".add-child-item", function () {
        var id = $(this).attr("data-id");
        Util.innerIframe(Util.getPath() + "/menu/child/create/" + id);
    });

    //刷新
    $("#btn-refresh").click(function () {
        gridTable.trigger("reloadGrid");
    });

    //新增
    $("#btn-add").click(function () {
        Util.innerIframe(Util.getPath() + "/menu/form");
    });


    function deletes(ids) {
        Util.ask("确定删除？", function (index) {
            Util.closeLayer(index);
            Util.postLoading(Util.getPath() + "/menu/deletes", {ids: ids}, function (res) {
                if (res.code === 200) {
                    Util.msgOk("删除成功！");
                    gridTable.trigger("reloadGrid");
                } else {
                    Util.msgError("删除失败！");
                }
            });
        });
    }

});

function reloadGrid() {
    $('#table').trigger("reloadGrid");
}