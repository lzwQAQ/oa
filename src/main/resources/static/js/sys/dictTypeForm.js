$(function () {

    $("#form").validate();
    $("input").iCheck({
        checkboxClass: 'icheckbox_minimal-green',
        radioClass: 'iradio_minimal-green'
    });

    $("#btnSubmit").click(function () {
        if ($("#form").valid()) {
            $("#form").ajaxSubmit({
                loading: true,
                type: "post",
                url: Util.getPath() + "/dicttype/save",
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


});