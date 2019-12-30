$(function () {

    //所有的输入框设置为不可用
    $("select,input,textarea").attr("disabled", "disabled");

    // ---------------------------------
    $.post(Util.getPath() + "/carapply/finddrivers", function (res) {
        if (res.code === 200) {
            $.each(res.data, function (i, v) {
                $("#driver_select").append('<option value="' + v.id + '">' + v.name + '</option>');
            });
            $("#driver_select").val($("#driverId").val()).attr("selected", true).trigger("change");
        }
    });
    $.post(Util.getPath() + "/carapply/findcars", function (res) {
        if (res.code === 200) {
            $.each(res.data, function (i, v) {
                $("#car_select").append('<option value="' + v.id + '">' + v.carNo + '</option>')
            });
            $("#car_select").find('option[value="' + $("#carId").val() + '"]').attr("selected", true).trigger("change");
        }
    });
});