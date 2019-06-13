$(function () {

    //所有的输入框设置为不可用
    $("select,input,textarea").attr("disabled", "disabled");
    $("input").iCheck({
        checkboxClass: 'icheckbox_minimal-green',
        radioClass: 'iradio_minimal-green'
    });
});