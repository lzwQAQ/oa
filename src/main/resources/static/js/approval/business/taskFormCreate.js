$(function () {

    var pendingFirstTask = (Util.getQueryString("firstTask") === "true") && Util.getQueryString("type") === "pending";

    loadSelectedDeptsUsers();

    $("input").iCheck({
        checkboxClass: 'icheckbox_minimal-green',
        radioClass: 'iradio_minimal-green'
    });
    $(".select2-multiple").select2({
        multiple: true,
        maximumSelectionLength: 3
    });
    $(".select2-single").select2();
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

    //------------------------------------------------------------

    var startTime, endTime;
    $('#startTime').daterangepicker({
        singleDatePicker: true,
        timePicker: false,
        timePicker24Hour: true,
        autoUpdateInput: false
    }).on('apply.daterangepicker', function (ev, picker) {
        if (endTime && endTime.diff(picker.startDate) <= 0) {
            Util.msgWarning("开始时间必须小于结束时间！");
            return;
        }
        startTime = picker.startDate;
        $(this).val(picker.startDate.format('YYYY-MM-DD'));
    }).on('cancel.daterangepicker', function (ev, picker) {
        $(this).val('');
    });

    $('#endTime').daterangepicker({
        singleDatePicker: true,
        timePicker: false,
        timePicker24Hour: true,
        autoUpdateInput: false
    }).on('apply.daterangepicker', function (ev, picker) {
        if (startTime && startTime.diff(picker.startDate) >= 0) {
            Util.msgWarning("结束时间必须大于开始时间！");
            return;
        }
        endTime = picker.startDate;
        $(this).val(picker.startDate.format('YYYY-MM-DD'));
    }).on('cancel.daterangepicker', function (ev, picker) {
        $(this).val('');
    });

    $("#btnSubmit").click(function () {
        if ($("#form").valid()) {
            parent.beforeSubmitForm($("#auditResult").val(), function (taskResult) {
                $("#form").ajaxSubmit({
                    loading: true,
                    type: "post",
                    url: Util.getPath() + "/business/submit",
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

    function loadSelectedDeptsUsers() {
        $.post(Util.getPath() + "/business/userlist", function (res) {
            if (res.code === 200) {
                var options = '<option value="{{id}}">{{name}}</option>';
                $.each(res.data, function (i, v) {
                    $("#header").append(options.replace(/{{name}}/g, v.name).replace(/{{id}}/g, v.id));
                });
            }
        }, "JSON");

        $.post(Util.getPath() + "/business/deptlist", function (res) {
            if (res.code === 200) {
                if (res.code === 200) {
                    var options = '<option value="{{id}}">{{name}}</option>';
                    $.each(res.data, function (i, v) {
                        $("#collaborationDept").append(options.replace(/{{name}}/g, v.name).replace(/{{id}}/g, v.id));
                    });
                }
            }
        }, "JSON");
    }
});