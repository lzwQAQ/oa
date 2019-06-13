$(function () {

    var gridTable = $('#table').jqGrid({
        url: Util.getPath() + "/org/tree/list",
        treeGrid: true,
        colModel: [{
            label: '机构代码',
            name: 'code',
            formatter: function (value, options, row) {
                return editAble ? ('<a href="javascript:void(0)" class="edit-item" data-id="' + row.id + '">' + value + '</a>') : value;
            }
        }, {
            label: '机构全称',
            name: 'name'
        }, {
            label: '机构简称',
            name: 'simpleName'
        }, {
            label: '排序号',
            name: 'sort',
            width: 100,
            fixed: true
        }, {
            label: '更新时间',
            name: 'updateDate',
            width: 150,
            fixed: true
        }, {
            label: "操作",
            name: "id",
            width: 100,
            fixed: true,
            hidden: editAble ? false : true,
            formatter: function (value, options, row) {
                var actions = [];
                actions.push('<a href="javascript:void(0)" class="actions edit-item" data-id="' + row.id + '" title="编辑机构" ><i class="fa fa-pencil"></i></a>');
                // actions.push('<a href="${ctx}/sys/office/disable?officeCode=" class="actions" title="停用机构"><i class="glyphicon glyphicon-ban-circle"></i></a>');
                // actions.push('<a href="${ctx}/sys/office/enable?officeCode=" class="actions" title="启用机构"><i class="glyphicon glyphicon-ok-circle"></i></a>');
                actions.push('<a href="javascript:void(0)" class="actions delete-item" data-id="' + row.id + '" title="删除机构"><i class="fa fa-trash-o"></i></a>');
                actions.push('<a href="javascript:void(0)" class="actions add-child-item" data-id="' + row.id + '" title="新增下级机构"><i class="fa fa-plus-square"></i></a>');
                return actions.join("");
            }
        }]
    }).enableAutoHeight(".content", ".other-items");

    // 编辑
    $("#table").on("click", ".edit-item", function () {
        var id = $(this).attr("data-id");
        Util.innerIframe(Util.getPath() + "/org/form/" + id);
    });

    // 删除
    $("#table").on("click", ".delete-item", function () {
        deletes($(this).attr("data-id"));
    });

    // 新增下级机构
    $("#table").on("click", ".add-child-item", function () {
        var id = $(this).attr("data-id");
        Util.innerIframe(Util.getPath() + "/org/child/create/" + id)
    });

    //刷新
    $("#btn-refresh").click(function () {
        gridTable.trigger("reloadGrid");
    });

    //新增
    $("#btn-add").click(function () {
        Util.innerIframe(Util.getPath() + "/org/form");
    });

    function deletes(ids) {
        Util.ask("确定删除？", function (index) {
            Util.closeLayer(index);
            Util.postLoading(Util.getPath() + "/org/deletes", {ids: ids}, function (res) {
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