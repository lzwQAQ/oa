$(function () {

    var pendingFirstTask = (Util.getQueryString("firstTask") === "true") && Util.getQueryString("type") === "pending";

    $("#form").validate({
        messages: {
            files: {
                required: "请选择文件"
            }
        }
    });
    $("#form select").select2();
    $("#btns-div").show();
    $("#approvalResult").attr("disabled", "disabled");

    $("#btnCancel").click(function () {
        pendingFirstTask ? parent.parent.layer.close(parent.parent.layer.getFrameIndex(parent.window.name)) : top.closeCurrentTab();
    });

    function closeTask() {
        if (pendingFirstTask) {
            parent.parent.layer.close(parent.parent.layer.getFrameIndex(parent.window.name));
            top.simpleWorkFlow_ReloadGrid();
            delete top.window.simpleWorkFlow_ReloadGrid;
        } else {
            top.closeCurrentTab();
        }
    }

    //------------------------------------------------------------

    initSelectFile();

    $("#btnSubmit").click(function () {
        if ($("#form").valid()) {
            parent.beforeSubmitForm($("#auditResult").val(), function (taskResult) {
                $("#form").ajaxSubmit({
                    loading: true,
                    type: "post",
                    url: Util.getPath() + "/releasedocument/submit",
                    dataType: "json",
                    data: {
                        taskResult: taskResult
                    },
                    success: function (res, statusText, xhr, $form) {
                        if (res.code === 200) {
                            Util.msgOk("提交成功！");
                            closeTask();
                        } else {
                            Util.msgError("提交失败！");
                        }
                    }
                });
            });
        } else {
            $('#form').validate().focusInvalid();
        }
    });

    /**
     * 初始化文件选择效果
     */
    function initSelectFile() {
        // 选择文件
        var extNames = [".doc", ".docx", ".pdf"];
        $(".file-block").on("click", ".file-delete", function () {
            if ($(".select-filename").length === 1) {
                $("#select-file-btn").show();
            }
            var clz = $(this).parent(".select-filename").attr("data-clz");
            $("." + clz).remove();
        });
        $(".select-files").on("change", ".select-file", function () {
            //扩展名的文件名
            var fileName = this.value.substring(this.value.lastIndexOf("\\") + 1);
            if (fileName) {
                var $this = $(this);

                //检查扩展名
                var extName = fileName.substring(fileName.lastIndexOf("."));
                if ($.inArray(extName, extNames) === -1) {
                    $this.after($this.clone().val(""));
                    $this.remove();
                    Util.msgWarning('不支持该文件类型！');
                    return;
                }

                // 记录下文件
                var clz = new Date().getTime();
                $("#upload-file-block").append('<span title="' + fileName + '" data-clz="' + clz + '" class="select-filename ' + clz + '">' + fileName + '<button type="button" class="close file-delete"><span>&times;</span></button></span>');
                $this.addClass(clz + "");
                if ($(".select-filename").length === 1) {
                    $("#select-file-btn").hide();
                }
                $this.after($this.clone().removeClass(clz + "").val(""));
                $this.hide();
            }
        });
    }

});