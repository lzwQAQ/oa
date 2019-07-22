package com.kuyuner.workflow.runtime.service.impl;

import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.common.lang.StringUtils;
import com.kuyuner.workflow.runtime.service.CreateTaskService;
import com.kuyuner.workflow.runtime.service.PendingTaskService;
import com.kuyuner.workflow.runtime.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 创建任务
 *
 * @author tangzj
 */
@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private PendingTaskService pendingTaskService;

    @Autowired
    private CreateTaskService createTaskService;



    @Override
    public ResultJson getFormPath(String modelKey, String startSequenceFlowName, String taskId) {
        ResultJson result = new ResultJson();
        if (StringUtils.isNotBlank(taskId) && !"null".equals(taskId)) {
            result = pendingTaskService.getDefaultFormPath(taskId);
        }
        if (StringUtils.isNotBlank(modelKey) && (StringUtils.isBlank(taskId) || "null".equals(taskId))){
            result = createTaskService.getStartFormPath(modelKey, startSequenceFlowName);
        }
        return result;
    }
}
