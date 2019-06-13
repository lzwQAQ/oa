$(function () {

    loadAuditResult();
    loadSelectedDeptsUsers();

    $("#form").validate();
    $("input").iCheck({
        checkboxClass: 'icheckbox_minimal-green',
        radioClass: 'iradio_minimal-green'
    }).iCheck('disable');
    $(".select2-multiple").select2({
        multiple: true,
        maximumSelectionLength: 3
    });
    $(".select2-single").select2();
    $(".only-view").attr("disabled", "disabled");
    $("#approvalResult-div,#btns-div").show();

    $("#btnSubmit").click(function () {
        if ($("#form").valid()) {
            Util.ask("确认提交审批？", function (index, layer) {
                Util.closeLayer(index);
                parent.beforeSubmitForm($("#auditResult").val(), function (taskResult) {
                    $("#form").ajaxSubmit({
                        loading: true,
                        type: "post",
                        url: Util.getPath() + "/business/approval",
                        data: {
                            taskResult: taskResult,
                            modelKey: Util.getQueryString("type")
                        },
                        success: function (res, statusText, xhr, $form) {
                            if (res.code === 200) {
                                Util.msgOk("提交成功！");
                                parent.parent.layer.close(parent.parent.layer.getFrameIndex(parent.window.name));
                                top.simpleWorkFlow_ReloadGrid();
                                delete top.window.simpleWorkFlow_ReloadGrid;
                            } else {
                                Util.msgError("提交失败！");
                            }
                        }
                    });
                });
            });
        } else {
            $('#form').validate().focusInvalid();
        }
    });

    $("#btnCancel").click(function () {
        parent.parent.layer.close(parent.parent.layer.getFrameIndex(parent.window.name));
    });

    /**
     * 加载审批选项
     */
    function loadAuditResult() {
        $("#auditResult-div").show();
        $.post(Util.getPath() + "/pendingtask/findNextSequenceFlowNames", {
            taskId: Util.getQueryString("taskId"),
            modelKey: Util.getQueryString("modelKey")
        }, function (res) {
            if (res.code === 200) {
                $("#auditResult").append("<option value=''>请选择处理结果</option>")
                $.each(res.data, function (i, v) {
                    $("#auditResult").append('<option value="' + v + '" >' + v + '</option>');
                });
                $("#audit-result-div").show();
            }
        });
    }

    function loadSelectedDeptsUsers() {
        $.post(Util.getPath() + "/business/userlist", function (res) {
            if (res.code === 200) {
                var options = '<option value="{{id}}">{{name}}</option>';
                $.each(res.data, function (i, v) {
                    $("#header").append(options.replace(/{{name}}/g, v.name).replace(/{{id}}/g, v.id));
                });
                if ($("#headers").val()) {
                    $("#header").val($("#headers").val().split(",")).trigger('change');
                }
            }
        }, "JSON");

        $.post(Util.getPath() + "/business/deptlist", function (res) {
            if (res.code === 200) {
                if (res.code === 200) {
                    var options = '<option value="{{id}}">{{name}}</option>';
                    $.each(res.data, function (i, v) {
                        $("#collaborationDept").append(options.replace(/{{name}}/g, v.name).replace(/{{id}}/g, v.id));
                    });
                    if ($("#collaborationDepts").val()) {
                        $("#collaborationDept").val($("#collaborationDepts").val().split(",")).trigger('change');
                    }
                }
            }
        }, "JSON");
    }
});