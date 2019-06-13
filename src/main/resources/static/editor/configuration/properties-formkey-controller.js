/*
 * Assignment
 */
var KisBpmFormKeyCtrl = ['$scope', '$modal', function ($scope, $modal) {

    //将属性重新设置成读属性
    $scope.property.mode = 'read';
    var taskKey = $scope.selectedItem.properties[0].value;
    var modelKey = $scope.modelData.model.properties.process_id;

    // 这里由于和angularjs结合使用，不能使用$符号
    jQuery.post(Util.getPath() + '/establish/findFormPath', {
        modelKey: modelKey,
        taskKey: taskKey
    }, function (res) {
        if (res.code === 200) {
            var data = res.data;
            jQuery("#formId").val(data.id);
            jQuery("#formPath").val(data.formPath);
            jQuery("#formName").val(data.formName);
            layer.open({
                type: 1,
                content: jQuery("#path-form"),
                title: "任务表单",
                area: ['560px', 'auto'],
                closeBtn: false,
                resize: false,
                btn: ['保存', '取消'],
                yes: function (index, layero) {
                    jQuery.post(Util.getPath() + '/establish/saveFormPath', {
                        id: jQuery("#formId").val(),
                        formName: jQuery("#formName").val(),
                        formPath: jQuery("#formPath").val(),
                        modelKey: modelKey,
                        taskKey: taskKey
                    }, function (res) {
                        if (res.code === 200) {
                            layer.msg('保存成功！', {icon: 1, time: 1000});
                            layer.close(index);
                            return;
                        }
                        layer.msg('保存失败！', {icon: 2, time: 1500});
                    });
                }
            });
        }else{
            layer.msg('获取表单路径出错！', {icon: 2, time: 1500});
        }
    });

}];