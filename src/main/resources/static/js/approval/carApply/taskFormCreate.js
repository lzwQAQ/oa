$(function () {

    var pendingFirstTask = (Util.getQueryString("firstTask") === "true") && Util.getQueryString("type") === "pending";

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
    }).on('cancel.daterangepicker', function (ev, picker) {
        $(this).val('');
    });

    $("#btnSubmit").click(function () {
        if ($("#form").valid()) {
            parent.beforeSubmitForm($("#auditResult").val(), function (taskResult) {
                $("#form").ajaxSubmit({
                    loading: true,
                    type: "post",
                    url: Util.getPath() + "/carapply/submit",
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

    var drivers, cars;
    $("#driver_select").change(function () {
        var $this = $(this);
        $("#phone").val("");
        $.each(drivers, function (i, v) {
            if ($this.val() === v.user.id) {
                $("#phone").val(v.user.phone);
            }
        });
    });
    $("#car_select").change(function () {
        var $this = $(this);
        $("#carNo").val("");
        $("#seatNum").val("");
        $.each(cars, function (i, v) {
            if ($this.val() === v.id) {
                $("#carNo").val(v.brand);
                $("#seatNum").val(v.seatNum);
            }
        });
    });
    $.post(Util.getPath() + "/carapply/finddrivers", function (res) {
        if (res.code === 200) {
            $.each(drivers = res.data, function (i, v) {
                $("#driver_select").append('<option value="' + v.user.id + '">' + v.user.name + '</option>');
            });
            $("#driver_select").find('option[value="' + $("#driverId").val() + '"]').attr("selected", true).trigger("change");
        }
    });
    $.post(Util.getPath() + "/carapply/findcars", function (res) {
        if (res.code === 200) {
            $.each(cars = res.data, function (i, v) {
                $("#car_select").append('<option value="' + v.id + '">' + v.carNo + '</option>')
            });
            $("#car_select").find('option[value="' + $("#carId").val() + '"]').attr("selected", true).trigger("change");
        }
    });

});