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
        url: Util.getPath() + "/accountsrecord/record/list",
        rownumbers: true,
        colModel: [{
            label: '业务名称',
            name: 'name',
            formatter: function (value, options, row) {
                return '<a href="javascript:void(0)" class="edit-item" data-id="' + row.id + '">' + value + '</a>';
            }
        }, {
            label: "费用支出/元",
            name: "totalPrice",
            width: 160,
            fixed: true
        }, {
            label: '申请人',
            name: 'senderName'
        }, {
            label: '申请部门 ',
            name: 'senderDeptName'
        }, {
            label: '申请时间',
            name: 'createDate',
            width: 160,
            fixed: true,
            formatter: function (value, options, row) {
                return value.split(" ")[0];
            }
        }]
    }).enableAutoHeight(".content", ".other-items");

    // 编辑
    $("#table").on("click", ".edit-item", function () {
        var id = $(this).attr("data-id");
        Util.innerIframe(Util.getPath() + "/accountsrecord/record/" + id + "?view=true");
    });

    //查询
    $("#btn-search").click(function () {
        gridTable.setGridParam({
            postData: {
                senderName: $("#senderName").val(),
                name: $("#name").val(),
                beginSendTime: $("#sendTime").val().split("~")[0],
                endSendTime: $("#sendTime").val().split("~")[1]
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
        Util.innerIframe(Util.getPath() + "/accountsrecord/form/");
    });

    //导出数据
    $("#btn-export").click(function () {
        var beginLeaveTime = $("#sendTime").val().split("~")[0];
        beginLeaveTime = beginLeaveTime ? beginLeaveTime : "";
        var endLeaveTime = $("#sendTime").val().split("~")[1];
        endLeaveTime = endLeaveTime ? endLeaveTime : "";
        var url = Util.getPath() + "/accountsrecord/exportrecord";
        url = encodeURI(url);
        console.log(url);
        top.window.open(url, "_blank");
    });
});

function reloadGrid() {
    $('#table').trigger("reloadGrid");
}