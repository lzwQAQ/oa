$(function () {

    $("#search select").select2({
        width: "200px"
    });

    var gridTable = $('#table').jqGrid({
        url: Util.getPath() + "/finance/send/list",
        rownumbers: true,
        colModel: [{
            label: '业务名称',
            name: 'business',
            formatter: function (value, options, row) {
                return '<a href="javascript:void(0)" class="edit-item" data-id="' + row.id + '">' + value + '</a>';
            }
        }, {
            label: '申请金额',
            name: 'money'
        }, {
            label: '所属流程',
            name: 'processName'
        }, {
            label: '当前环节',
            name: 'taskName'
        }, {
            label: '审批状态',
            name: 'taskName',
            formatter: function (value, options, row) {
                return '结束' === value ? '通过' : ("发起请假" === value ? '不通过' : '审批中');
            }
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
                business: $("#business").val()
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