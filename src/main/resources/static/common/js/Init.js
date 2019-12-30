//初始化工具类
(function (win) {
    String.prototype.trim = function () {
        return this.replace(/(^\s*)|(\s*$)/g, "");
    };

    String.prototype.replaceAll = function (s1, s2) {
        return this.replace(new RegExp(s1, "gm"), s2);
    };

    String.prototype.startWith = function (str) {
        if (str == null || str == "" || this.length == 0 || str.length > this.length) {
            return false;
        }
        if (this.substr(0, str.length) == str) {
            return true;
        } else {
            return false;
        }
        return true;
    };

    String.prototype.endWith = function (str) {
        if (str == null || str == "" || this.length == 0 || str.length > this.length) {
            return false;
        }
        if (this.substring(this.length - str.length) == str) {
            return true;
        } else {
            return false;
        }
        return true;
    };

    function getCookieVal(offset) {
        var endstr = document.cookie.indexOf(";", offset);
        if (endstr == -1) {
            endstr = document.cookie.length;
        }
        return unescape(document.cookie.substring(offset, endstr));
    }

    function openFile(command, filePath) {
        if (!Util.isIE()) {
            alert('当前浏览器不支持该项操作，请在IE6及以上版本重试！');
            return;
        }
        try {
            var shell = null;
            if (command == 'visio') {
                new ActiveXObject("Visio.Application").documents.open(filePath);
            } else if (command == 'pdf') {
                window.open(filePath);
            } else {
                shell = new ActiveXObject("wscript.shell");
                shell.run(command + " " + filePath);
            }
        } catch (e) {
            alert("打开文件失败,请检查浏览器安全级别！");
        }
    }

    function getHttpRequest() {
        var http;
        var activeX = ['MSXML2.XMLHTTP.3.0', 'MSXML2.XMLHTTP', 'Microsoft.XMLHTTP'];
        try {
            http = new XMLHttpRequest();
        } catch (e) {
            for (var i = 0; i < activeX.length; ++i) {
                try {
                    http = new ActiveXObject(activeX[i]);
                    break;
                } catch (e) {
                }
            }
        } finally {
            return http;
        }
    }

    win.Util = {
        _path: null,
        _rootpath: null,
        //获取管理路径名称
        getPath: function () {
            return this._path;
        },
        //获取静态资源路径名称
        getRootPath: function () {
            return this._rootpath;
        },
        //获取URL参数的值
        getQueryString: function (key) {
            var params = {};
            var qs = location.search.substring(1, location.search.length);
            if (qs.length == 0) {
                return;
            }
            qs = qs.replace(/\+/g, ' ');
            var args = qs.split('&');
            for (var i = 0; i < args.length; i++) {
                var pair = args[i].split('=');
                var name = decodeURIComponent(pair[0]);
                var value = (pair.length == 2) ? decodeURIComponent(pair[1]) : name;
                params[name] = value;
            }
            return params[key];
        },
        getAnchorObject: function () {
            var params = location.hash.split('\/');
            if (params.length > 3) {
                return {'option': params[2], 'value': params[3]};
            } else {
                return {};
            }
        },
        //空处理
        nullToEmpty: function (obj) {
            if (obj == null || obj == undefined || obj == 'null') {
                return '';
            } else {
                return obj;
            }
        },
        //打开窗口（非模式）
        openWin: function (url, name, width, height) {
            var iTop = (window.screen.availHeight - 30 - height) / 2;
            var iLeft = (window.screen.availWidth - 10 - width) / 2;
            window.open(url, name, 'height=' + height + ',' + 'innerHeight=' + height + ',width=' + width + ',innerWidth=' + width + ',top=' + iTop + ',left=' + iLeft + ',toolbar=no,menubar=no,scrollbars=auto,resizeable=no,location=no,status=no');
        },
        //打开窗口（模式）
        openDialogWin: function (url, width, height) {
            return window.showModalDialog(url, window, "dialogWidth:" + width + "px;dialogHeight:" + height + "px;resizable:no;center:yes;location:no;status:no");
        },
        //去除空格
        trim: function (str) {
            return str.trim();
        },
        //替换所有
        replaceAll: function (str, r1, r2) {
            return str.replaceAll(r1, r2);
        },
        //是否是IE浏览器
        isIE: function () {
            return navigator.appName == 'Microsoft Internet Explorer';
        },
        //str字符中是否以startStr字符开始
        startWith: function (str, startStr) {
            return str.startWith(startStr);
        },
        //str字符中是否以endStr字符结束
        endWith: function (str, endStr) {
            return str.endWith(endStr);
        },
        //根据name获取选中的对象 支持checkbox|radio
        getChecked: function (name) {
            var objArr = [];
            var nameObj = document.getElementsByName(name);
            for (var i = 0; i < nameObj.length; i++) {
                if (nameObj[i].checked) {
                    objArr.push(nameObj[i]);
                }
            }
            return objArr;
        },
        //根据id获取选中的对象 支持select
        getSelected: function (id) {
            var idObj = document.getElementById(id);
            return idObj.options[idObj.selectedIndex];
        },
        //获取日期
        getDate: function () {
            var date = new Date();
            var year = date.getFullYear();
            var month = parseInt(date.getMonth()) + 1;
            var day = parseInt(date.getDate());
            return year + '-' + String(month <= 9 ? '0' + month : month) + '-' + String(day <= 9 ? '0' + day : day);
        },
        //获取时间
        getTime: function () {
            var date = new Date();
            var hours = parseInt(date.getHours());
            var minutes = parseInt(date.getMinutes());
            var ss = date.getTime() % 60000;
            ss = parseInt((ss - (ss % 1000)) / 1000);
            return String(hours <= 9 ? '0' + hours : hours) + ':' + String(minutes <= 9 ? '0' + minutes : minutes) + ':' + String(ss <= 9 ? '0' + ss : ss);
        },
        //获取日期时间
        getDateTime: function () {
            return this.getDate() + ' ' + this.getTime();
        },
        //获取星期
        getToday: function () {
            var day = new Array();
            day[0] = "星期日";
            day[1] = "星期一";
            day[2] = "星期二";
            day[3] = "星期三";
            day[4] = "星期四";
            day[5] = "星期五";
            day[6] = "星期六";
            var date = new Date();
            return (day[date.getDay()]);
        },
        //escape编码
        escape: function (str) {
            return escape(str);
        },
        //unescape解码
        unescape: function (str) {
            return unescape(str);
        },
        //encodeURIComponent编码
        encodeURIComponent: function (str) {
            return encodeURIComponent(str);
        },
        //decodeURIComponent解码
        decodeURIComponent: function (str) {
            return decodeURIComponent(str);
        },
        //整段URL编码
        encodeURI: function (url) {
            return encodeURI(url);
        },
        //整段URL解码
        decodeURI: function (url) {
            return decodeURI(url);
        },
        //存放cookie
        setCookie: function (name, value) {
            var expdate = new Date();
            expdate.setTime(expdate.getTime() + (24 * 60 * 60 * 1000 * 31));
            var argv = this.setCookie.arguments;
            var argc = this.setCookie.arguments.length;
            var path = (argc > 3) ? argv[3] : null;
            var domain = (argc > 4) ? argv[4] : null;
            var secure = (argc > 5) ? argv[5] : false;
            document.cookie = name + "=" + escape(value) + "; expires=" + expdate.toGMTString() + ((path == null) ? "" : ("; path=" + path)) + ((domain == null) ? "" : ("; domain=" + domain)) + ((secure == true) ? "; secure" : "");
        },
        //存放cookie(天数)
        setCookie: function (name, value, days) {
            var expdate = new Date();
            expdate.setTime(expdate.getTime() + (24 * 60 * 60 * 1000 * days));
            var argv = this.setCookie.arguments;
            var argc = this.setCookie.arguments.length;
            var path = (argc > 3) ? argv[3] : null;
            var domain = (argc > 4) ? argv[4] : null;
            var secure = (argc > 5) ? argv[5] : false;
            document.cookie = name + "=" + escape(value) + "; expires=" + expdate.toGMTString() + ((path == null) ? "" : ("; path=" + path)) + ((domain == null) ? "" : ("; domain=" + domain)) + ((secure == true) ? "; secure" : "");
        },
        //获取cookie
        getCookie: function (name) {
            var arg = name + "=";
            var alen = arg.length;
            var clen = document.cookie.length;
            var i = 0;
            while (i < clen) {
                var j = i + alen;
                if (document.cookie.substring(i, j) == arg) {
                    return getCookieVal(j);
                }
                i = document.cookie.indexOf(" ", i) + 1;
                if (i == 0) {
                    break;
                }
            }
            return null;
        },
        //打开Word
        openWord: function (filePath) {
            openFile('winword', filePath);
        },
        //打开Excel
        openExcel: function (filePath) {
            openFile('excel', filePath);
        },
        //打开PPT
        openPowerPoint: function (filePath) {
            openFile('powerpnt', filePath);
        },
        //打开Visio
        openVisio: function (filePath) {
            openFile('visio', filePath);
        },
        //打开Pdf
        openPdf: function (filePath) {
            openFile('pdf', filePath);
        },
        validate: function (element, option) {
            var defaultOptions = {
                highlight: function (element, errorClass, validClass) {
                    $(element).closest('.form-group').addClass('has-error');
                },
                unhighlight: function (element, errorClass) {
                    var $parent = $(element).parent();
                    $(element).closest('.form-group').removeClass('has-error');
                },
                success: function (error, element) {
                    $(element).closest('.form-group').removeClass('has-error');
                },
                errorPlacement: function (error, element) {
                    var $element = $(element);
                    if ($element.is(":radio") || $element.is(":checkbox")) {
                        error.appendTo($element.parent().parent().parent());
                    } else {
                        error.appendTo($element.parent());
                    }
                },
                errorClass: "help-block m-b-none",
                errorElement: 'span'
            };
            var options = $.extend({}, defaultOptions, option);
            return $(element).validate(options);
        },
        msgWarning: function (msg) {
            return top.layer.msg(msg, {icon: 0, time: 1500});
        },
        msgOk: function (msg) {
            return top.layer.msg(msg, {icon: 1, time: 1000});
        },
        msgError: function (msg) {
            return top.layer.msg(msg, {icon: 2, time: 1500});
        },
        loading: function () {
            return top.layer.load();
        },
        closeLayer: function (index) {
            top.layer.close(index);
        },
        ask: function (msg, yesFunc, cancelFunc) {
            return top.layer.confirm(msg, {icon: 3, title: '系统提示'}, yesFunc, cancelFunc);
        },
        postLoading: function (url, param, success) {
            var loadIndex = Util.loading();
            $.post(url, param, success, "JSON").error(function () {
                Util.closeLayer(loadIndex);
                Util.msgError("操作失败！");
            }).complete(function () {
                Util.closeLayer(loadIndex);
            });
        },
        ajaxFormLoading: function (item, url, calback) {
            var loadIndex = Util.loading();
            $(item).ajaxSubmit({
                url: url,
                success: function (responseText, statusText, xhr, $form) {
                    Util.closeLayer(loadIndex);
                    calback(responseText, statusText, xhr, $form);
                },
                error: function () {
                    Util.closeLayer(loadIndex);
                    Util.msgError("操作失败！");
                }
            });
        },
        openIframe: function (url, yes) {
            top.layer.open({
                type: 2,
                area: ['800px', '450px'],
                scrollbar: false,
                resize: false,
                content: url,
                btn: ['确定', '取消'],
                yes: yes
            });
        },
        fullIframe: function (options) {
            var width = top.$(top.window).width();
            var height = top.$(top.window).height();
            options = $.extend(options, {
                type: 2,
                area: [width + 'px', height + 'px'],
                scrollbar: false,
                move: false,
                resize: false,
                success: function (layero, index) {
                    top.$(top.window).resize(fuc);
                },
                end: function () {
                    top.$(top.window).unbind("resize", fuc);
                }
            }, {});
            var index = top.layer.open(options);
            var fuc = function () {
                top.layer.full(index);
            };
        },
        innerIframe: function (url) {
            var width = $(window).width();
            var height = $(window).height();
            var index = layer.open({
                type: 2,
                closeBtn: false,
                scrollbar: false,
                resize: false,
                title: false,
                content: url,
                area: [width + 'px', height + 'px'],
                success: function (layero, index) {
                    $(window).resize(fuc);
                },
                end: function () {
                    $(window).unbind("resize", fuc);
                }
            });
            var fuc = function () {
                layer.full(index);
            };
        },
        viewImage: function (src) {
            $("<img/>").attr("src", src).load(function () {
                var width = this.width;
                var height = this.height;
                if (width >= height) {
                    var wWidth = $(top.window).width();
                    $(this).width(wWidth < width ? wWidth * 0.9 : width);
                } else {
                    var wHeight = $(top.window).height();
                    $(this).height(wHeight < height ? wHeight * 0.9 : height);
                }

                top.layer.open({
                    type: 1,
                    shade: 0.3,
                    area: ['auto', 'auto'],
                    content: $(this).prop("outerHTML"),
                    title: false
                });
            });
        },
        removeLastTab: function () {
            if (this.sessionStorageSupport) {
                sessionStorage.removeItem('menu_href');
            }
        },
        localStorageSupport: function () {
            //判断浏览器是否支持html5本地存储
            return (('localStorage' in window) && window['localStorage'] !== null)
        },
        sessionStorageSupport: function () {
            //判断浏览器是否支持html5本地存储
            return (('sessionStorage' in window) && window['sessionStorage'] !== null)
        },
        strTrim: function (str) {
        return str.replace(/(^\s*)|(\s*$)/g, "");
    }
    };
})(window);

