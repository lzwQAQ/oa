$(function () {
    $("input").iCheck({
        checkboxClass: 'icheckbox_minimal-green',
        radioClass: 'iradio_minimal-green'
    });

    $("#form").validate();

    $("#btnSubmit").click(function () {
        if ($("#form").valid()) {
            $("#form").ajaxSubmit({
                loading: true,
                type: "post",
                url: Util.getPath() + "/role/save",
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

    $("#orgDiv").click(function () {
        Util.openIframe(Util.getPath() + "/org/panel", function (index, layero) {
            var contentWindow = layero.find('iframe')[0].contentWindow;
            var selectItem = contentWindow.getSelected();
            if (selectItem) {
                $("#orgId").val(selectItem.id);
                $("#orgName").val(selectItem.name);
            } else {
                $("#orgId").val("");
                $("#orgName").val("");
            }
            Util.closeLayer(index);
        });
    });

});