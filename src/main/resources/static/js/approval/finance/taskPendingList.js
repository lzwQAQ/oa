$(function () {

    $("#search select").select2({
        width: "200px"
    });

    var gridTable = $('#table').jqGrid({
        url: Util.getPath() + "/finance/pending/list",
        rownumbers: true,
        jsonReader: {
            id: "taskId"
        },
        colModel: [{
            label: '业务名称',
            name: 'business',
            formatter: function (value, options, row) {
                return '<a href="javascript:void(0)" class="edit-item" data-id="' + row.taskId + '">' + value + '</a>';
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
            label: '申请人',
            name: 'sender'
        }, {
            label: '申请时间',
            name: 'time',
            width: 160,
            fixed: true
        }, {
            name: 'taskId',
            hidden: true
        }]
    }).enableAutoHeight(".content", ".other-items");

    // 编辑
    $("#table").on("click", ".edit-item", function () {
        var row = $("#table").jqGrid('getRowData', $(this).attr("data-id"));
        top.window.simpleWorkFlow_ReloadGrid = function () {
            $('#table').trigger("reloadGrid");
        };
        Util.fullIframe({
            title: '当前环节：' + row.taskName,
            content: Util.getPath() + '/pendingtask/handle?taskId=' + row.taskId
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