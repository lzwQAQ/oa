$(function () {

    $("#search select").select2({
        width: "200px"
    });

    var gridTable = $('#table').jqGrid({
        url: Util.getPath() + "/reception/historic/list",
        rownumbers: true,
        colModel: [{
            label: '饭店名称',
            name: 'hotelName',
            width: 120,
            fixed: true,
            formatter: function (value, options, row) {
                return '<a href="javascript:void(0)" class="edit-item" data-id="' + row.id + '">' + value + '</a>';
            }
        },{
            label: '吃饭标准',
            name: 'mealStandards'
        }, {
            label: '来宾数',
            name: 'guest'
        }, {
            label: '陪客数',
            name: 'accompany'
        }, {
            label: '时间',
            name: 'startTime'
        }, {
            label: '所属流程',
            name: 'processName'
        }, {
            label: '申请人',
            name: 'sender'
        }, {
            label: '申请时间',
            name: 'time',
            width: 160,
            fixed: true
        }, {
            name: 'processInstanceId',
            hidden: true
        }]
    }).enableAutoHeight(".content", ".other-items");

    // 编辑
    $("#table").on("click", ".edit-item", function () {
        var row = $("#table").jqGrid('getRowData', $(this).attr("data-id"));
        Util.fullIframe({
            title: '流程名称：' + row.processName,
            content: Util.getPath() + '/historictask/details?processInstanceId=' + row.processInstanceId
        });
    });

    //查询
    $("#btn-search").click(function () {
        gridTable.setGridParam({
            postData: {
                "hotelName": $("#hotelName").val(),
                type: $("#type").val(),
                senderName: $("#senderName").val()
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
});

function reloadGrid() {
    $('#table').trigger("reloadGrid");
}