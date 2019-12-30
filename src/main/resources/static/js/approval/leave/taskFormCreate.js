$(function () {

    var pendingFirstTask = (Util.getQueryString("firstTask") === "true") && Util.getQueryString("type") === "pending";

    $("#form").validate();
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

    var startTime, endTime;
    $('#startTime').daterangepicker({
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
        $(this).val(picker.startDate.format('YYYY-MM-DD HH:mm'));
        calculate(startTime, endTime, 8, 17.5, false, 12, 13.5)
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
        $(this).val(picker.startDate.format('YYYY-MM-DD HH:mm'));
        calculate(startTime, endTime, 8, 17.5, false, 12, 13.5)
    }).on('cancel.daterangepicker', function (ev, picker) {
        $(this).val('');
    });



    /**
     * 计算请假时长
     * @param {Object} beginTime    开始时间
     * @param {Object} endTime      结束时间
     * @param {Object} stWorkTime   上班时间
     * @param {Object} enWorkTime   下班时间
     * @param {Object} isFreeTime  是否要去除午休工作时长
     * @param {Object} freeTimeMon  午休开始时间
     * @param {Object} freeTimeAft  午休结束时间
     *
     */
    function calculate(beginTime, endTime, stWorkTime, enWorkTime, isFreeTime, freeTimeMon, freeTimeAft) {
        var days;
        var hours;
        var date;
        var freeTime = freeTimeAft - freeTimeMon;

        beginTime = beginTime.format('YYYY/MM/DD HH:mm:ss');
        var beginArr = beginTime.split(" ");
        var beginMonth = parseInt(beginArr[0].split("/")[1]);
        var beginDay = parseInt(beginArr[0].split("/")[2]);
        var beginHours = parseInt(beginArr[1].split(":")[0]);
        var beginMin = parseInt(beginArr[1].split(":")[1]);
        var beginHoursMin = beginHours + beginMin / 60;


        endTime = endTime.format('YYYY/MM/DD HH:mm:ss');
        var endArr = endTime.split(" ");
        var endMonth = parseInt(endArr[0].split("/")[1]);
        var endDay = parseInt(endArr[0].split("/")[2]);
        var endHours = parseInt(endArr[1].split(":")[0]);
        var endMin = parseInt(endArr[1].split(":")[1]);
        var endHoursMin = endHours + endMin / 60;


        //如果beginHoursMin时间小于上班时间都算上班时间
        if (beginHoursMin <= stWorkTime) {
            beginHoursMin = stWorkTime;
        }
        //如果endHoursMin时间大于上班时间都算下班时间
        if (endHoursMin >= enWorkTime) {
            endHoursMin = enWorkTime;
        }
        //如果结束时间在freeTimeMon和freeTimeAft之间都算freeTimeMon
        if (isFreeTime === true) {
            if (endHoursMin >= freeTimeMon && endHoursMin <= freeTimeAft) {
                endHoursMin = freeTimeMon;
            }
        }
        //获取结束时间-开始时间的天数
        var daysBetween = getDaysBetween(beginTime, endTime);
        if (daysBetween.length > 0) {
            var daysBetweenLen = daysBetween.length;
            //午休
            if (isFreeTime == true) {
                hour = enWorkTime - stWorkTime - freeTime;
                if (daysBetweenLen == 1) {
                    //同一天
                    hours = (endHoursMin) - (beginHoursMin) - freeTime;
                } else if (daysBetweenLen == 2) {
                    //跨一天
                    //第一天的时长
                    hours = enWorkTime - beginHoursMin;
                    //是否有午休
                    if (beginHoursMin <= freeTimeMon)
                        hours = hours - freeTime;
                    //第二天的时长
                    hours += endHoursMin - stWorkTime;
                    //是否有午休
                    if (endHoursMin >= freeTimeAft)
                        hours = hours - freeTime;
                } else {
                    //跨两天以上
                    //第一天的时长
                    hours = enWorkTime - beginHoursMin;
                    //是否有午休
                    if (beginHoursMin <= freeTimeMon)
                        hours = hours - freeTime;
                    //中间时长
                    hours += (daysBetweenLen - 2) * (hour);
                    //最后一天时长
                    hours += endHoursMin - stWorkTime;
                    //是否有午休
                    if (endHoursMin >= freeTimeAft)
                        hours = hours - freeTime;
                }
                days = Math.floor(hours / hour);
                hours = hours % hour;
                date = {"days": days, "hours": hours};
            } else {
                //非午休
                hour = enWorkTime - stWorkTime;
                if (daysBetweenLen == 1) {
                    //同一天
                    hours = (endHoursMin) - (beginHoursMin);
                } else if (daysBetweenLen == 2) {
                    //跨一天
                    hours = enWorkTime - beginHoursMin;
                    //第二天的时长
                    if (endHoursMin >= stWorkTime) {
                        //最后一天时长
                        hours += endHoursMin - stWorkTime;
                    }
                } else {
                    //跨两天以上
                    //第一天的时长
                    hours = enWorkTime - beginHoursMin;
                    //中间时长
                    hours += (daysBetweenLen - 2) * (hour);
                    if (endHoursMin >= stWorkTime) {
                        //最后一天时长
                        hours += endHoursMin - stWorkTime;
                    }
                }
                days = Math.floor(hours / hour);
                hours = hours % hour;
                date = {"days": days, "hours": hours};
            }
        }
        if (hours <= 5 && hours !== 0) {
            hours = 5
        }
        if (hours > 5) {
            hours = 0;
            days += 1;
        }
        $("#leaveDay").val(days + "." + hours);
        return date;
    }

    /**
     * 根据两个日期，判断相差天数
     * @param sDate1 开始日期 如：2016-11-01
     * @param sDate2 结束日期 如：2016-11-02
     * @returns {number} 返回相差天数
     */
    function getDaysBetween(sDate1, sDate2) {
        var arr = [];
        var startTime = new Date(sDate1);
        var endTime = new Date(sDate2);
        while ((endTime.getTime() - startTime.getTime()) >= 0) {
            var year = startTime.getFullYear();
            var month = (startTime.getMonth().toString().length === 1 ? "0" + startTime.getMonth().toString() : startTime.getMonth()) + 1;
            var day = startTime.getDate().toString().length === 1 ? "0" + startTime.getDate() : startTime.getDate();

            arr.push(year + "/" + month + "/" + day);
            startTime.setDate(startTime.getDate() + 1);
        }
        return arr;
    }


    $("#btnSubmit").click(function () {
        if ($("#form").valid()) {
            parent.beforeSubmitForm($("#auditResult").val(), function (taskResult) {
                $("#form").ajaxSubmit({
                    loading: true,
                    type: "post",
                    url: Util.getPath() + "/leave/submit",
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

});