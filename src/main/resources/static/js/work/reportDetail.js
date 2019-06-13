$(function () {
    $("#btnCancel").click(function () {
        parent.layer.close(parent.layer.getFrameIndex(window.name));
    });

    $.post(Util.getPath() + "/report/planchargepeoples", {
        planId: $("#planId").val()
    }, function (res) {
        if (res.code === 200) {
            $("#planChargePeoples").val(res.data.join(","));
        }
    }, "json");
});