//个性化配置jqgrid
(function ($) {

    // 未加载jqgrid，直接返回
    if (!$.jgrid) {
        return;
    }

    // 使用fontAwesome的字体样式覆盖原有的bootstrap字体样式
    // 这一大段代码是从源码当中拷贝而来
    $.extend(true, $.jgrid, {
        styleUI: {
            Bootstrap: {
                treegrid: {
                    icon_leaf: 'fa-circle-pointer'//为自定义属性
                }
            }
        }
    });

    $.extend(true, $.jgrid.defaults, {
        datatype: "json",
        treedatatype: "json",
        mtype: "POST",
        autowidth: true,
        responsive: false,//不使用自带的responsive，因为在宽度变化的时候默认是500毫秒之后改变（时间太长了），且时间不可设置
        styleUI: "Bootstrap",
        altRows: true,//斑马条纹
        prmNames: {
            page: "pageNum",
            rows: "pageSize",
            sort: null,
            order: null,
            search: null,
            nd: null,
            npage: null
        },
        jsonReader: {
            id: "id",
            root: "data",
            page: "pageNum",
            total: "pages",
            records: "total"
        },
        // 定义一组属性，这些属性重写所有colModel中的默认值
        cmTemplate: {
            resizable: false,
            sortable: false
        },
        rowNum: 15,
        rowList: [15, 25, 30],
        pager: "#page",
        pagerpos: "right",
        treeGrid: false,// 是否启用树结构表格
        treeGridModel: 'adjacency',
        treeGrid_bigData: false,//这里不用分页查询了
        treeReader: {
            level_field: "treeLevel",
            parent_id_field: "parentId",
            leaf_field: "leaf"
        },
        defaultExpandLevel: 1,	// 默认展开的层次
        onPaging: function (pgButton, ele) {
            if ("records" === pgButton) {
                // 切换每页显示数量的时候，均从第一页开始查询
                $(this).setGridParam({page: 1});
            } else if ("user" === pgButton) {
                //所有小于等于0或者大于最大页数的，均查询第一页
                if ($(this).getGridParam("lastpage") < $(ele).val() || $(ele).val() <= 0) {
                    $(ele).val(1);
                }
            }
        },
        beforeSelectRow: function (rowid, e) {
            //非多选情况下，支持点击行选中
            if ($(this).jqGrid('getGridParam', 'multiselect') === false) {
                return true;
            }

            //多选情况下，禁止点击行选中
            var i = $.jgrid.getCellIndex($(e.target).closest('td')[0]),
                cm = $(this).jqGrid('getGridParam', 'colModel');
            return (cm[i].name === 'cb');
        }
        /*postData: {
            code: $("#code").val(),
            name: $("#name").val()
        },*///这里的postData可用于初始化参数传递
    });

    //自定义实现宽高自适应,parent:父类高度，otherItem：起源元素的高度，offsetHeight：计算不准确时的偏移量
    $.fn.extend({
        enableAutoHeight: function (parent, otherItem, offsetHeight) {

            //表格宽高自适应，宽度发生变化后，200毫秒之后进行宽高调整，如果200毫秒之内再次发生变化，则再等200毫秒
            var $this = this;
            tableHeightResize();
            var timeout = null;
            var orientationEvent = "onorientationchange" in window ? "orientationchange" : "resize";
            $(window).on(orientationEvent, function () {
                if (timeout) {
                    clearTimeout(timeout);
                }
                timeout = setTimeout(tableHeightResize, 200);
            });

            timeout = setTimeout(tableHeightResize, 200);

            function tableHeightResize() {
                timeout = null;
                var height = 0;
                $(otherItem).each(function (i, v) {
                    height += $(this).outerHeight();
                });

                //分页插件的高度
                var options = $this.getGridParam();
                if (options.pager) {
                    height += $(options.pager).outerHeight();
                }

                height = $(parent).height() - height - 45;
                $this.resizeGrid(0);
                $this.setGridHeight(height + (offsetHeight ? offsetHeight : 0));
            }

            return this;
        }
    });
})(jQuery);

