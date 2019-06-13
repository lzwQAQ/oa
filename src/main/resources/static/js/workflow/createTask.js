$(function () {

    var taskResult = {
        startSequenceFlowName: '',
        sequenceFlowName: '',
        processDefinitionId: '',
        users: []
    };

    $.post(Util.getPath() + "/createtask/findFormPath", {modelKey: Util.getQueryString("modelKey")}, function (res) {
        if (res.code === 200) {
            taskResult.processDefinitionId = res.data.processDefinitionId;
            $("#formContent").attr("src", Util.getPath() + "/" + res.data.formPath);
            $("#activiti-image").attr("src", Util.getPath() + "/released/imageResource?processDefinitionId=" + taskResult.processDefinitionId + "&stime=" + new Date().getTime());
        }
    });

    $("#title li").click(function () {
        if ($(this).hasClass("active")) {
            return;
        }
        $("#title li").toggleClass("active");
        $("#content .tab-pane").toggleClass("active");
    });

    function beforeSubmitForm(sequenceFlowName, submitTask) {
        //初始化对象属性
        taskResult.sequenceFlowName = sequenceFlowName;
        taskResult.users = [];
        taskResult.taskId = '';

        $.post(Util.getPath() + "/createtask/isSelectNextTaskCandidateInfos", {
            sequenceFlowName: taskResult.sequenceFlowName,
            processDefinitionId: taskResult.processDefinitionId
        }, function (res) {
            if (res.code === 200) {
                var value = res.data;
                if (value === "true") {
                    if (Util.getQueryString("selectuser") === "false") {
                        submitTask(taskResult);
                    } else {
                        layer.confirm('是否需要指定下一步处理人？', {
                            icon: 3,
                            title: '提示',
                            btn: ['指定', '不指定', '取消'],
                            closeBtn: false,
                            resize: false,
                            yes: function (index, layero) {
                                layer.close(index);
                                Util.openIframe(Util.getPath() + "/pendingtask/showCreateCandidateInfoWindow?processDefinitionId=" + taskResult.processDefinitionId + "&sequenceFlowName=" + encodeURI(taskResult.sequenceFlowName), function (index, layero) {
                                    var contentWindow = layero.find('iframe')[0].contentWindow;
                                    var selects = contentWindow.getSelected();
                                    if (selects && selects.length > 0) {
                                        taskResult.users = selects;
                                        Util.closeLayer(index);
                                        submitTask(JSON.stringify(taskResult), index);
                                    } else {
                                        Util.msgError("请选择审批人员！");
                                    }
                                });
                            },
                            btn2: function (index, layero) {
                                submitTask(JSON.stringify(taskResult));
                            }
                        });
                    }
                } else if (value === "false") {
                    submitTask(JSON.stringify(taskResult));
                } else if (value === "sequenceFlowName error") {
                    Util.msgError('审批结果有误！');
                } else if (value === "no person error") {
                    Util.msgError('下一环节没有处理人，请配置后重新提交！');
                }
            } else {
                Util.msgError('获取环节信息错误！');
            }
        });
    }

    window.beforeSubmitForm = beforeSubmitForm;

});