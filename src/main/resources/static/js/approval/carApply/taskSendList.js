$(function () {

    $("#search select").select2({
        width: "200px"
    });

    var gridTable = $('#table').jqGrid({
        url: Util.getPath() + "/carapply/send/list",
        rownumbers: true,
        colModel: [{
            label: '申请车辆',
            name: 'car.carNo',
            width: 120,
            fixed: true,
            formatter: function (value, options, row) {
                if (value ===""){
                    value = "暂无"
                }
                return '<a href="javascript:void(0)" class="edit-item" data-id="' + row.id + '">' + value + '</a>';
            }
        },{
            label: '人数',
            name: 'people'
        }, {
            label: '地点',
            name: 'address'
        }, {
            label: '品牌型号',
            name: 'car.brand',
            formatter: function (value, options, row) {
                return "" === value ? '暂无' : value;
            }
        }, {
            label: '座位数',
            name: 'car.seatNum',
            formatter: function (value, options, row) {
                return "" === value ? '暂无' : value;
            }
        }, {
            label: '驾驶员',
            name: 'driver',
            formatter: function (value, options, row) {
                return "" === value ? '暂无' : value;
            }
        }, {
            label: '联系电话',
            name: 'phone',
            formatter: function (value, options, row) {
                return "" === value ? '暂无' : value;
            }
        },{
            label: '所属流程',
            name: 'processName'
        }, {
            label: '当前环节',
            name: 'taskName'
        }, {
            label: '审批状态',
            name: 'approvalOpinion',
            formatter: function (value, options, row) {
                if('结束' === row.taskName){
                    value = value === "不同意"?"不通过":"通过"
                }
                return '结束' === row.taskName ? value : ("用车申请" === row.taskName ? '不通过' : '审批中');
            }
        }, {
            label: '申请时间',
            name: 'time',
            width: 160,
            fixed: true
        }, {
            name: 'processInstanceId',
            hidden: true
        }]
    }).enableAutoHeight(".content", ".other-items");

    // 编辑
    $("#table").on("click", ".edit-item", function () {
        var row = $("#table").jqGrid('getRowData', $(this).attr("data-id"));
        Util.fullIframe({
            title: '流程名称：' + row.processName,
            content: Util.getPath() + '/historictask/details?processInstanceId=' + row.processInstanceId
        });
    });

    //查询
    $("#btn-search").click(function () {
        gridTable.setGridParam({
            postData: {
                "car.carNo": $("#carNo").val(),
                type: $("#type").val()
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

function reloadGrid() {
    $('#table').trigger("reloadGrid");
}