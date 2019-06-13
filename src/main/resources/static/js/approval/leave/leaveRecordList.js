$(function () {

    $("#search select").select2({
        width: "200px"
    });

    $('.daterange').daterangepicker({
        autoUpdateInput: false,
        timePicker: false,
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
        url: Util.getPath() + "/leaverecord/record/list",
        rownumbers: true,
        jsonReader: {
            id: "taskId"
        },
        colModel: [{
            label: '申请人',
            name: 'senderName',
            formatter: function (value, options, row) {
                return '<a href="javascript:void(0)" class="edit-item" data-id="' + row.id + '">' + value + '</a>';
            }
        }, {
            label: '申请部门 ',
            name: 'senderDeptName'
        }, {
            label: '请假类型',
            name: 'type',
            width: 120,
            fixed: true
        }, {
            label: '请假天数',
            name: 'leaveDay',
            width: 100,
            fixed: true
        }, {
            label: '请假开始时间',
            name: 'startTime',
            width: 160,
            fixed: true
        }, {
            label: '请假结束时间',
            name: 'endTime',
            width: 160,
            fixed: true
        }]
    }).enableAutoHeight(".content", ".other-items");

    // 编辑
    $("#table").on("click", ".edit-item", function () {
        var id = $(this).attr("data-id");
        Util.innerIframe(Util.getPath() + "/leaverecord/record/" + id + "?view=true");
    });

    //查询
    $("#btn-search").click(function () {
        gridTable.setGridParam({
            postData: {
                senderName: $("#senderName").val(),
                type: $("#type").val(),
                beginLeaveTime: $("#leaveTime").val().split("~")[0],
                endLeaveTime: $("#leaveTime").val().split("~")[1]
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

    //添加
    $("#btn-add").click(function () {
        Util.innerIframe(Util.getPath() + "/leaverecord/form/");
    });

    //导出数据
    $("#btn-export").click(function () {
        var beginLeaveTime = $("#leaveTime").val().split("~")[0];
        beginLeaveTime = beginLeaveTime ? beginLeaveTime : "";
        var endLeaveTime = $("#leaveTime").val().split("~")[1];
        endLeaveTime = endLeaveTime ? endLeaveTime : "";
        var url = Util.getPath() + "/leaverecord/exportrecord?senderName=" + $("#senderName").val() +
            "&type=" + $("#type").val() + "&beginLeaveTime=" + beginLeaveTime +
            "&endLeaveTime=" + endLeaveTime;
        url = encodeURI(url);
        console.log(url);
        top.window.open(url, "_blank");
    });
});

function reloadGrid() {
    $('#table').trigger("reloadGrid");
}