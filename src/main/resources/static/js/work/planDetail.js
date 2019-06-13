$(function () {

    $("#btnCancel").click(function () {
        parent.layer.close(parent.layer.getFrameIndex(window.name));
    });

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

    $.post(Util.getPath() + "/plan/chargepeoples", {
        planId: $("#id").val()
    }, function (res) {
        if (res.code === 200) {
            $.each(res.data, function (i, v) {
                $("#chargePeoples").find('option[value="' + v + '"]').attr("selected", true);
            });
            $("#chargePeoples").trigger("change");
        }
    }, "json");

});