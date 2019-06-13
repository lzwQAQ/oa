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

    $("#btnSubmit").click(function () {
        if ($("#form").valid()) {
            $("#form").ajaxSubmit({
                loading: true,
                type: "post",
                url: Util.getPath() + "/accountsrecord/save",
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