package com.kuyuner.workflow.historic.service;

import com.kuyuner.common.controller.ListJson;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;

/**
 * 历史任务
 *
 * @author tangzj
 */
public interface HistoricTaskService {

    /**
     * 查询历史任务
     *
     * @param userId
     * @param processDefinitionName
     * @param personName
     * @param pageNum
     * @param pageSize
     * @param modelKey
     * @param taskType              表示是提交申请还是审批的任务
     * @return
     */
    PageJson findHistoricTask(String userId, String processDefinitionName, String personName, String pageNum,
                              String pageSize, String modelKey, String taskType);

    /**
     * 根据历史任务ID获得任务表单及相关任务信息
     *
     * @param taskId
     * @return
     */
    ResultJson getDefaultFormPath(String taskId);

    /**
     * 根据历史流程实例ID，查询历史任务轨迹
     *
     * @param processInstanceId
     * @return
     */
    ListJson findHistoricTrackInfo(String processInstanceId);

    /**
     * 查询历史环节
     *
     * @param processInstanceId
     * @return
     */
    ResultJson findHistoricTaskInfo(String processInstanceId);

}
