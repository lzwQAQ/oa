$(function () {

    $(".select2").select2();

    $("input").iCheck({
        checkboxClass: 'icheckbox_minimal-green',
        radioClass: 'iradio_minimal-green'
    });

    var startTime, endTime;
    $('#beginTime').daterangepicker({
        singleDatePicker: true,
        timePicker: true,
        timePicker24Hour: true,
        autoUpdateInput: false,
        locale: {
            cancelLabel: '清除',
            applyLabel: '确定'
        }
    }).on('apply.daterangepicker', function (ev, picker) {
        if (endTime && endTime.diff(picker.startDate) <= 0) {
            Util.msgWarning("开始时间必须小于结束时间！");
            return;
        }
        startTime = picker.startDate;
        $(this).val(picker.startDate.format('YYYY-MM-DD HH:mm:ss'));
        calculate(startTime, endTime);
    }).on('cancel.daterangepicker', function (ev, picker) {
        $(this).val('');
    });

    $('#endTime').daterangepicker({
        singleDatePicker: true,
        timePicker: true,
        timePicker24Hour: true,
        autoUpdateInput: false,
        locale: {
            cancelLabel: '清除',
            applyLabel: '确定'
        }
    }).on('apply.daterangepicker', function (ev, picker) {
        if (startTime && startTime.diff(picker.startDate) >= 0) {
            Util.msgWarning("结束时间必须大于开始时间！");
            return;
        }
        endTime = picker.startDate;
        $(this).val(picker.startDate.format('YYYY-MM-DD HH:mm:ss'));
        calculate(startTime, endTime);
    }).on('cancel.daterangepicker', function (ev, picker) {
        $(this).val('');
    });

    function calculate(startTime, endTime) {
        if (startTime && endTime) {
            var l = new Date(endTime.format('YYYY/MM/DD HH:mm:ss')).getTime() - new Date(startTime.format('YYYY/MM/DD HH:mm:ss')).getTime();
            var day = parseInt(l / (1000 * 60 * 60 * 24));
            var hour = parseInt((l - day * 1000 * 60 * 60 * 24) / (1000 * 60 * 60));
            var minute = parseInt((l - hour * 1000 * 60 * 60 - day * 1000 * 60 * 60 * 24) / (1000 * 60));
            var str = '';
            str += day === 0 ? "" : day + "天";
            str += hour === 0 ? "" : hour + "小时";
            str += minute === 0 ? "" : minute + "分钟";
            $("#duration").val(str);
        }
    }

    $("#form").validate();

    $("#btnSubmit").click(function () {
        if ($("#form").valid()) {
            $("#form").ajaxSubmit({
                loading: true,
                type: "post",
                url: Util.getPath() + "/meeting/save",
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

    $.post(Util.getPath() + "/train/usertree", function (res) {
        if (res.code === 200) {
            $.each(res.data, function (i, v) {
                if (v.type === "user") {
                    v.icon = Util.getRootPath() + "/static/images/user.png";
                }
            });
            var ztreeDown = $("#joinPeopleName").ztreeDown(res.data, {
                clearInput: false,
                check: {
                    enable: true
                },
                callback: {
                    onCheck: function (event, treeId, treeNode) {
                        var treeObj = $.fn.zTree.getZTreeObj(treeId);
                        var nodes = treeObj.getCheckedNodes(true);
                        var selectUsers = [];
                        $.each(nodes, function (i, v) {
                            if (v.type === 'user') {
                                selectUsers.push(v.id);
                            }
                        });
                        $("#joinPeopleName").val("已选择 " + selectUsers.length + " 人");
                        $("#joinPeople").val(selectUsers.join(","));
                    }
                }
            });

            ztreeDown.zTree.expandAll(true);
            $("#joinPeopleName").click(function () {
                ztreeDown.showMenu();
            });
        }
    }, "JSON");

});