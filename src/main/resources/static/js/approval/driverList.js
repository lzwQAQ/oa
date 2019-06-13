$(function () {


    var gridTable = $('#table').jqGrid({
        url: Util.getPath() + "/driver/list",
        multiselect: true,
        colModel: [{
            label: '姓名',
            name: 'user.name',
            formatter: function (value, options, row) {
                return '<a href="javascript:void(0)" class="edit-item" data-id="' + row.id + '">' + value + '</a>';
            }
        }, {
            label: '性别',
            name: 'user.sex',
            formatter: function (value, options, row) {
                return "1" === value ? "男" : "女";
            }
        }, {
            label: '手机号',
            name: 'user.phone'
        }, {
            label: '驾龄',
            name: 'drivingYear'
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

    // 编辑
    $("#table").on("click", ".edit-item", function () {
        var id = $(this).attr("data-id");
        Util.innerIframe(Util.getPath() + "/driver/form/" + id);
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
                phone: $("#phone").val()
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
        Util.innerIframe(Util.getPath() + "/driver/form");
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
            Util.postLoading(Util.getPath() + "/driver/deletes", {ids: ids}, function (res) {
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