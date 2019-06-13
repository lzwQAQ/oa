$(function () {

    $("#search select").select2({
        width: "200px"
    });

    var gridTable = $('#table').jqGrid({
        url: Util.getPath() + "/plan/list",
        rownumbers: true,
        colModel: [{
            label: '任务名称',
            name: 'name',
            formatter: function (value, options, row) {
                return '<a href="javascript:void(0)" class="edit-item" data-id="' + row.id + '">' + value + '</a>';
            }
        }, {
            label: '负责人',
            name: 'chargePeople'
        }, {
            label: '发布人',
            name: 'sender'
        }, {
            label: '任务状态',
            name: 'state'
        }, {
            label: '任务进度',
            name: 'process',
            formatter: function (value, options, row) {
                return value + '%';
            }
        }, {
            label: '开始时间',
            name: 'beginTime'
        }, {
            label: '结束时间',
            name: 'endTime'
        }]
    }).enableAutoHeight(".content", ".other-items");

    // 查看
    $("#table").on("click", ".edit-item", function () {
        var id = $(this).attr("data-id");
        Util.innerIframe(Util.getPath() + "/plan/detail/" + id);
    });

    //刷新
    $("#btn-refresh").click(function () {
        gridTable.trigger("reloadGrid");
    });

    //查询
    $("#btn-search").click(function () {
        gridTable.setGridParam({
            postData: {
                name: $("#name").val(),
                state: $("#state").val()
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