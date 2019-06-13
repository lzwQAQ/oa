$(function () {

    $('.daterange').daterangepicker({
        autoUpdateInput: false,
        timePicker: true,
        timePicker24Hour: true,
        locale: {
            cancelLabel: '清除',
            applyLabel: '确定'
        }
    }).on('apply.daterangepicker', function (ev, picker) {
        $(this).val(picker.startDate.format('YYYY-MM-DD') + '~' + picker.endDate.format('YYYY-MM-DD'));
    }).on('cancel.daterangepicker', function (ev, picker) {
        $(this).val('');
    });

    var gridTable = $('#table').jqGrid({
        url: Util.getPath() + "/report/list",
        rownumbers: true,
        colModel: [{
            label: '任务名称',
            name: 'plan.name',
            formatter: function (value, options, row) {
                return '<a href="javascript:void(0)" class="edit-item" data-id="' + row.id + '">' + value + '</a>';
            }
        }, {
            label: '报告人',
            name: 'sender'
        }, {
            label: '负责人',
            name: 'chargePeople'
        }, {
            label: '发布人',
            name: 'plan.sender'
        }, {
            label: '工时',
            name: 'workHour'
        }, {
            label: '日报时间',
            name: 'reportTime'
        }]
    }).enableAutoHeight(".content", ".other-items");

    // 编辑
    $("#table").on("click", ".edit-item", function () {
        var id = $(this).attr("data-id");
        Util.innerIframe(Util.getPath() + "/report/detail/" + id);
    });

    //刷新
    $("#btn-refresh").click(function () {
        gridTable.trigger("reloadGrid");
    });
    //查询
    $("#btn-search").click(function () {
        gridTable.setGridParam({
            postData: {
                "plan.name": $("#planName").val(),
                sender: $("#sender").val(),
                beginReportTime: $("#reportTime").val().split("~")[0],
                endReportTime: $("#reportTime").val().split("~")[1]
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

});

function reloadGrid() {
    $('#table').trigger("reloadGrid");
}