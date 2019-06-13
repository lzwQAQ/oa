$(function () {
    $(".select2").select2();

    $("#chargePeoples").val("").select2({
        multiple: true,
        maximumSelectionLength: 3
    });

    var startTime, endTime;
    $('#beginTime').daterangepicker({
        singleDatePicker: true,
        timePicker: false,
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
        $(this).val(picker.startDate.format('YYYY-MM-DD'));
    }).on('cancel.daterangepicker', function (ev, picker) {
        $(this).val('');
    });

    $('#endTime').daterangepicker({
        singleDatePicker: true,
        timePicker: false,
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
        $(this).val(picker.startDate.format('YYYY-MM-DD'));
    }).on('cancel.daterangepicker', function (ev, picker) {
        $(this).val('');
    });

    $("#form").validate();

    $("#btnSubmit").click(function () {
        if ($("#form").valid()) {
            $("#form").ajaxSubmit({
                loading: true,
                type: "post",
                url: Util.getPath() + "/plan/save",
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

    $.post(Util.getPath() + "/plan/chargepeoples", {
        planId: $("#id").val()
    }, function (res) {
        if (res.code === 200) {
            $.each(res.data, function (i, v) {
                $("#chargePeoples").find('option[value="' + v.id + '"]').attr("selected", true);
            });
            $("#chargePeoples").trigger("change");
        }
    }, "json");

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
                        calculateJoinpeoples($.fn.zTree.getZTreeObj(treeId));
                    }
                }
            });

            ztreeDown.zTree.expandAll(true);
            $("#joinPeopleName").click(function () {
                ztreeDown.showMenu();
            });

            //查询已经选择了多少个人
            $.post(Util.getPath() + "/plan/joinpeoples", {
                planId: $("#id").val()
            }, function (res) {
                if (res.code === 200) {
                    var nodes = ztreeDown.zTree.transformToArray(ztreeDown.zTree.getNodes());
                    if (res.data) {
                        $.each(res.data, function (i, v) {
                            $.each(nodes, function (i, nv) {
                                if (v.id === nv.id) {
                                    ztreeDown.zTree.checkNode(nv, true, true);
                                    return false;
                                }
                            });
                        });
                    }
                    calculateJoinpeoples(ztreeDown.zTree);
                }
            }, "json");

            function calculateJoinpeoples(treeObj) {
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
    }, "JSON");

});