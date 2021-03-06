$(function () {

    $("select").select2({
        width: "200px"
    });

    var gridTable = $('#table').jqGrid({
        url: Util.getPath() + "/email/recyclebin/list",
        multiselect: true,
        colModel: [{
            label: '<div class="email-state rt"></div> <i class="fa fa-paperclip"></i>',
            name: 'id',
            width: 55,
            fixed: true,
            formatter: function (value, options, row) {
                var actions = [];
                actions.push('<div class="email-state ' + (row.read === '1' ? 'rr' : 'ru') + '"></div> ');
                actions.push(row.containFile === "1" ? '<i class="fa fa-paperclip"></i>' : "");
                return actions.join("");
            }
        }, {
            label: '发件人',
            name: 'senderName',
            width: 200,
            fixed: true
        }, {
            label: '主题',
            name: 'title',
            formatter: function (value, options, row) {
                var actions = [];
                actions.push(row.level === '1' ? "<span class='text-warning email-level'>(重要)</span>" : (row.level === '2' ? "<span class='text-danger email-level'>(紧急)</span>" : ""));
                actions.push('<a href="javascript:void(0)" class="edit-item" data-id="' + row.id + '">' + value + '</a>');
                return actions.join("");
            }
        }, {
            label: '类型',
            name: 'type',
            width: 120,
            fixed: true
        }, {
            label: '时间',
            name: 'createDate',
            width: 180,
            fixed: true
        }, {
            name: 'emailType',
            hidden: true
        }, {
            name: 'read',
            hidden: true
        }, {
            label: "操作",
            name: "id",
            width: 100,
            fixed: true,
            formatter: function (value, options, row) {
                var actions = [];
                actions.push('<a href="javascript:void(0)" class="actions delete-item" data-id="' + row.id + '" title="删除"><i class="fa fa-trash-o"></i></a>');
                return actions.join("");
            }
        }],
        gridComplete: function () {
            var ids = $("#table").getDataIDs();
            for (var i = 0; i < ids.length; i++) {
                var rowData = $("#table").getRowData(ids[i]);
                if (rowData.read !== '1') {
                    $('#table tr#' + ids[i]).css("font-weight", "bolder");
                }
            }
        }
    }).enableAutoHeight(".content", ".other-items");

    // 编辑
    $("#table").on("click", ".edit-item", function () {
        var id = $(this).attr("data-id");
        var data = gridTable.jqGrid('getRowData', id, true);
        Util.innerIframe(Util.getPath() + "/email/view/" + data.emailType + "/" + id);
    });

    // 删除
    $("#table").on("click", ".delete-item", function () {
        deletes($(this).attr("data-id"));
    });

    //刷新
    $("#btn-refresh").click(function () {
        gridTable.trigger("reloadGrid");
    });
    //查询
    $("#btn-search").click(function () {
        gridTable.setGridParam({
            postData: {
                title: $("#title").val(),
                senderName: $("#senderName").val(),
                type: $("#type").val(),
                level: $("#level").val()
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

    //批量删除
    $("#btn-delete").click(function () {
        var selectedRow = $("#table").jqGrid('getGridParam', 'selarrrow');
        if (selectedRow.length === 0) {
            Util.msgWarning("请选择需要删除的数据！");
            return;
        }
        deletes(selectedRow.join(","));
    });

    //批量恢复
    $("#btn-delete-recovery").click(function () {
        var selectedRow = $("#table").jqGrid('getGridParam', 'selarrrow');
        if (selectedRow.length === 0) {
            Util.msgWarning("请选择需要删除的数据！");
            return;
        }
        recoverys(selectedRow.join(","));
    });

    function deletes(ids) {
        Util.ask("确定删除？", function (index) {
            Util.closeLayer(index);
            Util.postLoading(Util.getPath() + "/email/deletes", {ids: ids}, function (res) {
                if (res.code === 200) {
                    Util.msgOk("删除成功！");
                    gridTable.trigger("reloadGrid");
                } else {
                    Util.msgError("删除失败！");
                }
            });
        });
    }

    function recoverys(ids) {
        Util.ask("确定恢复？", function (index) {
            Util.closeLayer(index);
            Util.postLoading(Util.getPath() + "/email/recoverys", {ids: ids}, function (res) {
                if (res.code === 200) {
                    Util.msgOk("恢复成功！");
                    gridTable.trigger("reloadGrid");
                } else {
                    Util.msgError("恢复失败！");
                }
            });
        });
    }
});

function reloadGrid() {
    $('#table').trigger("reloadGrid");
}