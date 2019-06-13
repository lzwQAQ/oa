$(function () {

    $("#form select").select2();

    $("input").iCheck({
        checkboxClass: 'icheckbox_minimal-green',
        radioClass: 'iradio_minimal-green'
    });

    $('.datetime').daterangepicker({
        singleDatePicker: true,
        showDropdowns: true,
        autoUpdateInput: false
    }).on('apply.daterangepicker', function (ev, picker) {
        $(this).val(picker.startDate.format('YYYY-MM-DD'));
    });

    $("#form").validate();

    $("#btnSubmit").click(function () {
        if ($("#form").valid()) {
            //将邮箱名称和后缀结合起来，形成最终的邮箱地址
            $("#form").ajaxSubmit({
                loading: true,
                type: "post",
                url: Util.getPath() + "/user/personalinfo/save",
                success: function (res, statusText, xhr, $form) {
                    if (res.code === 200) {
                        Util.msgOk("操作成功！");
                        top.closeCurrentTab();
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
        top.closeCurrentTab();
    });
});