$(function () {

    var gridTable = $('#table').jqGrid({
        url: Util.getPath() + "/released/findReleasedWorkFlow",
        rownumbers: true,
        colModel: [{
            name: 'key',
            label: '流程key'
        }, {
            name: 'name',
            label: '流程名称'
        }, {
            name: 'version',
            label: '版本'
        }, {
            name: 'time',
            label: '流程部署时间'
        }, {
            name: "procdefId",
            label: "流程查看",
            width: 100,
            fixed: true,
            formatter: function (value, options, row) {
                var actions = [];
                actions.push('<a href="javascript:void(0)" class="actions view-item" data-id="' + value + '" title="查看流程图"><i class="fa fa-picture-o"></i></a>');
                return actions.join("");
            }
        }]
    }).enableAutoHeight(".content", ".other-items");

    // 查看图片
    $("#table").on("click", ".view-item", function () {
        var src = Util.getPath() + '/released/imageResource?processDefinitionId=' + $(this).attr("data-id") + '&stime=' + new Date().getTime();
        var index = layer.open({
            type: 1,
            content: '<img src="' + src + '">',
            title: "流程图查看",
            scrollbar: true,
            move: false,
            resize: false
        });
        layer.full(index);
    });

    //查询
    $("#btn-search").click(function () {
        gridTable.setGridParam({
            postData: {
                modelKey: $("#modelKey").val(),
                modelName: $("#modelName").val()
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