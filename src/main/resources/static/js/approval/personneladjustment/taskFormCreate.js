$(function () {

    var pendingFirstTask = (Util.getQueryString("firstTask") === "true") && Util.getQueryString("type") === "pending";

    $("input").iCheck({
        checkboxClass: 'icheckbox_minimal-green',
        radioClass: 'iradio_minimal-green'
    });
    $("#form").validate();
    $("#form select").select2();
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

    //------------------------------------------------------------

    $('.single-date').daterangepicker({
        singleDatePicker: true,
        showDropdowns: true,
        autoUpdateInput: false
    }).on('apply.daterangepicker', function (ev, picker) {
        $(this).val(picker.startDate.format('YYYY-MM-DD'));
    });


    $("#btnSubmit").click(function () {
        if ($("#form").valid()) {
            parent.beforeSubmitForm($("#auditResult").val(), function (taskResult) {
                $("#form").ajaxSubmit({
                    loading: true,
                    type: "post",
                    url: Util.getPath() + "/personneladjustment/submit",
                    data: {
                        taskResult: taskResult
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

    $.post(Util.getPath() + "/dept/depttreelist", function (res) {
        if (res.code === 200) {
            var ztreeDown = $("#newDeptName").ztreeDown(res.data, {
                callback: {
                    onClick: function (event, treeId, treeNode) {
                        if (treeNode.type === 'org') {
                            return;
                        }
                        $("#newDeptId").val(treeNode.id);
                        $("#newDeptName").val(treeNode.name);
                        ztreeDown.hideMenu();
                    }
                }
            });

            $("#newDeptName").click(function () {
                ztreeDown.zTree.expandAll(true);
                ztreeDown.showMenu();
            });
        }
    }, "JSON");

});