$(function () {

    $("select").select2({
        width: "200px"
    });

    var gridTable = $('#table').jqGrid({
        url: Util.getPath() + "/emailsend/draft/list",
        multiselect: true,
        colModel: [{
            label: '<div class="email-state rt"></div> <i class="fa fa-paperclip"></i>',
            name: 'id',
            width: 55,
            fixed: true,
            formatter: function (value, options, row) {
                var actions = [];
                actions.push('<div class="email-state ' + (row.isSchedule === '1' ? "ti" : "rc") + '"></div> ');
                actions.push(row.containFile === "1" ? '<i class="fa fa-paperclip"></i>' : "");
                return actions.join("");
            }
        }, {
            label: '收件人',
            name: 'receiverName',
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
            name: 'isSchedule',
            hidden: true
        }]
    }).enableAutoHeight(".content", ".other-items");

    // 查看详情
    $("#table").on("click", ".edit-item", function () {
        var id = $(this).attr("data-id");
        var data = gridTable.jqGrid('getRowData', id, true);
        if (data.isSchedule === '1') {
            Util.innerIframe(Util.getPath() + "/email/view/send/" + id);
        } else {
            Util.innerIframe(Util.getPath() + "/emailsend/form/" + id);
        }
    });

    // 删除
    $("#table").on("click", ".delete-item", function () {
        updateDeletes($(this).attr("data-id"));
    });

    //查询
    $("#btn-search").click(function () {
        gridTable.setGridParam({
            postData: {
                title: $("#title").val(),
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

    function deletes(ids) {
        Util.ask("确定删除？", function (index) {
            Util.closeLayer(index);
            Util.postLoading(Util.getPath() + "/emailsend/deletes", {ids: ids}, function (res) {
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