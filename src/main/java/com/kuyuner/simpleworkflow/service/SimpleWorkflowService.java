package com.kuyuner.simpleworkflow.service;

import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.simpleworkflow.entity.SimpleWorkflow;

/**
 * 简单流程Service层接口
 *
 * @author administrator
 */
public interface SimpleWorkflowService {

    /**
     * 查询唯一
     *
     * @param id
     * @return
     */
    SimpleWorkflow get(String id);

    /**
     * 插入数据
     *
     * @param simpleWorkflow
     * @param taskResult
     * @param modelKey
     * @return
     */
    ResultJson submitForm(SimpleWorkflow simpleWorkflow, String taskResult, String modelKey);

    /**
     * 删除数据
     *
     * @param ids
     */
    ResultJson deletes(String... ids);

    /**
     * 查询待办事项
     *
     * @param pageNum
     * @param pageSize
     * @param simpleWorkflow
     * @return
     */
    PageJson findPendingList(String pageNum, String pageSize, SimpleWorkflow simpleWorkflow);

    /**
     * 查询审批历史
     *
     * @param pageNum
     * @param pageSize
     * @param simpleWorkflow
     * @return
     */
    PageJson findHistoricList(String pageNum, String pageSize, SimpleWorkflow simpleWorkflow);

    /**
     * 查询申请历史
     *
     * @param pageNum
     * @param pageSize
     * @param simpleWorkflow
     * @return
     */
    PageJson findSendList(String pageNum, String pageSize, SimpleWorkflow simpleWorkflow);

    /**
     * 查询历史数据
     *
     * @param businessLogId
     * @return
     */
    SimpleWorkflow getLog(String businessLogId);

    /**
     * 审批
     *
     *
     * @param id
     * @param approvalResult
     * @param taskResult
     * @param modelKey
     * @return
     */
    ResultJson approvalForm(String id, String approvalResult, String taskResult, String modelKey);
}