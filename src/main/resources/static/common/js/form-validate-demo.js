//以下为修改jQuery Validation插件兼容Bootstrap的方法，没有直接写在插件中是为了便于插件升级
$.validator.setDefaults({
    highlight: function(element) {
        $(element).closest('.form-group').removeClass('has-success').addClass('has-error');
    },
    success: function(element) {
        element.closest('.form-group').removeClass('has-error').addClass('has-success');
    },
    errorElement: "span",
    errorPlacement: function(error, element) {
        if (element.is(":radio") || element.is(":checkbox")) {
            error.appendTo(element.parent().parent().parent());
        } else {
            error.appendTo(element.parent());
        }
    },
    errorClass: "help-block m-b-none",
    validClass: "help-block m-b-none"


});

//以下为官方示例
$(function() {

    //icheck在做表单验证的时候不能实时进行表单校验，这里在选择事件发生的时候进行表单验证
    $('#agree-icheck').on('ifChecked ifUnchecked', function(event){
        $("#signupForm").validate().element("#agree");
    });

    // validate signup form on keyup and submit
    var icon = "<i class='fa fa-times-circle'></i> ";
    var a=$("#signupForm").validate({
        rules: {
            username: {
                required: true,
                minlength: 2
            },
            password: {
                required: true,
                minlength: 5
            },
            confirm_password: {
                required: true,
                equalTo: "#password"
            }, 
            email: {
                required: true,
                email: true
            },
            topic: {
                required: "#newsletter:checked",
                minlength: 2
            },
            agree: "required"
        },
        messages: {
            username: {
                required: icon + "请输入您的用户名",
                minlength: icon + "用户名必须两个字符以上"
            },
            password: {
                required: icon + "请输入您的密码",
                minlength: icon + "密码必须5个字符以上"
            },
            confirm_password: {
                required: icon + "请再次输入密码",
                equalTo: icon + "两次输入的密码不一致"
            },
            email: icon + "请输入您的E-mail",
            agree: {
                required: icon + "必须同意协议后才能注册",
                element: '#agree-error'
            }
        }
    });
    console.log(a.valid());
});