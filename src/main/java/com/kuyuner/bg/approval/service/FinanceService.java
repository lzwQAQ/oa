package com.kuyuner.bg.approval.service;

import com.kuyuner.bg.approval.entity.Finance;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;

/**
 * 财务申请Service层接口
 *
 * @author administrator
 */
public interface FinanceService {

    /**
     * 查询唯一
     *
     * @param id
     * @return
     */
    Finance get(String id);

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
    Finance getLog(String businessLogId);

    /**
     * 查询待办事项
     *
     * @param pageNum
     * @param pageSize
     * @param finance
     * @return
     */
    PageJson findPendingList(String pageNum, String pageSize, Finance finance);

    /**
     * 查询历史审批
     *
     * @param pageNum
     * @param pageSize
     * @param finance
     * @return
     */
    PageJson findHistoricList(String pageNum, String pageSize, Finance finance);

    /**
     * 查询申请历史
     *
     * @param pageNum
     * @param pageSize
     * @param finance
     * @return
     */
    PageJson findSendList(String pageNum, String pageSize, Finance finance);

    /**
     * 提交申请
     *
     * @param finance
     * @param taskResult
     * @return
     */
    ResultJson submitForm(Finance finance, String taskResult);

    /**
     * 审批操作
     *
     * @param id
     * @param approvalResult
     * @param taskResult
     * @return
     */
    ResultJson approvalForm(String id, String approvalResult, String taskResult);

}