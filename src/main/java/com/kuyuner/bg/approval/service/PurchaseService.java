package com.kuyuner.bg.approval.service;

import com.kuyuner.bg.approval.entity.Purchase;
import com.kuyuner.bg.approval.entity.PurchaseGoods;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;

import java.util.List;

/**
 * 采购申请Service层接口
 *
 * @author administrator
 */
public interface PurchaseService {

    /**
     * 查询唯一
     *
     * @param id
     * @return
     */
    Purchase get(String id);

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
    Purchase getLog(String businessLogId);

    /**
     * 查询待办事项
     *
     * @param pageNum
     * @param pageSize
     * @param purchase
     * @return
     */
    PageJson findPendingList(String pageNum, String pageSize, Purchase purchase);

    /**
     * 查询历史审批
     *
     * @param pageNum
     * @param pageSize
     * @param purchase
     * @return
     */
    PageJson findHistoricList(String pageNum, String pageSize, Purchase purchase);

    /**
     * 查询申请历史
     *
     * @param pageNum
     * @param pageSize
     * @param purchase
     * @return
     */
    PageJson findSendList(String pageNum, String pageSize, Purchase purchase);

    /**
     * 提交申请
     *
     * @param purchase
     * @param taskResult
     * @param fileInfos
     * @return
     */
    ResultJson submitForm(Purchase purchase, String taskResult, List<PurchaseGoods> fileInfos,String userId);

    /**
     * 审批操作
     *
     * @param id
     * @param approvalResult
     * @param taskResult
     * @return
     */
    ResultJson approvalForm(String id, String approvalResult, String taskResult,String userId);


    /**
     * 查询物品历史数据
     *
     * @param businessLogId
     * @return
     */
    List<PurchaseGoods> findGoodsLog(String businessLogId);

    /**
     * 查询物品信息
     *
     * @param businessId
     * @return
     */
    List<PurchaseGoods> findGoods(String businessId);

    void handleForm(Purchase purchase, String taskResult, List<PurchaseGoods> purchaseGoods,String userId);

}