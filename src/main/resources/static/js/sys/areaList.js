$(function () {


    var gridTable = $('#table').jqGrid({
        url: Util.getPath() + "/area/tree/list",
        treeGrid: true,
        colModel: [{
            label: '区划编码',
            name: 'code',
            formatter: function (value, options, row) {
                return editAble ? ('<a href="javascript:void(0)" class="edit-item" data-id="' + row.id + '">' + value + '</a>') : value;
            }
        }, {
            label: '区划名称',
            name: 'name'
        }, {
            label: '更新时间',
            name: 'updateDate'
        }, {
            label: '序号',
            name: 'sort',
            width: 100,
            fixed: true
        }, {
            label: "操作",
            name: "id",
            width: 100,
            fixed: true,
            hidden: editAble ? false : true,
            autowidth: false,
            formatter: function (value, options, row) {
                var actions = [];
                actions.push('<a href="javascript:void(0)" class="actions edit-item" data-id="' + row.id + '" title="编辑" ><i class="fa fa-pencil"></i></a>');
                actions.push('<a href="javascript:void(0)" class="actions delete-item" data-id="' + row.id + '" title="删除"><i class="fa fa-trash-o"></i></a>');
                actions.push('<a href="javascript:void(0)" class="actions add-child-item" data-id="' + row.id + '" title="新增下级区划"><i class="fa fa-plus-square"></i></a>');
                return actions.join("");
            }
        }]
    }).enableAutoHeight(".content", ".other-items");

    // 编辑
    $("#table").on("click", ".edit-item", function () {
        var id = $(this).attr("data-id");
        Util.innerIframe(Util.getPath() + "/area/form/" + id);
    });

    // 删除
    $("#table").on("click", ".delete-item", function () {
        deletes($(this).attr("data-id"));
    });

    // 新增下级区划
    $("#table").on("click", ".add-child-item", function () {
        var id = $(this).attr("data-id");
        Util.innerIframe(Util.getPath() + "/area/child/create/" + id);
    });

    //查询
    $("#btn-search").click(function () {
        gridTable.setGridParam({
            postData: {
                code: $("#code").val(),
                name: $("#name").val()
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

    //新增
    $("#btn-add").click(function () {
        Util.innerIframe(Util.getPath() + "/area/form");
    });


    function deletes(ids) {
        Util.ask("确定删除？", function (index) {
            Util.closeLayer(index);
            Util.postLoading(Util.getPath() + "/area/deletes", {ids: ids}, function (res) {
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