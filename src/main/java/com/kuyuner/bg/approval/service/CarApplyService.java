package com.kuyuner.bg.approval.service;

import com.kuyuner.bg.approval.entity.CarApply;
import com.kuyuner.common.controller.ListJson;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;

/**
 * 车辆申请Service层接口
 *
 * @author administrator
 */
public interface CarApplyService {

    /**
     * 查询唯一
     *
     * @param id
     * @return
     */
    CarApply get(String id);

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
    CarApply getLog(String businessLogId);

    /**
     * 查询待办事项
     *
     * @param pageNum
     * @param pageSize
     * @param carApply
     * @return
     */
    PageJson findPendingList(String pageNum, String pageSize, CarApply carApply);

    /**
     * 查询历史审批
     *
     * @param pageNum
     * @param pageSize
     * @param carApply
     * @return
     */
    PageJson findHistoricList(String pageNum, String pageSize, CarApply carApply);

    /**
     * 查询申请历史
     *
     * @param pageNum
     * @param pageSize
     * @param carApply
     * @return
     */
    PageJson findSendList(String pageNum, String pageSize, CarApply carApply);

    /**
     * 提交申请
     *
     * @param carApply
     * @param taskResult
     * @return
     */
    ResultJson submitForm(CarApply carApply, String taskResult,String userId);

    /**
     * 审批操作
     *
     * @param carApply
     * @param approvalResult
     * @param taskResult
     * @return
     */
    ResultJson approvalForm(CarApply carApply, String approvalResult, String taskResult,String userId);

    /**
     * 查询驾驶员
     *
     * @return
     */
    ListJson findDrivers(String userId);

    /**
     * 查询车辆
     *
     * @return
     */
    ListJson findCars();

    void _submitForm(CarApply carApply, String taskResult,String userId);
}