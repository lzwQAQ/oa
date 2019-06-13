$(function () {

    $("#search .select2").select2({
        width: "200px"
    });

    var gridTable = $('#table').jqGrid({
        url: Util.getPath() + "/receivedocument2/favorites/list",
        rownumbers: true,
        colModel: [{
            label: '标题',
            name: 'title',
            formatter: function (value, options, row) {
                return '<a href="javascript:void(0)" class="edit-item" data-id="' + row.id + '">' + value + '</a>';
            }
        }, {
            label: '发文编号',
            name: 'numbers'
        }, {
            label: '发文机构',
            name: 'releaseOrgName'
        }, {
            label: '密级',
            name: 'secret'
        }, {
            label: '紧急程度',
            name: 'urgentLevel'
        }, {
            label: '收文时间',
            name: 'receiveDate',
            width: 160,
            fixed: true
        }, {
            label: "操作",
            name: "id",
            width: 100,
            fixed: true,
            formatter: function (value, options, row) {
                var actions = [];
                actions.push('<a href="javascript:void(0)" class="actions star-item" data-star="' + row.star + '" data-id="' + row.id + '" title="收藏"><i class="fa ' + (row.star === '1' ? 'fa-star' : 'fa-star-o') + '"></i></a>');
                return actions.join("");
            }
        }]
    }).enableAutoHeight(".content", ".other-items");

    // 编辑
    $("#table").on("click", ".edit-item", function () {
        var id = $(this).attr("data-id");
        Util.innerIframe(Util.getPath() + "/receivedocument2/detail/" + id);
    }).on("click", ".star-item", function () {
        var id = $(this).attr("data-id");
        var star = $(this).attr("data-star");
        $.post(Util.getPath() + "/receivedocument2/star/" + id, {
            "star": '1' === star ? '0' : '1'
        }, function (res) {
            if (res.code === 200) {
                Util.msgOk(res.data);
                gridTable.trigger("reloadGrid");
            }
        }, "json");
    });

    //查询
    $("#btn-search").click(function () {
        gridTable.setGridParam({
            postData: {
                title: $("#title").val(),
                urgentLevel: $("#urgentLevel").val(),
                secret: $("#secret").val()
            }
        }).trigger("reloadGrid", {
            page: 1
        });
    });

    //重置
    $("#btn-reset").click(function () {
        $("#search input").val("");
        $("#search select").val("").trigger("change");
        $("#btn-search").click();
    });
});