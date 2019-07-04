package com.kuyuner.bg.approval.service;

import com.kuyuner.bg.approval.entity.PersonnelAdjustment;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;

/**
 * 人事调度Service层接口
 *
 * @author administrator
 */
public interface PersonnelAdjustmentService {

    /**
     * 查询唯一
     *
     * @param id
     * @return
     */
    PersonnelAdjustment get(String id);

    /**
     * 删除数据
     *
     * @param ids
     */
    ResultJson deletes(String... ids);

    /**
     * 获得历史数据
     *
     * @param logId
     * @return
     */
    PersonnelAdjustment getLog(String logId);

    /**
     * 查询待办事项
     *
     * @param pageNum
     * @param pageSize
     * @param personnelAdjustment
     * @return
     */
    PageJson findPendingList(String pageNum, String pageSize, PersonnelAdjustment personnelAdjustment,String userId);

    /**
     * 查询历史审批
     *
     * @param pageNum
     * @param pageSize
     * @param personnelAdjustment
     * @return
     */
    PageJson findHistoricList(String pageNum, String pageSize, PersonnelAdjustment personnelAdjustment,String userId);

    /**
     * 查询申请历史
     *
     * @param pageNum
     * @param pageSize
     * @param personnelAdjustment
     * @return
     */
    PageJson findSendList(String pageNum, String pageSize, PersonnelAdjustment personnelAdjustment,String userId);

    /**
     * 提交申请
     *
     * @param personnelAdjustment
     * @param taskResult
     * @return
     */
    ResultJson submitForm(PersonnelAdjustment personnelAdjustment, String taskResult);

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