//textarea高度随内容自适应
(function ($) {
    //文本框自动拉伸
    $.fn.autoTextarea = function (options) {
        var defaults = {
            maxHeight: null,//文本框是否自动撑高，默认：null，自动撑高；如果自动撑高必须输入数值，该值作为文本框自动撑高的最大高度
            minHeight: $(this).outerHeight() //默认最小高度，也就是文本框最初的高度，当内容高度小于这个高度的时候，文本以这个高度显示
        };
        var opts = $.extend({}, defaults, options);
        return $(this).each(function () {
            $(this).bind("paste cut keydown keyup focus blur input onpropertychange", function () {
                var height, style = this.style;
                var temp = -1;
                while (temp != this.scrollHeight) {
                    temp = this.scrollHeight;
                    if (this.scrollHeight > opts.minHeight) {
                        if (opts.maxHeight && this.scrollHeight > opts.maxHeight) {
                            height = opts.maxHeight;
                            style.overflowY = 'scroll';
                        } else {
                            height = this.scrollHeight;
                            style.overflowY = 'hidden';
                        }
                        style.height = height + 'px';
                    }
                }
            });
        });
    };
})(jQuery);

//jquery validate 默认配置修改及本地化处理
(function ($) {
    if (!$.validator) {
        return;
    }
    /*
     * Translated default messages for the jQuery validation plugin.
     * Locale: ZH (Chinese, 中文 (Zhōngwén), 汉语, 漢語)
     */
    $.extend($.validator.messages, {
        required: "该项为必填内容",
        remote: "请修正此字段",
        email: "请输入有效的电子邮件地址",
        url: "请输入有效的网址",
        date: "请输入有效的日期",
        dateISO: "请输入有效的日期 (YYYY-MM-DD)",
        number: "请输入有效的数字",
        digits: "只能输入数字",
        creditcard: "请输入有效的信用卡号码",
        equalTo: "两次输入的内容不相同",
        extension: "请输入有效的后缀",
        maxlength: $.validator.format("最多可以输入 {0} 个字符"),
        minlength: $.validator.format("最少要输入 {0} 个字符"),
        rangelength: $.validator.format("请输入长度在 {0} 到 {1} 之间的字符串"),
        range: $.validator.format("请输入范围在 {0} 到 {1} 之间的数值"),
        max: $.validator.format("请输入不大于 {0} 的数值"),
        min: $.validator.format("请输入不小于 {0} 的数值")
    });

    $.extend(true, $.validator.defaults, {
        highlight: function (element, errorClass, validClass) {
            $(element).addClass('validate-form-danger');
        },
        unhighlight: function (element, errorClass) {
            $(element).removeClass('validate-form-danger');
            this.settings.closeIndex();
        },
        success: function (error, element) {
            $(element).removeClass('validate-form-danger');
            this.closeIndex();
        },
        errorPlacement: function (error, element) {
            $(element).attr("error-msg", error.text());
            /*var $parent = $(element).parent();
            if ($parent.hasClass("input-group")) {
                $parent = $parent.parent();
            }
            error.appendTo($parent);*/
        },
        errorElement: 'small',
        errorClass: "text-danger",
        closeIndex: function () {
            if (this.lastIndex) {
                layer.close(this.lastIndex);
            }
        }
    });

    var _validate = $.fn.validate;
    $.fn.validate = function () {
        if (!$.data(this[0], "validator")) {
            var $this = this;
            this.on("focus", ".validate-form-danger", function () {
                var validate = $.data($this[0], "validator");
                validate.settings.lastIndex = layer.tips($(this).attr("error-msg"), this, {time: 1500});
            });
        }
        return _validate.apply(this, Array.prototype.slice.apply(arguments));
    };

    $.validator.addMethod("en", function (value, element) {
        var tel = /^[A-Za-z]+$/;
        return this.optional(element) || (tel.test(value));
    }, "请输入英文字母");

    $.validator.addMethod("en-num", function (value, element) {
        var tel = /^[a-zA-Z0-9]+$/;
        return this.optional(element) || (tel.test(value));
    }, "只能输入英文字母和数字");

   /* $.validator.addMethod("phone", function (value, element) {
        var tel = /^1\d{10}$/;
        return this.optional(element) || (tel.test(value));
    }, "请输入正确的手机号");*/

})(jQuery);

