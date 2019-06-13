$(function () {

    $("#form .select2").select2();

    $("#form").validate();

    $("#user_id_select").change(function () {
        var $this = $(this);
        $.each(users, function (i, v) {
            if ($this.val() === v.id) {
                $("#sex").val(v.sex === "1" ? "男" : "女");
                $("#phone").val(v.phone);
            }
        });
    });

    var users;
    $.post(Util.getPath() + "/driver/findusers", function (res) {
        if (res.code === 200) {
            users = res.data;
            $.each(res.data, function (i, v) {
                $("#user_id_select").append('<option value="' + v.id + '">' + v.name + '</option>');
            });
            $("#user_id_select").find('option[value="' + $("#user_id").val() + '"]').attr("selected", true).trigger("change");
        }
    });

    $("#btnSubmit").click(function () {
        if ($("#form").valid()) {
            $("#form").ajaxSubmit({
                loading: true,
                type: "post",
                url: Util.getPath() + "/driver/save",
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