/*
 * Assignment
 */
var KisBpmAssignmentCtrl = ['$scope', '$modal', function ($scope, $modal) {

    //将属性重新设置成读属性
    $scope.property.mode = 'read';
    var taskKey = $scope.selectedItem.properties[0].value;
    var modelKey = $scope.modelData.model.properties.process_id;

    var selected = {"role": [], "dept": [], "org": []};

    jQuery.post(Util.getPath() + '/establish/findTaskAuth', {
        modelKey: modelKey,
        taskKey: taskKey
    }, function (res) {
        if (res.code === 200) {
            jQuery.each(res.data, function () {
                selected[this.type].push(this.id);
            });
            initBadgeCount();
            renderTable('role', Util.getPath() + "/establish/findRole", [
                {checkbox: true},
                {field: 'roleName', title: '角色名称'},
                {field: 'roleDes', title: '角色描述'}
            ]);
            openAssigneeSettingTabs();
            return;
        }
        layer.msg('查询任务权限失败！', {icon: 2, time: 1500});
    });

    function openAssigneeSettingTabs() {
        var renderedTables = {};

        layer.open({
            type: 1,
            content: jQuery("#assignee-setting-tab"),
            title: "任务分配",
            area: ['800px', 'auto'],
            resize: false,
            btn: ['保存', '取消'],
            yes: function (index, layero) {
                jQuery.post(Util.getPath() + '/establish/saveFormAuth', {
                    modelKey: modelKey,
                    taskKey: taskKey,
                    roles: selected['role'].join(","),
                    depts: selected['dept'].join(","),
                    orgs: selected['org'].join(",")
                }, function (res) {
                    if (res.code === 200) {
                        layer.msg('保存成功！', {icon: 1, time: 1000});
                        layer.close(index);
                        return;
                    }
                    layer.msg('保存失败！', {icon: 2, time: 1500});
                });
            },
            end: function () {
                // 弹出框销毁的时候，让tab标签页切换到第一个；清空输入框内容；销毁bootstrap-table，防止缓存问题
                layui.element.tabChange('assignee-setting-tab-filter', 'assignee-setting-tab-first');
                jQuery("#search-content-role").val("");
                jQuery("#search-content-dept").val("");
                jQuery("#search-content-org").val("");
                jQuery('#table-role').bootstrapTable('destroy');
                jQuery('#table-dept').bootstrapTable('destroy');
                jQuery('#table-org').bootstrapTable('destroy');
            }
        });

        layui.element.on('tab(assignee-setting-tab-filter)', function (data) {
            if (!isRendered(data.index) && data.index === 1) {
                renderTable('dept', Util.getPath() + "/establish/findDept", [
                    {checkbox: true},
                    {field: 'deptCode', title: '部门编码'},
                    {field: 'deptName', title: '部门名称'},
                    {field: 'orgName', title: '所属机构'}
                ]);
            } else if (!isRendered(data.index) && data.index === 2) {
                renderTable('org', Util.getPath() + "/establish/findOrg", [
                    {checkbox: true},
                    {field: 'orgCode', title: '机构编码'},
                    {field: 'orgName', title: '机构名称'}
                ]);
            }
            renderedTables[data.index + ""] = data.index;
        });

        function isRendered(index) {
            return renderedTables[index + ""] != null;
        }
    }

    /**
     * 初始化tab切换页的提示数量
     * @param rows
     */
    function initBadgeCount() {
        jQuery("#badge-count-role").text(selected['role'].length > 99 ? '99+' : selected['role'].length);
        jQuery("#badge-count-dept").text(selected['dept'].length > 99 ? '99+' : selected['dept'].length);
        jQuery("#badge-count-org").text(selected['org'].length > 99 ? '99+' : selected['org'].length);
    }

    /**
     * 渲染列表，绑定查询按钮事件;已经渲染过的列表会重新刷新数据
     * @param type 列表类型：role,dept,org
     * @param url
     * @param columns
     * @param auths 已选择权限
     */
    function renderTable(type, url, columns) {
        var table = jQuery('#table-' + type).bootstrapGrid({
            url: url,
            uniqueId: 'id',
            pagination: false,
            height: 320,
            columns: columns,
            onLoadSuccess: function (res) {
                var ids = selected[type];
                if (ids && res.data) {
                    for (var i = 0; i < ids.length; i++) {
                        var id = ids[i];
                        for (var j = 0; j < res.data.length; j++) {
                            if (id === res.data[j].id) {
                                table.table('checkBy', {field: "id", values: [id]});
                                continue;
                            }
                        }
                    }
                }
            },
            onCheckAll: function (rows) {
                checkChange(rows, rows.length);
            },
            onUncheckAll: function (rows) {
                checkChange(rows, 0);
            },
            onCheck: function (row) {
                checkChange([row], 1);
            },
            onUncheck: function (row) {
                checkChange([row], -1);

            }
        });
        jQuery("#search-" + type).unbind("click").click(function () {
            table.resetSearch({
                searchText: jQuery("#search-content-" + type).val()
            });
        });

        /**
         * 勾选状态发生变化时，修改记录的数据和界面显示的数量
         * @param rows
         * @param flag
         */
        function checkChange(rows, flag) {
            jQuery.each(rows, function (i, v) {
                if (flag > 0) {
                    if (jQuery.inArray(v.id, selected[type]) === -1) {
                        selected[type].push(v.id);
                    }
                } else {
                    removeFromArray(selected[type], v.id);
                }
            });
            jQuery("#badge-count-" + type).text(selected[type].length > 99 ? '99+' : selected[type].length);
        }
    }

    /**
     * 从数组中删除指定对象
     * @param array
     * @param obj
     */
    function removeFromArray(array, obj) {
        var arrayTemp = [];
        jQuery.each(array, function (i, v) {
            if (v === obj) {
                return;
            }
            arrayTemp.push(v);
        });
        array.splice(0, array.length);
        jQuery.each(arrayTemp, function (i, v) {
            array.push(v);
        });
    }
}];