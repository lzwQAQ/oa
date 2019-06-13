$(function () {
    //----------------------第一个列表--------------------------------
    var gridTable = $('#table').jqGrid({
        url: Util.getPath() + "/user/role/list/" + Util.getQueryString("userId"),
        multiselect: true,
        pager: null,
        colModel: [{
            label: '角色名称',
            name: 'name',
            width: 100
        }, {
            label: '角色代码',
            name: 'code',
            width: 100
        }, {
            label: '系统内置',
            name: 'isSys',
            width: 70,
            formatter: function (value, options, row) {
                return value === "1" ? "是" : "否";
            }
        }, {
            label: '所属机构',
            name: 'orgName'
        }]
    }).enableAutoHeight(".content", ".other-items", -41);

    //查询
    $("#btn-search").click(function () {
        gridTable.setGridParam({
            postData: {
                roleName: $("#roleName").val()
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

    //----------------------第二个列表--------------------------------

    function initGridTable2() {
        if (!gridTable2) {
            gridTable2 = $('#table2').jqGrid({
                url: Util.getPath() + "/user/roles/" + Util.getQueryString("userId"),
                multiselect: true,
                pager: null,
                colModel: [{
                    label: '角色名称',
                    name: 'name',
                    width: 100
                }, {
                    label: '角色代码',
                    name: 'code',
                    width: 100
                }, {
                    label: '系统内置',
                    name: 'isSys',
                    width: 70,
                    formatter: function (value, options, row) {
                        return value === "1" ? "是" : "否";
                    }
                }, {
                    label: '所属机构',
                    name: 'orgName'
                }]
            }).enableAutoHeight(".content", ".other-items", -41);
        }
    }

    var gridTable2;

    //查询
    $("#btn-search2").click(function () {
        gridTable2.setGridParam({
            postData: {
                roleName: $("#roleName2").val()
            }
        }).trigger("reloadGrid", {
            page: 1
        });
    });

    //重置
    $("#btn-reset2").click(function () {
        $("#search2 input").val("");
        $("#btn-search2").click();
    });

    //-----------------------切换以及提交和删除-------------------------------
    $(".presentation").click(function () {
        if ($(this).hasClass("active") === true) {
            return;
        }

        $(".presentation").toggleClass("active");
        $(".table").toggle();
        $(".layui-layer-btn0").toggle();
        initGridTable2();
    });

    //点击提交
    $(".btn-confirm").click(function () {
        var selectedRow = $("#table").jqGrid('getGridParam', 'selarrrow');
        var userId = Util.getQueryString("userId");
        $.post(Util.getPath() + "/user/roles/" + userId + "/save", {roles: selectedRow.join(",")}, function (res) {
            if (res.code === 200) {
                parent.layer.close(parent.layer.getFrameIndex(window.name));
                Util.msgOk("提交成功！");
            } else {
                Util.msgError("操作失败！");
            }
        }, "JSON");
    });

    //点击删除
    $(".btn-delete").click(function () {
        var selectedRow = $("#table2").jqGrid('getGridParam', 'selarrrow');
        $.post(Util.getPath() + "/user/roles/delete", {ids: selectedRow.join(",")}, function (res) {
            if (res.code === 200) {
                parent.layer.close(parent.layer.getFrameIndex(window.name));
                Util.msgOk("提交成功！");
            } else {
                Util.msgError("操作失败！");
            }
        }, "JSON");
    });

    //点击取消
    $(".btn-cancle").click(function () {
        parent.layer.close(parent.layer.getFrameIndex(window.name));
    });
});