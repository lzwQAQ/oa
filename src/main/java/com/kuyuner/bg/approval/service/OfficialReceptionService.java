package com.kuyuner.bg.approval.service;

import com.kuyuner.bg.approval.entity.OfficialReception;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;

/**
 * 公务接待申请Service层接口
 *
 * @author administrator
 */
public interface OfficialReceptionService {

    /**
     * 查询唯一
     *
     * @param id
     * @return
     */
    OfficialReception get(String id);

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
    OfficialReception getLog(String businessLogId);

    /**
     * 查询待办事项
     *
     * @param pageNum
     * @param pageSize
     * @param officialReception
     * @return
     */
    PageJson findPendingList(String pageNum, String pageSize, OfficialReception officialReception);

    /**
     * 查询历史审批
     *
     * @param pageNum
     * @param pageSize
     * @param officialReception
     * @return
     */
    PageJson findHistoricList(String pageNum, String pageSize, OfficialReception officialReception);

    /**
     * 查询申请历史
     *
     * @param pageNum
     * @param pageSize
     * @param officialReception
     * @return
     */
    PageJson findSendList(String pageNum, String pageSize, OfficialReception officialReception);

    /**
     * 提交申请
     *
     * @param officialReception
     * @param taskResult
     * @return
     */
    ResultJson submitForm(OfficialReception officialReception, String taskResult, String userId);

    /**
     * 审批操作
     *
     * @param officialReception
     * @param approvalResult
     * @param taskResult
     * @return
     */
    ResultJson approvalForm(OfficialReception officialReception, String approvalResult, String taskResult, String userId);

    void _submitForm(OfficialReception officialReception, String taskResult, String userId);
}