$(function () {

    $("#form select").select2({
        maximumSelectionLength: 2
    });

    $("input").iCheck({
        checkboxClass: 'icheckbox_minimal-green',
        radioClass: 'iradio_minimal-green'
    });

    //----------------------------
    $('input[name="datetime"]').daterangepicker({
        singleDatePicker: true,
        timePicker: true,
        timePicker24Hour: true,
        autoUpdateInput: false,
        locale: {
            cancelLabel: '清除',
            applyLabel: '确定'
        }
    }).on('apply.daterangepicker', function (ev, picker) {
        $(this).val(picker.startDate.format('YYYY-MM-DD HH:mm:ss'));
    }).on('cancel.daterangepicker', function (ev, picker) {
        $(this).val('');
    });

    $('input[name="datetimerange"]').daterangepicker({
        autoUpdateInput: false,
        timePicker: true,
        timePicker24Hour: true,
        locale: {
            cancelLabel: '清除',
            applyLabel: '确定'
        }
    }).on('apply.daterangepicker', function (ev, picker) {
        $(this).val(picker.startDate.format('YYYY-MM-DD HH:mm:ss') + ' ~ ' + picker.endDate.format('YYYY-MM-DD HH:mm:ss'));
    }).on('cancel.daterangepicker', function (ev, picker) {
        $(this).val('');
    });
    //----------------------------


    $("#form").validate();

    $("#btnSubmit").click(function () {
        if ($("#form").valid()) {
            $("#form").ajaxSubmit({
                loading: true,
                type: "post",
                url: Util.getPath() + "/org/save",
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

    $("#parentDiv").click(function () {
        Util.openIframe(Util.getPath() + "/org/panel", function (index, layero) {
            var contentWindow = layero.find('iframe')[0].contentWindow;
            var selectItem = contentWindow.getSelected();
            if (selectItem) {
                if ($("#id").val() === selectItem.id) {
                    Util.msgWarning("不能选择自己成为父类！");
                    return;
                }
                $("#parentId").val(selectItem.id);
                $("#parentName").val(selectItem.name);
            } else {
                $("#parentId").val("");
                $("#parentName").val("");
            }
            Util.closeLayer(index);
        });
    });

});