$(function () {

    var pendingFirstTask = (Util.getQueryString("firstTask") === "true") && Util.getQueryString("type") === "pending";
    if (pendingFirstTask) {
        $("#title").attr("disabled", "disabled");
    }

    loadAuditResult();

    $("#form").validate();
    $("#btns-div").show();
    $("#approvalResult").attr("disabled", "disabled");

    $("#btnCancel").click(function () {
        pendingFirstTask ? parent.parent.layer.close(parent.parent.layer.getFrameIndex(parent.window.name)) : top.closeCurrentTab();
    });

    function closeTask() {
        if (pendingFirstTask) {
            parent.parent.layer.close(parent.parent.layer.getFrameIndex(parent.window.name));
            top.simpleWorkFlow_ReloadGrid();
            delete top.window.simpleWorkFlow_ReloadGrid;
        } else {
            top.closeCurrentTab();
        }
    }

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

    // --------------------------------------------------

    $("#btnSubmit").click(function () {
        if ($("#form").valid()) {
            parent.beforeSubmitForm($("#auditResult").val(), function (taskResult) {
                $("#form").ajaxSubmit({
                    loading: true,
                    type: "post",
                    url: Util.getPath() + "/simpleworkflow/sbumit",
                    data: {
                        taskResult: taskResult,
                        modelKey: Util.getQueryString("modelKey")
                    },
                    success: function (res, statusText, xhr, $form) {
                        if (res.code === 200) {
                            Util.msgOk("提交成功！");
                            closeTask();
                        } else {
                            Util.msgError("提交失败！");
                        }
                    }
                });
            });
        } else {
            $('#form').validate().focusInvalid();
        }
    });
});