$(function () {

    $(".users-show").click(function () {
        $("#users").slideDown();
        $(".users-show").hide();
        $(".users-hide").show();
    });

    $(".users-hide").click(function () {
        $("#users").slideUp();
        $(".users-hide").hide();
        $(".users-show").show();
    });

    $("#btnCancel").click(function () {
        parent.layer.close(parent.layer.getFrameIndex(window.name));
    });

});