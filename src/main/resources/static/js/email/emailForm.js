$(function () {
    $("input").iCheck({
        checkboxClass: 'icheckbox_minimal-green',
        radioClass: 'iradio_minimal-green'
    });

    $(".select2-multiple").select2({
        multiple: true
    });
    $(".select2-single").select2();

    // ------------------------------------富文本编辑框
    if (Util.getQueryString("type")) {
        $("#editor").prepend($("#from-templete").html());
    }
    var extraHeight = 40;
    var editor = UM.getEditor('editor', {
        imageUrl: Util.getPath() + "/umeditor/uploadimage",
        imagePath: Util.getRootPath() + "/",
        toolbar: [
            'undo redo | bold italic underline strikethrough | superscript subscript | forecolor backcolor | removeformat |',
            'insertorderedlist insertunorderedlist | fontfamily fontsize',
            '| justifyleft justifycenter justifyright justifyjustify |', 'link unlink | image',
            '| horizontal'
        ],
        initialFrameHeight: $("#edit-contain").height() - extraHeight,
        initialFrameWidth: '100%',
        autoHeightEnabled: false
    });

    // ------------------------------------文件上传控件
    var $list = $('#thelist'),
        state = 'pending';
    var uploader = WebUploader.create({
        swf: Util.getRootPath() + '/static/common/plugins/webuploader/Uploader.swf',
        server: Util.getPath() + '/temp/file/upload',
        // 选择文件的按钮。可选。
        // 内部根据当前运行是创建，可能是input元素，也可能是flash.
        pick: '#picker',
        // 不压缩image, 默认如果是jpeg，文件上传前会压缩一把再上传！
        resize: false,
        threads: 1,
        //单个文件大小限制,60M
        fileSingleSizeLimit: 1024 * 1024 * 60,
        //上传文件数量限制
        fileNumLimit: 10
    });
    // 当有文件被添加进队列的时候
    uploader.on('fileQueued', function (file) {
        $list.append($("#file-item-li").html().replace(/{{fileId}}/g, file.id).replace(/{{fileName}}/g, file.name));
        if (file.getStatus() === 'complete') {
            var $file = $('#' + file.id);
            $file.find('p.state').text('已上传');
        }
        uploader.upload();
    });
    // 文件上传过程中创建进度条实时显示。
    uploader.on('uploadProgress', function (file, percentage) {
        var $file = $('#' + file.id);
        $file.find('p.state').hide();
        $file.find(".progress").show();
        $file.find('.progress .progress-bar').css('width', percentage * 100 + '%');
    });
    uploader.on('uploadSuccess', function (file, res) {
        var $file = $('#' + file.id);
        $file.find(".progress").hide();
        $file.find('p.state').show().text('已上传');
        filesArra[file.id] = res.data[0];
    });

    uploader.on('uploadError', function (file) {
        var $file = $('#' + file.id);
        $file.find(".progress").hide();
        $file.find('p.state').show().text('上传出错');
    });

    uploader.on("error", function (errorType, size, file) {
        if (errorType === 'F_EXCEED_SIZE') {
            Util.msgError("文件 " + file.name + " 大小超过" + (size / 1024 / 1024) + "M");
        }
        if (errorType === "Q_EXCEED_NUM_LIMIT") {
            Util.msgError("上传文件数量不能超过 " + size + " 个");
        }

    });

    //删除已经上传的文件
    $list.on("click", ".file-delete", function () {
        var fileId = $(this).closest(".file-item").attr("id");
        uploader.removeFile(fileId);
        delete filesArra[fileId];
        $(this).closest(".file-item").remove();
    });

    // ------------------------------------发送邮件
    var filesArra = {};
    $.post(Util.getPath() + "/emailsend/receivers", function (res) {
        if (res.code === 200) {
            var options = '<option value="{{name}};{{email}}">{{name}} &lt;{{email}}&gt;</option>';
            $.each(res.data, function (i, v) {
                $("#receivers").append(options.replace(/{{name}}/g, v.name).replace(/{{email}}/g, v.email));
                $("#copys").append(options.replace(/{{name}}/g, v.name).replace(/{{email}}/g, v.email));
                $("#secrets").append(options.replace(/{{name}}/g, v.name).replace(/{{email}}/g, v.email));
            });
            selectUsers();
            replyUsers();
        }
    }, "JSON");

    selectedFiles();

    // 点击发送
    $("#send").click(function () {
        $("#schedule").val("0");
        $("#draft").val("0");
        send();
    });

    // 定时发送
    $("#schedule-send").click(function () {
        //初始化选择时间，选择一个小时之后
        var nextDate = new Date(new Date().getTime() + 60 * 60 * 1000);
        $("#year").find('option[value="' + parseNum(nextDate.getFullYear()) + '"]').attr("selected", true);
        $("#month").find('option[value="' + parseNum(nextDate.getMonth() + 1) + '"]').attr("selected", true);
        $("#day").find('option[value="' + parseNum(nextDate.getDate()) + '"]').attr("selected", true);
        $("#hour").find('option[value="' + parseNum(nextDate.getHours()) + '"]').attr("selected", true);
        $("#minute").find('option[value="' + parseNum(nextDate.getMinutes()) + '"]').attr("selected", true);

        layer.open({
            type: 1,
            title: false,
            area: ['450px'],
            scrollbar: false,
            resize: false,
            content: $("#schedule-time"),
            btn: ['确定', '取消'],
            yes: function (index) {
                var date = new Date(getScheduleTime().replace(/-/g, "/"));
                if (date.getTime() < new Date().getTime()) {
                    Util.msgError("选择的时间，不能小于当前时间！");
                    return;
                }
                if ((date.getTime() - new Date().getTime()) < 1000 * 60 * 10) {
                    Util.msgError("距离当前时间小于10分钟！");
                    return;
                }
                $("#draft").val("1");
                $("#schedule").val("1");
                $("#scheduleTime").val(getScheduleTime());
                saveDraft();
                layer.close(index);
            }
        });
    });

    // 保存草稿
    $("#save-draft").click(function () {
        $("#draft").val("1");
        $("#schedule").val("0");
        saveDraft();
    });

    // 关闭
    $("#close").click(function () {
        if ($("#id").val() || Util.getQueryString("type")) {
            Util.ask("关闭之前请注意保存，确定关闭？", function (index) {
                Util.closeLayer(index);
                parent.layer.close(parent.layer.getFrameIndex(window.name));
                if (parent.reloadGrid) {
                    parent.reloadGrid();
                }
            });
        } else {
            Util.ask("关闭之后，邮件内容将消失，确认关闭？", function (index) {
                Util.closeLayer(index);
                top.closeCurrentTab();
            });
        }
    });

    initDateSelect();

    /**
     * 发送邮件
     */
    function send() {
        if ($("#form").valid()) {
            var files = [];
            $.each(filesArra, function (i, v) {
                files.push(v);
            });
            $("#form").ajaxSubmit({
                loading: true,
                type: "post",
                data: {
                    files: JSON.stringify(files)
                },
                url: Util.getPath() + "/emailsend/save",
                success: function (res, statusText, xhr, $form) {
                    if (res.code === 200) {
                        Util.msgOk("邮件发送成功！");
                        if ($("#id").val() || Util.getQueryString("type")) {
                            parent.layer.close(parent.layer.getFrameIndex(window.name));
                            if (parent.reloadGrid) {
                                parent.reloadGrid();
                            }
                        } else {
                            top.closeCurrentTab();
                        }
                    } else {
                        Util.msgError("邮件发送失败！");
                    }
                }
            });
        } else {
            $('#form').validate().focusInvalid();
        }
    }

    /**
     * 保存草稿
     */
    function saveDraft() {
        if ($("#form").valid()) {
            var files = [];
            $.each(filesArra, function (i, v) {
                files.push(v);
            });
            $("#form").ajaxSubmit({
                loading: true,
                type: "post",
                data: {
                    files: JSON.stringify(files)
                },
                url: Util.getPath() + "/emailsend/save",
                success: function (res, statusText, xhr, $form) {
                    if (res.code === 200) {
                        Util.msgOk("邮件保存成功！");
                        if ($("#id").val() || Util.getQueryString("type")) {
                            parent.layer.close(parent.layer.getFrameIndex(window.name));
                            if (parent.reloadGrid) {
                                parent.reloadGrid();
                            }
                        } else {
                            top.closeCurrentTab();
                        }
                    } else {
                        Util.msgError("邮件保存失败！");
                    }
                }
            });
        } else {
            $('#form').validate().focusInvalid();
        }
    }

    /**
     * 更新选中状态
     */
    function selectUsers() {
        if ($("#id").val()) {
            $.post(Util.getPath() + "/emailsend/selectedusers", {"emailId": $("#id").val()}, function (res) {
                if (res.code === 200) {
                    $.each(res.data.receivers, function (i, v) {
                        $("#receivers").find('option[value="' + v + '"]').attr("selected", true);
                    });
                    $.each(res.data.copys, function (i, v) {
                        $("#copys").find('option[value="' + v + '"]').attr("selected", true);
                    });
                    $.each(res.data.secrets, function (i, v) {
                        $("#secrets").find('option[value="' + v + '"]').attr("selected", true)
                    });

                    $("#receivers").trigger("change");
                    $("#copys").trigger("change");
                    $("#secrets").trigger("change");

                }
            }, "JSON");
        }
    }

    /**
     * 回复人员
     */
    function replyUsers() {
        if ($("#fromId").val()) {
            var type = Util.getQueryString("type");
            if (type === 'forward') {
                return;
            }
            $.post(Util.getPath() + "/email/" + Util.getQueryString("emailType") + "/replyusers", {
                "emailId": $("#fromId").val(),
                "type": type
            }, function (res) {
                if (res.code === 200) {
                    $.each(res.data.receivers, function (i, v) {
                        $("#receivers").find('option[value="' + v + '"]').attr("selected", true);
                    });
                    $("#receivers").trigger("change");

                    if (type === 'reply') {
                        return;
                    }

                    $.each(res.data.copys, function (i, v) {
                        $("#copys").find('option[value="' + v + '"]').attr("selected", true);
                    });

                    $("#copys").trigger("change");

                }
            }, "JSON");
        }
    }

    /**
     * 查询已经选择的内容
     */
    function selectedFiles() {
        if ($("#id").val() || $("#fromId").val()) {
            $.post(Util.getPath() + "/email/file/list", {"emailId": $("#id").val() || $("#fromId").val()}, function (res) {
                if (res.code === 200) {
                    $.each(res.data, function (i, item) {
                        var obj = {};
                        obj.name = item.fileName;
                        obj.size = 1;
                        obj.lastModifiedDate = new Date();
                        obj.id = item.id;
                        obj.ext = item.fileName.substr(item.fileName.lastIndexOf(".") + 1, item.fileName.length);

                        var file = new WebUploader.File(obj);
                        //将文件状态改为'已上传完成'
                        file.setStatus('complete');
                        uploader.addFiles(file);
                        filesArra[file.id] = {
                            name: item.fileName,
                            size: item.fileSize,
                            url: item.filePath,
                            suffix: item.fileName.substr(item.fileName.lastIndexOf("."), item.fileName.length)
                        };
                    });
                }
            }, "JSON");
        }
    }

    function initDateSelect() {
        for (var i = 1; i <= 12; i++) {
            $("#month").append('<option value="' + parseNum(i) + '">' + i + '</option>');
        }
        initDay();
        for (var i = 0; i <= 23; i++) {
            $("#hour").append('<option value="' + parseNum(i) + '">' + i + '</option>');
        }
        for (var i = 0; i <= 59; i++) {
            $("#minute").append('<option value="' + parseNum(i) + '">' + i + '</option>');
        }
        $("#month").change(function () {
            initDay();
        });

        function initDay() {
            var days = 12;
            if ($.inArray($("#month").val(), ['01', '03', '05', '07', '08', '10', '12']) > -1) {
                days = 31;
            } else if ($.inArray($("#month").val(), ['04', '06', '09', '11']) > -1) {
                days = 30;
            } else if ($.inArray($("#month").val(), ['02']) > -1) {
                days = isLeapYear($("#year").val()) ? 29 : 28;
            }
            $("#day").empty();
            for (var i = 1; i <= days; i++) {
                $("#day").append('<option value="' + parseNum(i) + '">' + i + '</option>');
            }
        }
    }

    function parseNum(num) {
        return num < 10 ? ("0" + num) : num;
    }

    function isLeapYear(Year) {
        if (((Year % 4) == 0) && ((Year % 100) != 0) || ((Year % 400) == 0)) {
            return true;
        } else {
            return false;
        }
    }

    function getScheduleTime() {
        return $("#year").val() + "-" + $("#month").val() + "-" + $("#day").val() + " " + $("#hour").val() + ":" + $("#minute").val() + ":00";
    }
});