(function ($) {
    if (!$.fn.ajaxSubmit) {
        return;
    }
    var _ajaxSubmit = $.fn.ajaxSubmit;

    /**
     * ajaxSubmit() provides a mechanism for immediately submitting
     * an HTML form using AJAX.
     *
     * @param    {object|string}    options        jquery.form.js parameters or custom url for submission
     * @param    {object}        data        extraData
     * @param    {string}        dataType    ajax dataType
     * @param    {function}        onSuccess    ajax success callback function
     */
    $.fn.ajaxSubmit = function (options, data, dataType, onSuccess) {
        if (options.loading === true) {
            var loadIndex = Util.loading();
            if (typeof options.success === 'function') {
                var _success = options.success;
                options.success = function (data, status, xhr, $form) {
                    Util.closeLayer(loadIndex);
                    return _success.apply(this, [data, status, xhr, $form]);
                }
            } else {
                options.success = function (data, status, xhr, $form) {
                    Util.closeLayer(loadIndex);
                }
            }

            if (typeof options.error === 'function') {
                var _error = options.error;
                options.error = function (xhr, status, error, $form) {
                    Util.closeLayer(loadIndex);
                    return _error.apply(this, [xhr, status, error, $form]);
                }
            } else {
                options.error = function (xhr, status, error, $form) {
                    Util.closeLayer(loadIndex);
                    Util.msgError("操作失败！");
                }
            }


        }
        return _ajaxSubmit.apply(this, [options, data, dataType]);
    }
})(jQuery);

