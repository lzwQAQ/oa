$(function () {
    $("input").iCheck({
        checkboxClass: 'icheckbox_minimal-green',
        radioClass: 'iradio_minimal-green'
    }).iCheck('disable');
    $(".select2-multiple").select2({
        multiple: true,
        maximumSelectionLength: 3
    });

    loadSelectedDeptsUsers();

    //所有的输入框设置为不可用
    $("select,input,textarea").attr("disabled", "disabled");

    function loadSelectedDeptsUsers() {
        $.post(Util.getPath() + "/business/userlist", function (res) {
            if (res.code === 200) {
                var options = '<option value="{{id}}">{{name}}</option>';
                $.each(res.data, function (i, v) {
                    $("#header").append(options.replace(/{{name}}/g, v.name).replace(/{{id}}/g, v.id));
                });
                if ($("#headers").val()) {
                    $("#header").val($("#headers").val().split(",")).trigger('change');
                }
            }
        }, "JSON");

        $.post(Util.getPath() + "/business/deptlist", function (res) {
            if (res.code === 200) {
                if (res.code === 200) {
                    var options = '<option value="{{id}}">{{name}}</option>';
                    $.each(res.data, function (i, v) {
                        $("#collaborationDept").append(options.replace(/{{name}}/g, v.name).replace(/{{id}}/g, v.id));
                    });
                    if ($("#collaborationDepts").val()) {
                        $("#collaborationDept").val($("#collaborationDepts").val().split(",")).trigger('change');
                    }
                }
            }
        }, "JSON");
    }
});