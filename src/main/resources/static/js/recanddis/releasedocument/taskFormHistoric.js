$(function () {

    //所有的输入框设置为不可用
    $("select,input,textarea").attr("disabled", "disabled");

    $(".file-view").click(function () {
        var url = officeUrl + officeAddress + $(this).attr("filepath");
        top.window.open(encodeURI(url), "_blank");
    });

});