$(function () {

    $("#btnCancel").click(function () {
        parent.layer.close(parent.layer.getFrameIndex(window.name));
    });

    if (Util.getQueryString("view")) {
        $("#form input").attr("readonly", "readonly");
        $("#form select").attr("disabled", "disabled");
        return;
    }

    $("#form").validate();
    $("#form select").select2();

    var startTime, endTime;
    $('#startTime').daterangepicker({
        singleDatePicker: true,
        timePicker: true,
        timePicker24Hour: true,
        autoUpdateInput: false,
        locale: {
            cancelLabel: '清除',
            applyLabel: '确定'
        }
    }).on('apply.daterangepicker', function (ev, picker) {
        if (endTime && endTime.diff(picker.startDate) <= 0) {
            Util.msgWarning("开始时间必须小于结束时间！");
            return;
        }
        startTime = picker.startDate;
        $(this).val(picker.startDate.format('YYYY-MM-DD HH:mm'));
        calculate(startTime, endTime);
    }).on('cancel.daterangepicker', function (ev, picker) {
        $(this).val('');
    });

    $('#endTime').daterangepicker({
        singleDatePicker: true,
        timePicker: true,
        timePicker24Hour: true,
        autoUpdateInput: false,
        locale: {
            cancelLabel: '清除',
            applyLabel: '确定'
        }
    }).on('apply.daterangepicker', function (ev, picker) {
        if (startTime && startTime.diff(picker.startDate) >= 0) {
            Util.msgWarning("结束时间必须大于开始时间！");
            return;
        }
        endTime = picker.startDate;
        $(this).val(picker.startDate.format('YYYY-MM-DD HH:mm'));
        calculate(startTime, endTime);
    }).on('cancel.daterangepicker', function (ev, picker) {
        $(this).val('');
    });

    function calculate(startTime, endTime) {
        if (startTime && endTime) {
            var l = new Date(endTime.format('YYYY/MM/DD')).getTime() - new Date(startTime.format('YYYY/MM/DD')).getTime();
            var day = parseInt(l / (1000 * 60 * 60 * 24));
            var startDate = new Date(startTime.format('YYYY/MM/DD HH:mm'));
            var endDate = new Date(endTime.format('YYYY/MM/DD HH:mm'));

            var leaveDay = 0;
            if (day === 0) {
                if (startDate.getHours() < 12 && endDate.getHours() > 12) {
                    leaveDay = 1;
                } else {
                    leaveDay = 0.5;
                }
            } else {
                leaveDay += startDate.getHours() > 12 ? 0.5 : 1;
                leaveDay += endDate.getHours() > 12 ? 1 : 0.5;
            }

            day -= 1;
            day = day <= 0 ? 0 : day;
            leaveDay += day;

            $("#leaveDay").val(leaveDay);
        }
    }

    $("#btnSubmit").click(function () {
        if ($("#form").valid()) {
            $("#form").ajaxSubmit({
                loading: true,
                type: "post",
                url: Util.getPath() + "/leaverecord/save",
                success: function (res, statusText, xhr, $form) {
                    if (res.code === 200) {
                        Util.msgOk("操作成功！");
                        parent.layer.close(parent.layer.getFrameIndex(window.name));
                        parent.reloadGrid();
                    } else {
                        Util.msgError("操作失败！");
                    }
                }
            });
        } else {
            $('#form').validate().focusInvalid();
        }
    });

    $("#senderId").change(function () {
        var deptname = $(this).find("option:selected").attr("deptname");
        $("#deptname").val(deptname);
    });

});