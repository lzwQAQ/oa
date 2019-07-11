package com.kuyuner.workflow.runtime.service;

import com.kuyuner.common.controller.ListJson;
import com.kuyuner.common.controller.ResultJson;

/**
 * 创建任务
 *
 * @author tangzj
 */
public interface TaskService {

    /**
     * 获得开始任务的表单
     *
     * @param modelKey
     * @param startSequenceFlowName
     * @return
     */
    ResultJson getFormPath(String modelKey, String startSequenceFlowName,String taskId);

}
