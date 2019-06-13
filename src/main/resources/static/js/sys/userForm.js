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
            $("#email").val($("#email_name").val() + $("#email_suffix").text());
            $("#form").ajaxSubmit({
                loading: true,
                type: "post",
                url: Util.getPath() + "/user/save",
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

    $("#dept-div").click(function () {
        Util.openIframe(Util.getPath() + "/dept/panel", function (index, layero) {
            var contentWindow = layero.find('iframe')[0].contentWindow;
            var selectItem = contentWindow.getSelected();
            $("#deptId").val(selectItem ? selectItem.id : "");
            $("#deptName").val(selectItem ? selectItem.name : "");
            Util.closeLayer(index);
        });
    });
});