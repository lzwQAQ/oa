package com.kuyuner.bg.approval.service;

import com.kuyuner.bg.approval.entity.Business;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;

import java.util.List;
import java.util.Map;

/**
 * 业务申请Service层接口
 *
 * @author administrator
 */
public interface BusinessService {

    /**
     * 查询唯一
     *
     * @param id
     * @return
     */
    Business get(String id);

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
    Business getLog(String businessLogId);

    /**
     * 查询待办事项
     *
     * @param pageNum
     * @param pageSize
     * @param business
     * @return
     */
    PageJson findPendingList(String pageNum, String pageSize, Business business);

    /**
     * 查询历史审批
     *
     * @param pageNum
     * @param pageSize
     * @param business
     * @return
     */
    PageJson findHistoricList(String pageNum, String pageSize, Business business);

    /**
     * 查询申请历史
     *
     * @param pageNum
     * @param pageSize
     * @param business
     * @return
     */
    PageJson findSendList(String pageNum, String pageSize, Business business);

    /**
     * 提交申请
     *
     * @param business
     * @param taskResult
     * @return
     */
    ResultJson submitForm(Business business, String taskResult);

    /**
     * 审批操作
     *
     * @param id
     * @param approvalResult
     * @param taskResult
     * @return
     */
    ResultJson approvalForm(String id, String approvalResult, String taskResult);

    /**
     * 查询所有的用户
     *
     * @return
     */
    List<Map<String, Object>> findAllUsers();

    /**
     * 查询所有的部门
     *
     * @return
     */
    List<Map<String, Object>> findAllDepts();
}