$(function () {
    var processInstanceId = Util.getQueryString("processInstanceId");

    $("#title li").click(function () {
        if ($(this).hasClass("active")) {
            return;
        }
        $("#title li").toggleClass("active");
        $("#content .tab-pane").toggleClass("active");
    });

    // 任务时间轴点击事件绑定
    $(".content-left").on("click", ".h-block", function () {
        var $this = $(this);
        var taskId = $this.attr("id");
        $(".content-left").find(".h-active").removeClass("h-active");
        $this.addClass("h-active");
        initTaskInfo(taskId);
    });

    // 初始化任务时间轴
    $.post(Util.getPath() + "/historictask/historicTaskTrackInfo", {processInstanceId: processInstanceId}, function (res) {
        if (res.code === 200) {
            initTaskInfo(res.data.list[0].taskId);
            $("#activiti-image").attr("src", Util.getPath() + "/historictask/historicTraskImage?ifSpecial=" + !res.data.isEnd + "&taskId=" + res.data.list[res.data.list.length - 1].taskId + "&stime=" + new Date().getTime());
            var isEnd = res.data.isEnd;
            $("#task-process").text(isEnd ? "(审批已结束)" : "(正在办理中)");
            var htmlTemplete = $(".h-block").prop("outerHTML");
            var $taskContentLeft = $(".content-left");
            var lastIndex = res.data.list.length;
            $.each(res.data.list, function (i, v) {
                var time = v.approvalDate.split(" ")[0];
                var date = v.approvalDate.split(" ")[1];
                var html = htmlTemplete.replace(/{{handle_time}}/g, time);
                html = html.replace(/{{handle_date}}/g, date);
                html = html.replace(/{{taskId}}/g, v.taskId);
                html = html.replace(/{{active}}/g, i === 0 ? "h-active" : "");
                html = html.replace(/{{taskName}}/g, v.taskName);
                html = html.replace(/{{is-end}}/g, (i + 1 === lastIndex) ? "h-end" : "");
                $taskContentLeft.append(html);
            });
            $taskContentLeft.find(".h-end .h-center").append(isEnd ? '<img class="h-circle-end" src="' + Util.getRootPath() + '/static/img/circle_end.png">' :
                '<img class="h-circle-end h-circle-arrow" src="' + Util.getRootPath() + '/static/img/arrow.png">');
        }
    });

    /**
     * 初始化任务信息，主要初始化任务轨迹中的列表和图片
     * @param taskId
     */
    function initTaskInfo(taskId) {
        $.post(Util.getPath() + "/historictask/findFormPath", {taskId: taskId}, function (res) {
            if (res.code === 200) {
                var data = res.data;
                $("#process-name").text("流程名称：" + data.processDefinitionName);
                $("#formContent").attr("src", Util.getPath() + "/" + data.formPath);
            }
        });
    }
});