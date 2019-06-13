$(function () {

    loadAuditResult();

    $("#form").validate();

    $(".select2").select2();

    $(".only-view").attr("disabled", "disabled");
    $("#approvalResult-div,#btns-div").show();

    $(".file-view").click(function () {
        var url = officeUrl + officeAddress + $(this).attr("filepath");
        top.window.open(encodeURI(url), "_blank");
    });

    $("#btnSubmit").click(function () {
        if ($("#form").valid()) {
            Util.ask("确认提交审批？", function (index, layer) {
                Util.closeLayer(index);
                parent.beforeSubmitForm($("#auditResult").val(), function (taskResult) {
                    $("#form").ajaxSubmit({
                        loading: true,
                        type: "post",
                        url: Util.getPath() + "/releasedocument/approval",
                        data: {
                            taskResult: taskResult,
                            modelKey: Util.getQueryString("type"),
                            taskName: Util.getQueryString("taskName")
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

    if (Util.getQueryString("taskName") === '发布') {
        $("#sendTo-div").show();
        $.post(Util.getPath() + "/train/usertree", function (res) {
            if (res.code === 200) {
                $.each(res.data, function (i, v) {
                    if (v.type === "user") {
                        v.icon = Util.getRootPath() + "/static/images/user.png";
                    }
                });
                var ztreeDown = $("#sendToName").ztreeDown(res.data, {
                    clearInput: false,
                    check: {
                        enable: true
                    },
                    callback: {
                        onCheck: function (event, treeId, treeNode) {
                            var treeObj = $.fn.zTree.getZTreeObj(treeId);
                            var nodes = treeObj.getCheckedNodes(true);
                            var selectUsers = [];
                            $.each(nodes, function (i, v) {
                                if (v.type === 'user') {
                                    selectUsers.push(v.id);
                                }
                            });
                            $("#sendToName").val("已选择 " + selectUsers.length + " 人");
                            $("#sendTo").val(selectUsers.join(","));
                        }
                    }
                });

                ztreeDown.zTree.expandAll(true);
                $("#sendToName").click(function () {
                    ztreeDown.showMenu();
                });
            }
        }, "JSON");
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
                $("#auditResult").append("<option value=''>请选择处理结果</option>");
                $.each(res.data, function (i, v) {
                    $("#auditResult").append('<option value="' + v + '" >' + v + '</option>');
                });
                $("#audit-result-div").show();
            }
        });
    }

});