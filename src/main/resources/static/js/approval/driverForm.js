$(function () {

    var throttle = {
        isCheckPhone: true,
        isCheckName: true,
        isExistPhone: true
    };

    var oldValue = {
        phone: '',
        name: ''
    };

    $("#form .select2").select2();

    $("input").iCheck({
        checkboxClass: 'icheckbox_minimal-green',
        radioClass: 'iradio_minimal-green'
    });

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
                        Util.msgError(res.errorMsg);
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

    $("#name").focus(function (e) {
        var $this = $(e.currentTarget);
        $this.attr("orgin", $this.attr("placeholder"));
    });

    $("#name").blur(function (e) {
        var $this = $(e.currentTarget);
        var id = Util.strTrim($("#id").val());
        var name = Util.strTrim($this.val());
        $this.attr("placeholder", $this.attr("orgin"));
        if (oldValue.name != name) {
            throttle.isCheckName = true;
            oldValue.name = name;
        } else {
            throttle.isCheckName = false;
            oldValue.name = name;
        }
        if (throttle.isCheckName){
            $.post(Util.getPath() + "/driver/check/1/value", {
                id: id,
                data: name
            }, function (res) {
                if (res.code != 200) {
                    Util.msgError('驾驶员姓名已存在，请重新输入！');
                }
            });
        }
    });

    $("#phone").focus(function (e) {
        var $this = $(e.currentTarget);
        $this.attr("orgin", $this.attr("placeholder"));
    });

    $("#phone").blur(function (e) {
        var $this = $(e.currentTarget);
        var id = Util.strTrim($("#id").val());
        var phone = Util.strTrim($this.val());
        $this.attr("placeholder", $this.attr("orgin"));
        if (oldValue.phone != phone) {
            throttle.isCheckPhone = true;
            oldValue.phone = phone;
        } else {
            throttle.isCheckPhone = false;
            oldValue.phone = phone;
        }
        if (throttle.isCheckPhone){
            $.post(Util.getPath() + "/driver/check/2/value", {
                id: id,
                data: phone
            }, function (res) {
                if (res.code != 200) {
                    Util.msgError('手机号码已存在，请重新输入！');
                }
            });
        }
    });


});