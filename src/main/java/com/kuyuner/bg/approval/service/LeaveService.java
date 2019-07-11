package com.kuyuner.bg.approval.service;

import com.kuyuner.bg.approval.entity.Leave;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;

/**
 * 请假Service层接口
 *
 * @author administrator
 */
public interface LeaveService {

    /**
     * 查询唯一
     *
     * @param id
     * @return
     */
    Leave get(String id);

    /**
     * 删除数据
     *
     * @param ids
     */
    ResultJson deletes(String... ids);

    /**
     * 获得历史数据
     *
     * @param businessLogId
     * @return
     */
    Leave getLog(String businessLogId);

    /**
     * 查询待办事项
     *
     * @param pageNum
     * @param pageSize
     * @param leave
     * @return
     */
    PageJson findPendingList(String pageNum, String pageSize, Leave leave,String userId);

    /**
     * 查询历史审批
     *
     * @param pageNum
     * @param pageSize
     * @param leave
     * @return
     */
    PageJson findHistoricList(String pageNum, String pageSize, Leave leave,String userId);

    /**
     * 查询申请历史
     *
     * @param pageNum
     * @param pageSize
     * @param leave
     * @return
     */
    PageJson findSendList(String pageNum, String pageSize, Leave leave,String userId);

    /**
     * 提交申请
     *
     * @param leave
     * @param taskResult
     * @return
     */
    ResultJson submitForm(Leave leave, String taskResult,String userId);

    /**
     * 审批操作
     *
     * @param id
     * @param approvalResult
     * @param taskResult
     * @return
     */
    ResultJson approvalForm(String id, String approvalResult, String taskResult,String userId);

    void handleForm(Leave leave, String taskResult,String userId);
}