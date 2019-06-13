$(function () {

    $.validator.addMethod("modek-key", function (value, element) {
        var tel = /^[a-zA-Z][a-zA-Z0-9_]*$/;
        return this.optional(element) || (tel.test(value));
    }, "该输入项只能输入英文字母、数字及下划线，以英文字母开头");

    var gridTable = $('#table').jqGrid({
        url: Util.getPath() + "/establish/findWorkFlowModel",
        multiselect: true,
        colModel: [{
            name: 'modelKey',
            label: '流程key',
            formatter: function (value, options, row) {
                return '<a href="javascript:void(0)" class="edit-activiti-item" data-id="' + row.modelId + '">' + value + '</a>';
            }
        }, {
            name: 'modelName',
            label: '流程名称'
        }, {
            name: 'modelCreateTime',
            label: '流程创建时间'
        }, {
            name: 'modelLastUpdateTime',
            label: '流程最后修改时间'
        }, {
            label: "操作",
            field: "modelId",
            title: "流程设计",
            width: 120,
            fixed: true,
            formatter: function (value, options, row) {
                var actions = [];
                actions.push('<a href="javascript:void(0)" class="actions edit-item" data-id="' + row.modelId + '" title="编辑" ><i class="fa fa-pencil"></i></a>');
                actions.push('<a href="javascript:void(0)" class="actions delete-item" data-id="' + row.modelId + '" title="删除"><i class="fa fa-trash-o"></i></a>');
                actions.push('<a href="javascript:void(0)" class="actions deploy-item" data-id="' + row.modelId + '" title="部署"><i class="fa fa-cloud-upload"></i></a>');
                actions.push('<a href="javascript:void(0)" class="actions export-item" data-id="' + row.modelId + '" title="导出模板"><i class="fa fa-download"></i></a>');
                return actions.join("");
            }
        }]
    }).enableAutoHeight(".content", ".other-items");

    // 编辑
    $("#table").on("click", ".edit-item", function () {
        var id = $(this).attr("data-id");
        $.post(Util.getPath() + "/establish/findWorkFlowModelById", {
            id: id
        }, function (res) {
            if (res.code === 200) {
                var categoryForm = $("#model-form");
                var data = res.data;
                categoryForm.find('input[name="modelId"]').val(data.modelId);
                categoryForm.find('input[name="modelKey"]').val(data.modelKey);
                categoryForm.find('input[name="modelName"]').val(data.modelName);
                showModelFormLayer();
            } else {
                Util.msgError('获取信息失败！');
            }
        });
    });

    // 删除
    $("#table").on("click", ".delete-item", function () {
        deletes($(this).attr("data-id"));
    }).on("click", ".edit-activiti-item", function () {
        var width = top.$(top.window).width();
        var height = top.$(top.window).height();

        var index = top.layer.open({
            type: 2,
            area: [width + 'px', height + 'px'],
            title: '在线编辑流程',
            scrollbar: false,
            move: false,
            resize: false,
            content: Util.getRootPath() + '/static/editor/modeler.html?modelId=' + $(this).attr("data-id"),
            success: function (layero, index) {
                $(window).resize(fuc);
            },
            end: function () {
                $(window).unbind("resize", fuc);
            }
        });
        var fuc = function () {
            top.layer.full(index);
        };
    }).on("click", ".deploy-item", function () {
        var modelId = $(this).attr("data-id");
        layer.confirm('确定需要部署已选择的流程？', {
            icon: 3
        }, function (index) {
            layer.close(index);
            Util.postLoading(Util.getPath() + "/establish/deployWorkFlowModel", {modelId: modelId}, function (res) {
                if (res.code === 200) {
                    Util.msgOk('部署成功！');
                } else {
                    Util.msgError(res.errorMsg ? res.errorMsg : '部署失败！');
                }
            });
        });
    }).on("click", ".export-item", function () {
        window.open(Util.getPath() + "/establish/exportModel?ids=" + $(this).attr("data-id"), "_blank");
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

    //新增
    $("#btn-add").click(function () {
        showModelFormLayer();
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
            Util.postLoading(Util.getPath() + "/establish/deleteWorkFlowModel", {ids: ids}, function (res) {
                if (res.code === 200) {
                    Util.msgOk("删除成功！");
                    gridTable.trigger("reloadGrid");
                } else {
                    Util.msgError("删除失败！");
                }
            });
        });
    }

    function showModelFormLayer() {
        var modelForm = $("#model-form");
        layer.open({
            type: 1,
            zIndex: 90,
            content: modelForm,
            title: "流程模板信息维护",
            area: ['465px', 'auto'],
            scrollbar: false,
            resize: false,
            btn: ['提交', '取消'],
            yes: function (index, layero) {
                if (!modelForm.valid()) {
                    modelForm.validate().focusInvalid();
                    return;
                }
                modelForm.ajaxSubmit({
                    url: Util.getPath() + "/establish/saveOrUpdateWorkFlowModel",
                    dataType: 'json',
                    type: 'post',
                    success: function (res) {
                        if (res.code === 200) {
                            $('#table').trigger("reloadGrid");
                            layer.close(index);
                            Util.msgOk('提交成功！');
                        } else {
                            Util.msgError(res.errorMsg === 'key重复' ? '流程Key已存在，请重新输入！' : '提交失败！');
                        }
                    }
                });
            },
            end: function () {
                modelForm[0].reset();
                modelForm.validate().resetForm();
            }
        });
    }

});

function reloadGrid() {
    $('#table').trigger("reloadGrid");
}