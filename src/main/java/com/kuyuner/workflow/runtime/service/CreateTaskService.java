package com.kuyuner.workflow.runtime.service;

import com.kuyuner.common.controller.ListJson;
import com.kuyuner.common.controller.ResultJson;

/**
 * 创建任务
 *
 * @author tangzj
 */
public interface CreateTaskService {

    /**
     * 查询可创建的流程定义（最新的流程定义）
     *
     * @return
     */
    ListJson findCreatableTasks();

    /**
     * 获得开始任务的表单
     *
     * @param modelKey
     * @param startSequenceFlowName
     * @return
     */
    ResultJson getStartFormPath(String modelKey, String startSequenceFlowName);


    /**
     * 查询下一节点候选人
     *
     * @param processDefinitionId
     * @param searchText
     * @param startSequenceFlowName
     * @param sequenceFlowName
     * @return
     */
    ListJson findNextUserTaskCandidateInfos(String processDefinitionId, String searchText, String startSequenceFlowName, String sequenceFlowName);

}
