$(function () {

    var pendingFirstTask = (Util.getQueryString("firstTask") === "true") && Util.getQueryString("type") === "pending";

    $("#form").validate();
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

    $("#btnSubmit").click(function () {
        if (!checkGoodsList()) {
            return;
        }

        if ($("#form").valid()) {
            parent.beforeSubmitForm($("#auditResult").val(), function (taskResult) {
                var goods = collectGoodsData();
                goods = JSON.stringify(goods);
                $("#form").ajaxSubmit({
                    loading: true,
                    type: "post",
                    url: Util.getPath() + "/purchase/submit",
                    data: {
                        taskResult: taskResult,
                        goods: goods,
                        totalPrice: $("#price-num").text()
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

    $("#goods").on("click", "td.enable-edit", function () {
        var $this = $(this).removeClass("enable-edit danger");
        var text = $this.text();
        if ($this.attr("data-placeholder-flag") === 'true') {
            text = "";
        }

        $this.empty();
        if ($this.hasClass("select")) {
            var $select = $($("#unit-div").html());
            $select.find('option[value="' + text + '"]').attr("selected", true);
            $select.appendTo($this).focus();
        } else {
            $('<input style="width: 100%" type="text" class="inner-edit" value="' + text + '">').appendTo($this).focus();
        }
        $this.attr("data-value", text);
    }).on("blur", ".inner-edit", function () {
        var $this = $(this);
        var value = $this.val();
        var $td = $this.parent("td");
        var f = checkData($td.attr("data-check"), value);
        value = f ? value : $td.attr("data-value");
        var placeholder = $td.attr("data-placeholder");

        $td.empty().append(value ? value : placeholder).addClass("enable-edit");
        $td.attr("data-placeholder-flag", value ? "false" : "true");

        //计算总价和列的价格
        if ($td.hasClass("calculation")) {
            calculation();
        }
    }).on("keyup", ".inner-edit", function (event) {
        if (event.keyCode === 13) {
            $(this).blur();
        }
    }).on("click", ".add", function () {
        $(this).parents("tr").after($("#goods-templete").find("tbody").html());
    }).on("click", ".delete", function () {
        $(this).parents("tr").remove();
        calculation();
    });

    function checkData(dataType, value) {
        if (!dataType) {
            return true;
        }
        if (dataType === 'integer') {
            var f = /^[1-9]\d*$/.test(value);
            if (!f) {
                Util.msgWarning("只能输入正整数！");
            }
            f = value / 1 <= 99999999;
            if (!f) {
                Util.msgWarning("数量不能大于99999999！");
            }
            return f;
        }
        if (dataType === 'num') {
            var f = /^[1-9]\d*\.[0-9]{1,2}$|0\.[0-9]{1,2}$|^[0-9]\d*$/.test(value);
            if (!f) {
                Util.msgWarning("只能输入数字，小数点最多保留2位！");
            }
            f = value / 1 <= 99999999;
            if (!f) {
                Util.msgWarning("单价不能大于99999999！");
            }
            return f;
        }
        if (dataType === 'max') {
            if (value.length > 50) {
                Util.msgWarning("长度不能大于50！");
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * 计算物品各个价格
     */
    function calculation() {
        $("#goods").find("tr").each(function (i, v) {
            var calculations = $(this).find(".calculation");
            var total = ($(calculations[0]).text() * $(calculations[1]).text()).toFixed(2);
            $(this).find(".price-sum").text(isNaN(total) ? 0 : total);
        });

        var sum = 0;
        $("#goods").find(".price-sum").each(function (i, v) {
            sum += ($(this).text() / 1);
        });
        $("#price-num").text(sum.toFixed(2));
    }

    /**
     * 检查商品列表
     */
    function checkGoodsList() {
        var flag = true;
        $("#goods tbody td.danger").removeClass("danger");
        $("#goods tbody td").each(function (i, v) {
            var $this = $(this);
            var f = $this.attr("data-placeholder-flag") === 'true';
            if (f) {
                $this.addClass("danger");
                Util.msgWarning("该数据项不能为空！");
                return flag = false;
            }

            if ($this.hasClass("calculation") && $this.text() / 1 <= 0) {
                $this.addClass("danger");
                Util.msgWarning("单价和数量必须大于0！");
                return flag = false;
            }

        });
        return flag;
    }

    /**
     *收集商品信息
     */
    function collectGoodsData() {
        var goods = [];
        $("#goods tbody tr").each(function (i, v) {
            var $this = $(this);
            goods.push({
                name: $this.find(".name").text(),
                model: $this.find(".model").text(),
                price: $this.find(".price").text(),
                num: $this.find(".num").text(),
                unit: $this.find(".select").text(),
                totalPrice: $this.find(".price-sum").text()
            });
        });
        return goods;
    }
});