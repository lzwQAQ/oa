$(function () {
    //计算元素集合的总宽度
    function calSumWidth(elements) {
        var width = 0;
        $(elements).each(function () {
            width += $(this).outerWidth(true);
        });
        return width;
    }

    //滚动到指定选项卡
    function scrollToTab(element) {
        var marginLeftVal = calSumWidth($(element).prevAll()),
            marginRightVal = calSumWidth($(element).nextAll());
        // 可视区域非tab宽度
        var tabOuterWidth = calSumWidth($(".content-tabs").children().not(".J_menuTabs"));
        //可视区域tab宽度
        var visibleWidth = $(".content-tabs").outerWidth(true) - tabOuterWidth;
        //实际滚动宽度
        var scrollVal = 0;
        if ($(".page-tabs-content").outerWidth() < visibleWidth) {
            scrollVal = 0;
        } else if (marginRightVal <= (visibleWidth - $(element).outerWidth(true) - $(element).next().outerWidth(true))) {
            if ((visibleWidth - $(element).next().outerWidth(true)) > marginRightVal) {
                scrollVal = marginLeftVal;
                var tabElement = element;
                while ((scrollVal - $(tabElement).outerWidth()) > ($(".page-tabs-content").outerWidth() - visibleWidth)) {
                    scrollVal -= $(tabElement).prev().outerWidth();
                    tabElement = $(tabElement).prev();
                }
            }
        } else if (marginLeftVal > (visibleWidth - $(element).outerWidth(true) - $(element).prev().outerWidth(true))) {
            scrollVal = marginLeftVal - $(element).prev().outerWidth(true);
        }
        $('.page-tabs-content').animate({
            marginLeft: 0 - scrollVal + 'px'
        }, "fast");
    }

    //查看左侧隐藏的选项卡
    function scrollTabLeft() {
        var marginLeftVal = Math.abs(parseInt($('.page-tabs-content').css('margin-left')));
        // 可视区域非tab宽度
        var tabOuterWidth = calSumWidth($(".content-tabs").children().not(".J_menuTabs"));
        //可视区域tab宽度
        var visibleWidth = $(".content-tabs").outerWidth(true) - tabOuterWidth;
        //实际滚动宽度
        var scrollVal = 0;
        if ($(".page-tabs-content").width() < visibleWidth) {
            return false;
        } else {
            var tabElement = $(".J_menuTab:first");
            var offsetVal = 0;
            while ((offsetVal + $(tabElement).outerWidth(true)) <= marginLeftVal) {//找到离当前tab最近的元素
                offsetVal += $(tabElement).outerWidth(true);
                tabElement = $(tabElement).next();
            }
            offsetVal = 0;
            if (calSumWidth($(tabElement).prevAll()) > visibleWidth) {
                while ((offsetVal + $(tabElement).outerWidth(true)) < (visibleWidth) && tabElement.length > 0) {
                    offsetVal += $(tabElement).outerWidth(true);
                    tabElement = $(tabElement).prev();
                }
                scrollVal = calSumWidth($(tabElement).prevAll());
            }
        }
        $('.page-tabs-content').animate({
            marginLeft: 0 - scrollVal + 'px'
        }, "fast");
    }

    //查看右侧隐藏的选项卡
    function scrollTabRight() {
        var marginLeftVal = Math.abs(parseInt($('.page-tabs-content').css('margin-left')));
        // 可视区域非tab宽度
        var tabOuterWidth = calSumWidth($(".content-tabs").children().not(".J_menuTabs"));
        //可视区域tab宽度
        var visibleWidth = $(".content-tabs").outerWidth(true) - tabOuterWidth;
        //实际滚动宽度
        var scrollVal = 0;
        if ($(".page-tabs-content").width() < visibleWidth) {
            return false;
        } else {
            var tabElement = $(".J_menuTab:first");
            var offsetVal = 0;
            while ((offsetVal + $(tabElement).outerWidth(true)) <= marginLeftVal) {//找到离当前tab最近的元素
                offsetVal += $(tabElement).outerWidth(true);
                tabElement = $(tabElement).next();
            }
            offsetVal = 0;
            while ((offsetVal + $(tabElement).outerWidth(true)) < (visibleWidth) && tabElement.length > 0) {
                offsetVal += $(tabElement).outerWidth(true);
                tabElement = $(tabElement).next();
            }
            scrollVal = calSumWidth($(tabElement).prevAll());
            if (scrollVal > 0) {
                $('.page-tabs-content').animate({
                    marginLeft: 0 - scrollVal + 'px'
                }, "fast");
            }
        }
    }

    function menuItem() {
        // 获取标识数据
        var dataUrl = $(this).attr('href'),
            dataIndex = $(this).data('index'),
            menuName = $.trim($(this).find("span.nav-label").text());
        rememberTab(dataUrl);
        openTab(dataUrl, menuName, dataIndex);
        return false;
    }

    var openTab = function (dataUrl, menuName, dataIndex) {
        if (dataUrl == undefined || $.trim(dataUrl).length == 0) return;
        dataIndex = dataIndex == null ? new Date().getTime() : dataIndex;
        var flag = true;
        // 选项卡菜单已存在
        $('.J_menuTab').each(function () {
            if ($(this).data('id') == dataUrl) {
                if (!$(this).hasClass('active')) {
                    $(this).addClass('active').siblings('.J_menuTab').removeClass('active');
                    scrollToTab(this);
                    // 显示tab对应的内容区
                    $('.J_mainContent .J_iframe').each(function () {
                        if ($(this).data('id') == dataUrl) {
                            $(this).show().siblings('.J_iframe').hide();
                            return;
                        }
                    });
                }
                flag = false;
                return;
            }
        });

        // 选项卡菜单不存在
        if (flag) {
            var str = '<a href="javascript:;" class="active J_menuTab" data-id="' + dataUrl + '">' + menuName + ' <i class="fa fa-times-circle"></i></a>';
            $('.J_menuTab').removeClass('active');

            // 添加选项卡对应的iframe
            var str1 = '<iframe class="J_iframe" name="iframe' + dataIndex + '" width="100%" height="100%" src="' + dataUrl + '" frameborder="0" data-id="' + dataUrl + '" seamless></iframe>';
            $('.J_mainContent').find('iframe.J_iframe').hide().parents('.J_mainContent').append(str1);

            //显示loading提示
            var loading = layer.load();

            $('.J_mainContent iframe:visible').load(function () {
                //iframe加载完成后隐藏loading提示
                layer.close(loading);
            });
            // 添加选项卡
            $('.J_menuTabs .page-tabs-content').append(str);
            scrollToTab($('.J_menuTab.active'));
        }
        return;

    };

    openRememberTab();

    /**
     * 打开上次记住的标签页
     */
    function openRememberTab() {
        if (Util.sessionStorageSupport) {
            var dataUrl = sessionStorage.getItem('menu_href');
            if (dataUrl) {
                $('.J_menuItem').each(function (i, v) {
                    var url = $(this).attr("href");
                    if (url === dataUrl) {
                        var url = $(this).attr('href'),
                            dataIndex = $(this).data('index'),
                            menuName = $.trim($(this).find("span.nav-label").text());
                        openTab(url, menuName, dataIndex);

                        //自动展开菜单
                        var $menu = $("#side-menu");
                        var menu = $menu.data("mm");
                        var items = $(this).parents("#side-menu ul").prev();
                        for (var j = 0; j < items.length; j++) {
                            var self = $(items[items.length - j - 1]);
                            var $parent = self.parent('li');
                            var $list = $parent.children('ul');
                            menu.show($list);
                            menu.transitioning = 0;
                            self.attr('aria-expanded', true);
                        }
                        return false;
                    }
                });
            }
        }
    }

    /**
     * 记住当前打开的标签页
     * @param dataUrl
     * @param dataIndex
     * @param menuName
     */
    function rememberTab(dataUrl) {
        if (Util.sessionStorageSupport) {
            sessionStorage.setItem('menu_href', dataUrl);
        }
    }

    $('.J_menuItem').on('click', menuItem);

    // 关闭选项卡菜单
    function closeTab() {
        var closeTabId = $(this).parents('.J_menuTab').data('id');
        var currentWidth = $(this).parents('.J_menuTab').width();
        Util.removeLastTab();

        // 当前元素处于活动状态
        if ($(this).parents('.J_menuTab').hasClass('active')) {

            // 当前元素后面有同辈元素，使后面的一个元素处于活动状态
            if ($(this).parents('.J_menuTab').next('.J_menuTab').size()) {

                var activeId = $(this).parents('.J_menuTab').next('.J_menuTab:eq(0)').data('id');
                $(this).parents('.J_menuTab').next('.J_menuTab:eq(0)').addClass('active');

                $('.J_mainContent .J_iframe').each(function () {
                    if ($(this).data('id') == activeId) {
                        $(this).show().siblings('.J_iframe').hide();
                        rememberTab(activeId);
                        return false;
                    }
                });

                var marginLeftVal = parseInt($('.page-tabs-content').css('margin-left'));
                if (marginLeftVal < 0) {
                    $('.page-tabs-content').animate({
                        marginLeft: (marginLeftVal + currentWidth) + 'px'
                    }, "fast");
                }

                //  移除当前选项卡
                $(this).parents('.J_menuTab').remove();

                // 移除tab对应的内容区
                $('.J_mainContent .J_iframe').each(function () {
                    if ($(this).data('id') == closeTabId) {
                        $(this).remove();
                        return false;
                    }
                });
            }

            // 当前元素后面没有同辈元素，使当前元素的上一个元素处于活动状态
            if ($(this).parents('.J_menuTab').prev('.J_menuTab').size()) {
                var activeId = $(this).parents('.J_menuTab').prev('.J_menuTab:last').data('id');
                $(this).parents('.J_menuTab').prev('.J_menuTab:last').addClass('active');
                $('.J_mainContent .J_iframe').each(function () {
                    if ($(this).data('id') == activeId) {
                        rememberTab(activeId);
                        $(this).show().siblings('.J_iframe').hide();
                        return false;
                    }
                });

                //  移除当前选项卡
                $(this).parents('.J_menuTab').remove();

                // 移除tab对应的内容区
                $('.J_mainContent .J_iframe').each(function () {
                    if ($(this).data('id') == closeTabId) {
                        $(this).remove();
                        return false;
                    }
                });
            }
        } else {
            // 当前元素不处于活动状态
            //  移除当前选项卡
            $(this).parents('.J_menuTab').remove();

            // 移除相应tab对应的内容区
            $('.J_mainContent .J_iframe').each(function () {
                if ($(this).data('id') == closeTabId) {
                    $(this).remove();
                    return false;
                }
            });
            scrollToTab($('.J_menuTab.active'));
        }
        return false;
    }

    // 点击用户名下拉效果
    $("#user-dropdown-toggle").hover(function () {
    }, function () {
        $("#user-dropdown-menu").hide();
    }).click(function () {
        $("#user-dropdown-menu").show();
    });
    $("#user-dropdown-menu").hover(function () {
        $(this).show();
    }, function () {
        $(this).hide();
    });

    //个人信息
    $("#btn-personalinfo").click(function () {
        var dataUrl = Util.getPath() + "/user/personalinfo",
            dataIndex = "999",
            menuName = $.trim($(this).text());
        rememberTab(dataUrl);
        openTab(dataUrl, menuName, dataIndex);
    });

    // 修改密码
    $("#form-changepasswoprd").validate({
        messages: {
            oldPassword: {
                required: "请输入原始密码"
            },
            password: {
                required: "请输入密码",
                minlength: "密码长度不能小于{0}位",
                maxlength: "密码长度不能大于{0}位"
            },
            confirmpassword: {
                required: "请输入密码",
                equalTo: "两次密码输入不一致"
            }
        }
    });
    $("#btn-changepassword").click(function () {
        var id = $(this).attr("data-id");
        top.layer.open({
            type: 1,
            content: $("#changepasswoprd"),
            btn: ['确定', '取消'],
            yes: function (index, layero) {
                if ($("#form-changepasswoprd").valid()) {
                    var password = layero.find("#password").val();
                    var oldPassword = layero.find("#oldPassword").val();
                    $.post(Util.getPath() + "/user/mypassword/change", {
                        oldPassword: oldPassword,
                        password: password
                    }, function (res) {
                        if (res.code === 200) {
                            Util.closeLayer(index);
                            top.layer.alert("密码修改成功，请重新登录！", {
                                icon: 3,
                                title: '系统提示',
                                closeBtn: false
                            }, function (index) {
                                location.href = Util.getRootPath() + "/logout";
                            });
                        } else {
                            Util.msgError(res.errorMsg ? res.errorMsg : "修改密码失败！");
                        }
                    }, "JSON");
                } else {
                    $('#form-changepasswoprd').validate().focusInvalid();
                }
            }
        });
    });

    // 点击关闭操作
    $("#J_tabClose").hover(function () {
        $("#dropdown-menu-right").show();
    }, function () {
        $("#dropdown-menu-right").hide();
    });
    $("#dropdown-menu-right").hover(function () {
        $(this).show();
    }, function () {
        $(this).hide();
    });

    //关闭其他选项卡
    $('.J_tabCloseOther').on('click', function () {
        $('.page-tabs-content').children("[data-id]").not(":first").not(".active").each(function () {
            $('.J_iframe[data-id="' + $(this).data('id') + '"]').remove();
            $(this).remove();
        });
        $('.page-tabs-content').css("margin-left", "0");
    });

    //滚动到已激活的选项卡
    $('.J_tabShowActive').on('click', function () {
        scrollToTab($('.J_menuTab.active'));
    });

    // 点击选项卡菜单
    $('.J_menuTabs').on('click', '.J_menuTab', function () {
        if (!$(this).hasClass('active')) {
            var currentId = $(this).data('id');
            // 显示tab对应的内容区
            rememberTab(currentId);
            $('.J_mainContent .J_iframe').each(function () {
                if ($(this).data('id') == currentId) {
                    $(this).show().siblings('.J_iframe').hide();
                    return false;
                }
            });
            $(this).addClass('active').siblings('.J_menuTab').removeClass('active');
            scrollToTab(this);
        }
    }).on('click', '.J_menuTab i', closeTab);

    // 左移按扭
    $('.J_tabLeft').on('click', scrollTabLeft);

    // 右移按扭
    $('.J_tabRight').on('click', scrollTabRight);

    // 关闭全部
    $('.J_tabCloseAll').on('click', function () {
        Util.removeLastTab();
        $('.page-tabs-content').children("[data-id]").not(":first").each(function () {
            $('.J_iframe[data-id="' + $(this).data('id') + '"]').remove();
            $(this).remove();
        });
        $('.page-tabs-content').children("[data-id]:first").each(function () {
            $('.J_iframe[data-id="' + $(this).data('id') + '"]').show();
            $(this).addClass("active");
        });
        $('.page-tabs-content').css("margin-left", "0");
    });


    window.openTab = openTab;
    window.closeCurrentTab = function () {
        // 找到当前活跃的标签，然后点击关闭
        $('.J_menuTabs').find(".J_menuTab.active i").click();
    };
});
