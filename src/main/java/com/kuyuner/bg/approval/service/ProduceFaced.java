package com.kuyuner.bg.approval.service;

import com.kuyuner.bg.approval.entity.Leave;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;

/**
 * 请假Service层接口
 *
 * @author administrator
 */
public interface ProduceFaced {


    /**
     * 查询待办事项
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageJson findPendingList(String pageNum, String pageSize, String senderName,String businessName,String leaveType, String userId);

    /**
     * 查询历史审批
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageJson findHistoricList(String pageNum, String pageSize, String senderName,String businessName,String leaveType, String userId);

    /**
     * 查询申请历史
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageJson findSendList(String pageNum, String pageSize, String senderName,String businessName,String leaveType, String userId);

    String getCode();

    /**
     * 提交
     * @param body
     * @return
     */
    ResultJson submit(String body,String taskResult,String userId,String goods, String modelKey);

    /**
     * 审批
     * @param id
     * @return
     */
    ResultJson approvalForm(String id, String approvalResult, String taskResult,String userId);

    /**
     * 获取任务详情信息
     * @param taskId
     * @param userId
     * @return
     */
    ResultJson detail(String produceType,String businessId, String firstTask, String type,String taskId,
                      String modelKey, String startSequenceFlowName,String userId);
}