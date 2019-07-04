package com.kuyuner.bg.approval.service;

import com.kuyuner.bg.approval.entity.Leave;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;

/**
 * 请假Service层接口
 *
 * @author administrator
 */
public interface ProduceService {


    /**
     * 查询待办事项
     *
     * @param pageNum
     * @param pageSize
     * @param leave
     * @return
     */
    PageJson findPendingList(String pageNum, String pageSize, Leave leave, String userId);

    /**
     * 查询历史审批
     *
     * @param pageNum
     * @param pageSize
     * @param leave
     * @return
     */
    PageJson findHistoricList(String pageNum, String pageSize, Leave leave, String userId);

    /**
     * 查询申请历史
     *
     * @param pageNum
     * @param pageSize
     * @param leave
     * @return
     */
    PageJson findSendList(String pageNum, String pageSize, Leave leave, String userId);

    String getCode();

}