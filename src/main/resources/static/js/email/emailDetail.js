$(function () {
    $.post(Util.getPath() + "/email/file/list", {emailId: $("#id").val()}, function (res) {
        if (res.code === 200 && res.data.length > 0) {
            var attachment = "";
            var fileHtml = $("#file-box-file").html();
            var imageHtml = $("#file-box-image").html();
            $.each(res.data, function (i, v) {
                if (isImage(v.fileName)) {
                    attachment += imageHtml.replace(/{{fileName}}/g, v.fileName)
                        .replace(/{{imageUrl}}/g, Util.getRootPath() + "/" + v.filePath)
                        .replace(/{{fileUrl}}/g, v.filePath)
                        .replace(/{{fileId}}/g, v.id);
                } else {
                    attachment += fileHtml.replace(/{{fileName}}/g, v.fileName)
                        .replace(/{{fileUrl}}/g, v.filePath)
                        .replace(/{{fileId}}/g, v.id);
                }
            });
            if (attachment !== '') {
                $("#attachment-num").text(" " + res.data.length + " 个附件");
                $("#mail-attachment").show();
                $("#attachment").append(attachment);
                $("#attachment .email-image").each(function () {
                    $(this).attr("src", $(this).attr("data-src"));
                });
            }
        }
    }, "json");

    //点击返回
    $(".btn-back").click(function () {
        parent.layer.close(parent.layer.getFrameIndex(window.name));
        if (parent.reloadGrid) {
            parent.reloadGrid();
        }
    });

    //点击转发
    $(".btn-forward").click(function () {
        location.href = Util.getPath() + "/emailsend/form/" + $("#id").val() + "?type=forward&emailType=" + $("#emailType").val();
    });

    //点击回复
    $(".btn-reply").click(function () {
        location.href = Util.getPath() + "/emailsend/form/" + $("#id").val() + "?type=reply&emailType=" + $("#emailType").val();
    });

    //点击回复全部
    $(".btn-reply-all").click(function () {
        location.href = Util.getPath() + "/emailsend/form/" + $("#id").val() + "?type=reply_all&emailType=" + $("#emailType").val();
    });

    //点击文件下载
    $("#attachment").on("click", ".file-download", function () {
        var url = Util.getPath() + "/email/file/download/" + $(this).attr("data-file-id");
        window.open(url);
    });

    //彻底删除
    $(".btn-delete").click(function () {
        deletes($("#id").val());
    });

    //回收站
    $(".btn-update-delete").click(function () {
        updateDeletes($("#id").val());
    });


    function isImage(fileName) {
        var imgExt = ['.bmp', '.jpg', '.png', '.gif', '.jpeg'];
        var ext = fileName.substring(fileName.lastIndexOf("."), fileName.length);
        for (var i = 0; i < imgExt.length; i++) {
            if (imgExt[i] === ext) {
                return true;
            }
        }
        return false;
    }

    function deletes(ids) {
        Util.ask("确定删除？", function (index) {
            Util.closeLayer(index);
            Util.postLoading(Util.getPath() + "/email/deletes", {ids: ids}, function (res) {
                if (res.code === 200) {
                    Util.msgOk("删除成功！");
                    parent.layer.close(parent.layer.getFrameIndex(window.name));
                    if (parent.reloadGrid) {
                        parent.reloadGrid();
                    }
                } else {
                    Util.msgError("删除失败！");
                }
            });
        });
    }

    function updateDeletes(ids) {
        Util.ask("确定移动到回收站？", function (index) {
            Util.closeLayer(index);
            Util.postLoading(Util.getPath() + "/email/update/deletes", {ids: ids}, function (res) {
                if (res.code === 200) {
                    Util.msgOk("移动到回收站成功！");
                    parent.layer.close(parent.layer.getFrameIndex(window.name));
                    if (parent.reloadGrid) {
                        parent.reloadGrid();
                    }
                } else {
                    Util.msgError("移动到回收站失败！");
                }
            });
        });
    }

});