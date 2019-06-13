$(function () {

    $("#search select").select2({
        width: "200px"
    });

    var gridTable = $('#table').jqGrid({
        url: Util.getPath() + "/user/list",
        multiselect: true,
        colModel: [{
            label: '用户名',
            name: 'username',
            width: 120,
            fixed: true,
            formatter: function (value, options, row) {
                return editAble ? ('<a href="javascript:void(0)" class="edit-item" data-id="' + row.id + '">' + value + '</a>') : value;
            }
        }, {
            label: '姓名',
            name: 'name',
            width: 140,
            fixed: true,
        }, {
            label: '性别',
            name: 'sex',
            width: 80,
            fixed: true,
            formatter: function (value, options, row) {
                return value === '1' ? "男" : "女";
            }
        }, {
            label: '手机号',
            name: 'phone'
        }, {
            label: '邮箱',
            name: 'email'
        }, {
            label: '账号状态',
            name: 'state',
            width: 100,
            fixed: true,
        }, {
            label: '所属部门',
            name: 'dept.name'
        }, {
            label: '所属机构',
            name: 'dept.org.name'
        }, {
            label: "操作",
            name: "id",
            width: 120,
            fixed: true,
            hidden: editAble ? false : true,
            formatter: function (value, options, row) {
                var actions = [];
                actions.push('<a href="javascript:void(0)" class="actions edit-item" data-id="' + row.id + '" title="编辑" ><i class="fa fa-pencil"></i></a>');
                actions.push('<a href="javascript:void(0)" class="actions password-item" data-id="' + row.id + '" title="修改密码" ><i class="fa fa-key"></i></a>');
                actions.push('<a href="javascript:void(0)" class="actions role-setting" data-id="' + row.id + '" title="角色设置"><i class="fa fa-user"></i></a>');
                actions.push('<a href="javascript:void(0)" class="actions delete-item" data-id="' + row.id + '" title="删除"><i class="fa fa-trash-o"></i></a>');
                return actions.join("");
            }
        }]
    }).enableAutoHeight(".content", ".other-items");

    // 编辑
    $("#table").on("click", ".edit-item", function () {
        var id = $(this).attr("data-id");
        Util.innerIframe(Util.getPath() + "/user/form/" + id);
    });

    // 修改密码
    $("#table").on("click", ".password-item", function () {
        var id = $(this).attr("data-id");
        top.layer.open({
            type: 1,
            content: $("#changepasswoprd").html(),
            btn: ['确定', '取消'],
            yes: function (index, layero) {
                var password = layero.find("#password").val();
                if (password) {
                    if (password.length < 6) {
                        Util.msgWarning("密码不能小于6位！");
                    }

                    var confirmPassword = layero.find("#confirmpassword").val();
                    if (password === confirmPassword) {
                        $.post(Util.getPath() + "/user/password/change", {
                            id: id,
                            password: password
                        }, function (res) {
                            if (res.code === 200) {
                                Util.closeLayer(index);
                                Util.msgOk("密码修改成功！");
                            }
                        }, "JSON");
                    } else {
                        Util.msgWarning("两次输入的密码不一致！");
                    }
                } else {
                    Util.msgWarning("密码不能为空！");
                }

            }
        });
    });

    // 角色设置
    $("#table").on("click", ".role-setting", function () {
        top.layer.open({
            type: 2,
            area: ['800px', '450px'],
            scrollbar: false,
            resize: false,
            title: "角色设置",
            content: Util.getPath() + "/user/roles/form?userId=" + $(this).attr("data-id"),
            yes: function (index, layero) {
                var contentWindow = layero.find('iframe')[0].contentWindow;
                contentWindow.submitForm(function (res) {
                    if (res.code === 200) {
                        Util.msgOk("操作成功！");
                        Util.closeLayer(index);
                    } else {
                        Util.msgError("操作失败！");
                    }
                });
            }
        });
    });

    // 删除
    $("#table").on("click", ".delete-item", function () {
        deletes($(this).attr("data-id"));
    });

    //查询
    $("#btn-search").click(function () {
        gridTable.setGridParam({
            postData: {
                username: $("#username").val(),
                name: $("#name").val(),
                userType: $("#userType").val(),
                state: $("#state").val()
            }
        }).trigger("reloadGrid", {
            page: 1
        });
    });

    //重置
    $("#btn-reset").click(function () {
        $("#search input").val("");
        $("#search select").val("").trigger("change");
        $("#btn-search").click();
    });

    //新增
    $("#btn-add").click(function () {
        Util.innerIframe(Util.getPath() + "/user/form");
    });

    //批量删除
    $("#btn-delete").click(function () {
        var selectedRow = $("#table").jqGrid('getGridParam', 'selarrrow');
        console.log(selectedRow);
        if (selectedRow.length === 0) {
            Util.msgWarning("请选择需要删除的数据！");
            return;
        }
        deletes(selectedRow.join(","));
    });

    function deletes(ids) {
        Util.ask("确定删除？", function (index) {
            Util.closeLayer(index);
            Util.postLoading(Util.getPath() + "/user/deletes", {ids: ids}, function (res) {
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