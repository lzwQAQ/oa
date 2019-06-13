$(function () {

    initSelectFile();

    $("#form").validate({
        messages: {
            files: {
                required: "请选择文件"
            }
        }
    });

    $("#btnSubmit").click(function () {
        if ($("#form").valid()) {
            $("#form").ajaxSubmit({
                loading: true,
                type: "post",
                dataType: "json",
                url: Util.getPath() + "/releasedocument/filetemplete/save",
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
                $("#fileName").val(fileName);
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