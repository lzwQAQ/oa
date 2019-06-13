$(function () {
    $('.datetime').daterangepicker({
        singleDatePicker: true,
        timePicker: false,
        timePicker24Hour: true,
        autoUpdateInput: false,
        locale: {
            cancelLabel: '清除',
            applyLabel: '确定'
        }
    }).on('apply.daterangepicker', function (ev, picker) {
        $(this).val(picker.startDate.format('YYYY-MM-DD'));
    }).on('cancel.daterangepicker', function (ev, picker) {
        $(this).val('');
    });

    $("#form").validate();

    $("#btnSubmit").click(function () {
        if ($("#form").valid()) {
            $("#form").ajaxSubmit({
                loading: true,
                type: "post",
                url: Util.getPath() + "/report/save",
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

    $("#btnCancel").click(function () {
        parent.layer.close(parent.layer.getFrameIndex(window.name));
    });

    function initPlanInfo(planId) {
        $.post(Util.getPath() + "/report/planchargepeoples", {
            planId: planId
        }, function (res) {
            if (res.code === 200) {
                $("#planChargePeoples").val(res.data.join(","));
            }
        }, "json");
    }

    if ($("#planId").val()) {
        initPlanInfo($("#planId").val());
    }

    $("#planId").change(function () {
        if ($(this).val()) {
            initPlanInfo($(this).val());
        }
    });
});