//select2默认配置选项
(function ($) {
    if ($.fn.select2) {
        $.fn.select2.defaults.set("language", "zh-CN");
        $.fn.select2.defaults.set("width", "100%");
    }
})(jQuery);

//daterangepicker本地化
(function (moment) {
    if (!moment) {
        return;
    }
    //localization example for daterang picker
    //change moment.js language to French
    //example taken from: http://momentjs.com/docs/#/i18n/changing-language/

    moment.locale('zh-cn', {
        months: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
        monthsShort: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
        weekdays: ["星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"],
        weekdaysShort: ["周日", "周一", "周二", "周三", "周四", "周五", "周六", "周日"],
        weekdaysMin: ["日", "一", "二", "三", "四", "五", "六", "日"],
        longDateFormat: {
            LT: "Ah点mm",
            L: "YYYY年MMMD日",
            LL: "YYYY年MMMD日",
            LLL: "YYYY年MMMD日LT",
            LLLL: "YYYY年MMMD日ddddLT"
        },
        calendar: {
            sameDay: "今天",
            nextDay: '明天',
            nextWeek: '下周',
            lastDay: '昨天',
            lastWeek: '上周',
            sameElse: 'L'
        },
        relativeTime: {
            future: "%s内",
            past: "%s前",
            s: "几秒",
            m: "1分钟",
            mm: "%d分钟",
            h: "1小时",
            hh: "%d小时",
            d: "1天",
            dd: "%d天",
            M: "1个月",
            MM: "%d个月",
            y: "1年",
            yy: "%d年"
        },
        ordinal: function (number) {
            return number + (number === 1 ? 'er' : 'ème');
        },
        week: {
            dow: 1, // Monday is the first day of the week.
            doy: 4  // The week that contains Jan 4th is the first week of the year.
        }
    });
})(typeof moment === "undefined" ? null : moment);

