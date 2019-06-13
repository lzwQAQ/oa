$(function () {

    var gridTable = $('#table').jqGrid({
        url: Util.getPath() + "/simpleworkflow/historic/list",
        rownumbers: true,
        colModel: [{
            label: '标题',
            name: 'title',
            formatter: function (value, options, row) {
                return '<a href="javascript:void(0)" class="edit-item" data-id="' + row.id + '">' + value + '</a>';
            }
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
                title: $("#title").val()
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