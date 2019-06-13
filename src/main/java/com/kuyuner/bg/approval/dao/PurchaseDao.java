package com.kuyuner.bg.approval.dao;

import com.kuyuner.bg.approval.bean.PurchaseHistoricListView;
import com.kuyuner.bg.approval.bean.PurchasePendingListView;
import com.kuyuner.bg.approval.entity.Purchase;
import com.kuyuner.common.persistence.CrudDao;
import com.kuyuner.common.persistence.annotation.MyBatisDao;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 采购申请Dao层接口
 *
 * @author administrator
 */
@MyBatisDao
public interface PurchaseDao extends CrudDao<Purchase> {
    /**
     * 查询历史消息
     *
     * @param purchaseLog
     * @return
     */
    Purchase getLog(Purchase purchaseLog);

    /**
     * 查询待办事项
     *
     * @param purchase
     * @param userId
     * @return
     */
    List<PurchasePendingListView> findPendingList(@Param("purchase") Purchase purchase, @Param("userId") String userId);

    /**
     * 查询审批历史
     *
     * @param purchase
     * @param userId
     * @return
     */
    List<PurchaseHistoricListView> findHistoricList(@Param("purchase") Purchase purchase, @Param("userId") String userId);

    /**
     * 查询申请历史
     *
     * @param purchase
     * @param userId
     * @return
     */
    List<PurchaseHistoricListView> findSendList(@Param("purchase") Purchase purchase, @Param("userId") String userId);

    /**
     * 获得处理意见
     *
     * @param id
     * @return
     */
    String getApprovalResult(String id);

    /**
     * 保存业务日志
     *
     * @param purchaseLog
     * @return
     */
    int insertLog(Purchase purchaseLog);

}