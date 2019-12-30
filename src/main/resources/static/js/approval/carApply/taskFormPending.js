$(function () {

    loadAuditResult();

    $("#form").validate();

    $(".only-view").attr("disabled", "disabled");
    $("#approvalResult-div,#btns-div").show();

    $("#auditResult").on('change', function() {
        if ($(this).val() === "同意"){
            $("#car_select").attr("required", "required");
            $("#driver_select").attr("required", "required");
            if($("#carId").val() !=""){
                $("#car_select").val($("#carId").val()).attr("selected", true).trigger("change");
            }
            if($("#driverId").val() != ""){
                $("#driver_select").val().attr("selected", true).trigger("change");
            }
        }else {
            $("#car_select").removeAttr("required");
            $("#driver_select").removeAttr("required");
            $("#car_select").val("").attr("selected", true).trigger("change");
            $("#driver_select").val("").attr("selected", true).trigger("change");
        }
    });
    var drivers, cars;
    $("#driver_select").on('change',function () {
        var $this = $(this);
        $("#phone").val("");
        $.each(drivers, function (i, v) {
            if ($this.val() === v.id) {
                $("#phone").val(v.phone);
            }
        });
    });
    $("#car_select").on('change',function () {
        var $this = $(this);
        $("#brand").val("");
        $.each(cars, function (i, v) {
            if ($this.val() === v.id) {
                $("#brand").val(v.brand);
            }
        });
    });

    $("#btnSubmit").click(function () {
        if ($("#form").valid()) {
            Util.ask("确认提交审批？", function (index, layer) {
                Util.closeLayer(index);
                parent.beforeSubmitForm($("#auditResult").val(), function (taskResult) {
                    if ($("#auditResult").val() === "不同意"){
                        $("#carId").val("");
                        $("#driverId").val("");
                        $("#car_select").val("").attr("selected", true).trigger("change");
                        $("#driver_select").val("").attr("selected", true).trigger("change");
                    }
                    $("#form").ajaxSubmit({
                        loading: true,
                        type: "post",
                        url: Util.getPath() + "/carapply/approval",
                        data: {
                            taskResult: taskResult,
                            modelKey: Util.getQueryString("type")
                        },
                        success: function (res, statusText, xhr, $form) {
                            if (res.code === 200) {
                                Util.msgOk("提交成功！");
                                parent.parent.layer.close(parent.parent.layer.getFrameIndex(parent.window.name));
                                top.simpleWorkFlow_ReloadGrid();
                                delete top.window.simpleWorkFlow_ReloadGrid;
                            } else {
                                Util.msgError("提交失败！");
                            }
                        }
                    });
                });
            });
        } else {
            $('#form').validate().focusInvalid();
        }
    });

    $("#btnCancel").click(function () {
        parent.parent.layer.close(parent.parent.layer.getFrameIndex(parent.window.name));
    });

    /**
     * 加载审批选项
     */
    function loadAuditResult() {
        $("#auditResult-div").show();
        $.post(Util.getPath() + "/pendingtask/findNextSequenceFlowNames", {
            taskId: Util.getQueryString("taskId"),
            modelKey: Util.getQueryString("modelKey")
        }, function (res) {
            if (res.code === 200) {
                $("#auditResult").append("<option value=''>请选择处理结果</option>")
                $.each(res.data, function (i, v) {
                    $("#auditResult").append('<option value="' + v + '" >' + v + '</option>');
                });
                $("#audit-result-div").show();
            }
        });
    }

    // ---------------------------------
    $.post(Util.getPath() + "/carapply/finddrivers", function (res) {
        if (res.code === 200) {
            $.each(drivers = res.data, function (i, v) {
                $("#driver_select").append('<option value="' + v.id + '">' + v.name + '</option>');
            });
            $("#driver_select").find('option[value="' + $("#driverId").val() + '"]').attr("selected", true).trigger("change");
        }
    });
    $.post(Util.getPath() + "/carapply/findcars", function (res) {
        if (res.code === 200) {
            $.each(cars = res.data, function (i, v) {
                $("#car_select").append('<option value="' + v.id + '">' + v.carNo + '</option>')
            });
            $("#car_select").find('option[value="' + $("#carId").val() + '"]').attr("selected", true).trigger("change");
        }
    });
});