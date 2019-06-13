$(function () {

    var gridTable = $('#table').jqGrid({
        url: Util.getPath() + "/dictdata/list/" + Util.getQueryString("dictType"),
        colModel: [{
            label: '字典值',
            name: 'dictValue',
            formatter: function (value, options, row) {
                return editAble ? ('<a href="javascript:void(0)" class="edit-item" data-id="' + row.id + '">' + value + '</a>') : value;
            }
        }, {
            label: '字典代码',
            name: 'dictCode'
        }, {
            label: '字典类型',
            name: 'dictType'
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
            formatter: function (value, options, row) {
                var actions = [];
                actions.push('<a href="javascript:void(0)" class="actions edit-item" data-id="' + row.id + '" title="编辑" ><i class="fa fa-pencil"></i></a>');
                actions.push('<a href="javascript:void(0)" class="actions delete-item" data-id="' + row.id + '" title="删除"><i class="fa fa-trash-o"></i></a>');
                return actions.join("");
            }
        }]
    }).enableAutoHeight(".content", ".other-items");

    $("#btn-back").click(function () {
        parent.layer.close(parent.layer.getFrameIndex(window.name));
    });

    // 编辑
    $("#table").on("click", ".edit-item", function () {
        var id = $(this).attr("data-id");
        Util.innerIframe(Util.getPath() + "/dictdata/form/" + id);
    });

    // 删除
    $("#table").on("click", ".delete-item", function () {
        deletes($(this).attr("data-id"));
    });

    //查询
    $("#btn-search").click(function () {
        gridTable.setGridParam({
            postData: {
                dictCode: $("#dictCode").val(),
                dictValue: $("#dictValue").val()
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
        Util.innerIframe(Util.getPath() + "/dictdata/form");
    });

    //批量删除
    $("#btn-delete").click(function () {
        var selectedRow = $("#table").jqGrid('getGridParam', 'selarrrow');
        if (selectedRow.length === 0) {
            Util.msgWarning("请选择需要删除的数据！");
            return;
        }
        deletes(selectedRow.join(","));
    });

    function deletes(ids) {
        Util.ask("确定删除？", function (index) {
            Util.closeLayer(index);
            Util.postLoading(Util.getPath() + "/dictdata/deletes", {ids: ids}, function (res) {
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