(function ($) {
    $.extend($.fn, {
        ztreeDown: function (data, options) {
            var $this = $(this);
            var setting = {
                view: {
                    dblClickExpand: false
                },
                data: {
                    simpleData: {
                        enable: true
                    }
                },
                clearInput: true
            };
            var id = $(this).attr("id");
            $.extend(setting, options, true);
            var treeDiv = '<div id="' + id + '_menuContent" class="menuContent">' +
                '<ul id="' + id + '_treeDemo" class="ztree"></ul>' +
                '</div>';
            $("body").append(treeDiv);

            var zTree = $.fn.zTree.init($("#" + id + "_treeDemo"), setting, data);

            function showMenu(fuc) {
                var cityObj = $this;
                var cityOffset = $this.offset();
                $('#' + id + '_menuContent').css({
                    left: cityOffset.left + "px",
                    top: cityOffset.top + cityObj.outerHeight() + "px",
                    width: cityObj.outerWidth()
                }).slideDown("fast");

                $("body").bind("mousedown", onBodyDown);

                if (fuc instanceof Function) {
                    fuc();
                }
            }

            function hideMenu() {
                $('#' + id + '_menuContent').fadeOut("fast");
                $("body").unbind("mousedown", onBodyDown);
            }

            function onBodyDown(event) {
                if (!(event.target.id == "parentName" || event.target.id == (id + "_menuContent") || $(event.target).parents("#" + id + "_menuContent").length > 0)) {
                    $("#parentId").val("");
                    if (options.clearInput) {
                        $this.val("");
                    }
                    hideMenu();
                }
            }

            return {
                hideMenu: hideMenu,
                showMenu: showMenu,
                zTree: zTree
            };
        }
    });

})(jQuery);