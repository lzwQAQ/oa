$(function () {

    var gridTable = $('#table').jqGrid({
        url: Util.getPath() + "/releasedocument/filetemplete/list",
        multiselect: true,
        colModel: [{
            label: '文件名称',
            name: 'fileName',
            formatter: function (value, options, row) {
                return '<a href="javascript:void(0)" class="edit-item" data-id="' + row.id + '">' + value + '</a>';
            }
        }, {
            label: '文件大小',
            name: 'fileSize',
            formatter: function (value, options, row) {
                var v = value / (1024 * 1024);
                return v > 1 ? (v.toFixed(2) + " MB") : ((value / 1024).toFixed(2) + " KB");
            }
        }, {
            label: "操作",
            name: "id",
            width: 100,
            fixed: true,
            formatter: function (value, options, row) {
                var actions = [];
                actions.push('<a href="javascript:void(0)" class="actions download-item" data-id="' + row.id + '" title="下载" ><i class="fa fa-download"></i></a>');
                if (officeViewEnable) {
                    actions.push('<a href="javascript:void(0)" class="actions view-item" data-id="' + row.id + '" title="预览"><i class="fa fa-eye"></i></a>');
                }
                actions.push('<a href="javascript:void(0)" class="actions delete-item" data-id="' + row.id + '" title="删除"><i class="fa fa-trash-o"></i></a>');
                return actions.join("");
            }
        }, {
            hidden: true,
            name: "filePath"
        }]
    }).enableAutoHeight(".content", ".other-items");

    // 编辑
    $("#table").on("click", ".edit-item", function () {
        var id = $(this).attr("data-id");
        Util.innerIframe(Util.getPath() + "/releasedocument/filetemplete/form/" + id);
    }).on("click", ".view-item", function () {
        var id = $(this).attr("data-id");
        var data = gridTable.jqGrid('getRowData', id, true);
        var url = officeUrl + officeAddress + data.filePath;
        top.window.open(encodeURI(url), "_blank");
    });

    // 删除
    $("#table").on("click", ".delete-item", function () {
        deletes($(this).attr("data-id"));
    });

    // 下载
    $("#table").on("click", ".download-item", function () {
        var id = $(this).attr("data-id");
        window.open(Util.getPath() + "/releasedocument/filetemplete/download/" + id, "_blank");
    });

    //刷新
    $("#btn-refresh").click(function () {
        gridTable.trigger("reloadGrid");
    });
    //查询
    $("#btn-search").click(function () {
        gridTable.setGridParam({
            postData: {
                fileName: $("#fileName").val()
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
        Util.innerIframe(Util.getPath() + "/releasedocument/filetemplete/form");
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
            Util.postLoading(Util.getPath() + "/releasedocument/filetemplete/deletes", {ids: ids}, function (res) {
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