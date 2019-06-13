(function ($) {
    var BootstrapTable = $.fn.bootstrapTable.Constructor,
        _updateSelected = BootstrapTable.prototype.updateSelected,
        _checkAll_ = BootstrapTable.prototype.checkAll_,
        _onPagePre = BootstrapTable.prototype.onPagePre,
        _onPageNext = BootstrapTable.prototype.onPageNext,
        _onPageNumber = BootstrapTable.prototype.onPageNumber;

    BootstrapTable.prototype.updateSelected = function () {
        _updateSelected.apply(this, Array.prototype.slice.apply(arguments));
        var checkAll = this.$selectAll.prop("checked");
        this.$selectAll.add(this.$selectAll_).closest("th")[checkAll ? "addClass" : "removeClass"]("selected");
    };
    BootstrapTable.prototype.checkAll_ = function () {
        _checkAll_.apply(this, Array.prototype.slice.apply(arguments));
        this.updateSelected();
    };

    /*处理bootstraptable的分页按钮点击bug  开始*/
    BootstrapTable.prototype.onPagePre = function (event) {
        event.preventDefault();

        var isDisabled = $(event.currentTarget).hasClass("disabled");
        if (isDisabled === true) {
            return;
        }

        _onPagePre.apply(this, Array.prototype.slice.apply(arguments));
    };
    BootstrapTable.prototype.onPageNext = function (event) {
        event.preventDefault();

        var isDisabled = $(event.currentTarget).hasClass("disabled");
        if (isDisabled === true) {
            return;
        }

        _onPageNext.apply(this, Array.prototype.slice.apply(arguments));
    };
    BootstrapTable.prototype.onPageNumber = function (event) {
        event.preventDefault();

        var isDisabled = $(event.currentTarget).hasClass("disabled");
        if (isDisabled === true) {
            return;
        }

        _onPageNumber.apply(this, Array.prototype.slice.apply(arguments));
    };
    /*处理bootstraptable的分页按钮点击bug  结束*/


    $.fn.bootstrapGrid = function (options) {
        var $this = $(this);
        var opt = $.extend({}, {
            extraHeight: 0,
            undefinedText: '',
            classes: 'table table-hover',
            queryParamsType: "limit",
            contentType: "application/x-www-form-urlencoded",
            method: "post",
            dataField: "data",
            totalField: "total",
            sidePagination: "server",
            checkboxHeader: false,//不显示自带的checkbox标题
            pagination: true,
            paginationLoop: false,
            paginationNextText: "下一页",
            paginationPreText: "上一页",
            pageSize: 15,
            pageList: [15, 30, 50, 100, 300],
            smartDisplay: true,//true：是否自动判断显示分页信息和card 视图；false：一直显示分页视图
            responseHandler: function (res) {
                var total = res.total;
                if (total == null || total === "" || total === undefined || total <= 0) {
                    // 仅有0条数据的时候，不使用智能展示，不显示分页
                    this.smartDisplay = true;
                } else {
                    // 只要有数据，一直显示分页，禁用智能展示
                    this.smartDisplay = false;
                }
                return {
                    total: total,
                    data: res.data
                };
            },
            queryParams: function (params) {
                return $.extend({}, this.querys, {
                    start: params.offset,
                    limit: params.limit
                });
            },
            formatLoadingMessage: function () {
                return [
                    '<div class="loading-waiting">',
                    '<img src=' + '"../img/loading-waiting.gif" />',
                    '正在加载中，请稍后...',
                    '</div>'
                ].join("");
            },
            formatShowingRows: function (pageFrom, pageTo, totalRows) {
                return '共 ' + totalRows + ' 条';
            },
            formatRecordsPerPage: function (pageNumber) {
                return ' 每页显示 ' + pageNumber + ' 条' + ' <div class="pull-right pagination-skip"><span>跳转至&nbsp;</span><span class="pull-right">&nbsp;页</span><input type="text" min="1" class="form-control input-sm pull-right pagination-num"></div>';
            },
            formatAllRows: function () {
                return "全部";
            }
        }, options);
        $.each(opt.columns, function () {
            if (this.checkbox === true) {
                $.extend(this, {
                    width: 36,
                    title: '<input name="btSelectAll" type="checkbox" class="bt-select-all"><div class="cus-checkbox"><i class="fa fa-check-square"></i><i class="fa fa-square-o"></i></div>',
                    showSelectTitle: true,
                    align: 'center',
                    field: true,
                    formatter: function (value, row, index) {
                        return '<div class="cus-checkbox"><i class="fa fa-check-square"></i><i class="fa fa-square-o"></i></div>';
                    }
                });
            }
        });
        var table = $this.bootstrapTable(opt);

        /*resetTableView(table, opt.extraHeight);
        var resizeFunc = function () {
            resetTableView(table, opt.extraHeight);
        };
        $(window).resize(resizeFunc);*/

        var inputTemp = "";
        $this.parents(".bootstrap-table").on("keyup", ".pagination-num", function (event) {
            if (event.keyCode == "13") {
                var num = $(this).val();
                var reg = /^[1-9]\d*$/;
                if (reg.test(num)) {
                    table.bootstrapTable("selectPage", parseInt(num));
                }
            }
        }).on("propertychange input", ".pagination-num", function (event) {
            var num = $(this).val();
            var reg = /^[1-9]\d*$/;
            if (num == "" || reg.test(num)) {
                inputTemp = num;
            } else {
                $(this).val(inputTemp);
            }
        });
        return {
            resetSearch: function (param) {
                table.data("bootstrap.table").options.querys = param;
                table.bootstrapTable("refresh", {
                    pageNumber: 1
                });
            },
            table: function (method, param) {
                return table.bootstrapTable(method, param);
            }
        };
    };

    function resetTableView(table, extraHeight) {
        var dynamicHeight = document.documentElement.clientHeight - $(".navbar.navbar-static-top").height() - $(".breadcrumb").height() - $(".footer").height();
        var height = dynamicHeight - 30 - 15 - 30 - extraHeight;
        table.bootstrapTable("resetView", {"height": height})
    }
})($);