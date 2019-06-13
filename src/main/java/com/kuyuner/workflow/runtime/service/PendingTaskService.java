package com.kuyuner.workflow.runtime.service;

import com.kuyuner.common.controller.ListJson;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;

/**
 * 运行中流程
 *
 * @author tangzj
 */
public interface PendingTaskService {

    /**
     * 根据用户信息查询所有任务
     *
     *
     * @param pageNum
     * @param pageSize
     * @param processDefinitionName
     * @param taskName
     * @param modelKey
     * @return
     */
    PageJson findTask(String pageNum, String pageSize, String processDefinitionName, String taskName, String modelKey);

    /**
     * 获得默认的表单路径及任务相关信息
     *
     * @param taskId
     * @return
     */
    ResultJson getDefaultFormPath(String taskId);

    /**
     * 查询下一个任务的所有候选信息（候选组、候选人、会签人员）
     *
     * @param taskId
     * @param sequenceFlowName
     * @param searchText
     */
    ListJson findNextUserTaskCandidateInfos(String taskId, String sequenceFlowName, String searchText);

    /**
     * 是否可以找到下一环节处理人
     *
     * @param processDefinitionId
     * @param taskKey
     * @param sequenceFlowName
     * @param processInstanceId 可选参数，可为空
     * @return
     */
    ResultJson isSelectNextTaskCandidateInfos(String processDefinitionId, String taskKey, String sequenceFlowName, String processInstanceId);

}
