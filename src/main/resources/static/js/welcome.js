$(function () {
    var latestEmailId;
    $.post(Util.getPath() + "/emailreceive/latestemail", function (res) {
        if (res.code === 200) {
            if (res.data) {
                latestEmailId = res.data.id;
            }
            setInterval(function () {
                $.post(Util.getPath() + "/emailreceive/latestemail", function (res) {
                    if (res.code === 200) {
                        if (res.data && res.data.id !== latestEmailId) {
                            latestEmailId = res.data.id;
                            notifyEmail(res.data.senderName, res.data.senderAccount, res.data.title);
                        }
                    }
                }, "JSON");
            }, 3000);
        }
    }, "JSON");


    function notifyEmail(senderName, senderAccount, title) {
        top.$(".newmailNotify").remove();
        var html = $("#email_notice").html().replace("{{senderName}}", senderName).replace("{{senderAccount}}", senderAccount).replace("{{title}}", title);
        var $notify = top.$(html).appendTo("body");
        $notify.find(".webpushtip1close").click(function () {
            $(this).parents(".newmailNotify").remove();
            return false;
        });
        $notify.find(".notify_type,.notify_content").click(function () {
            $(this).parents(".newmailNotify").remove();
            Util.fullIframe({
                title: '邮件',
                closeBtn: false,
                content: Util.getPath() + "/email/view/receive/" + latestEmailId
            });
        });
    }

});