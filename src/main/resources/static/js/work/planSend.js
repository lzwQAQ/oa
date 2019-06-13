$(function () {

    $("#search select").select2({
        width: "200px"
    });

    var gridTable = $('#table').jqGrid({
        url: Util.getPath() + "/plan/sendlist",
        multiselect: true,
        colModel: [{
            label: '任务名称',
            name: 'name',
            formatter: function (value, options, row) {
                return '<a href="javascript:void(0)" class="detail-item" data-id="' + row.id + '">' + value + '</a>';
            }
        }, {
            label: '负责人',
            name: 'chargePeople'
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
        }, {
            label: "操作",
            name: "id",
            width: 100,
            fixed: true,
            formatter: function (value, options, row) {
                var actions = [];
                actions.push('<a href="javascript:void(0)" class="actions edit-item" data-id="' + row.id + '" title="编辑" ><i class="fa fa-pencil"></i></a>');
                actions.push('<a href="javascript:void(0)" class="actions delete-item" data-id="' + row.id + '" title="删除"><i class="fa fa-trash-o"></i></a>');
                return actions.join("");
            }
        }]
    }).enableAutoHeight(".content", ".other-items");

    $("#btn-add").click(function () {
        Util.innerIframe(Util.getPath() + "/plan/form/");
    });

    // 详情
    $("#table").on("click", ".detail-item", function () {
        var id = $(this).attr("data-id");
        Util.innerIframe(Util.getPath() + "/plan/detail/" + id);
    });

    // 编辑
    $("#table").on("click", ".edit-item", function () {
        var id = $(this).attr("data-id");
        Util.innerIframe(Util.getPath() + "/plan/form/" + id);
    });

    // 删除
    $("#table").on("click", ".delete-item", function () {
        deletes($(this).attr("data-id"));
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

    function deletes(ids) {
        Util.ask("确定删除？", function (index) {
            Util.closeLayer(index);
            Util.postLoading(Util.getPath() + "/plan/deletes", {ids: ids}, function (res) {
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