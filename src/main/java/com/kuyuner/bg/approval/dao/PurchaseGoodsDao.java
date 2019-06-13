package com.kuyuner.bg.approval.dao;

import com.kuyuner.bg.approval.entity.PurchaseGoods;
import com.kuyuner.common.persistence.CrudDao;
import com.kuyuner.common.persistence.annotation.MyBatisDao;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 采购清单Dao层接口
 *
 * @author administrator
 */
@MyBatisDao
public interface PurchaseGoodsDao extends CrudDao<PurchaseGoods> {
    /**
     * 插入日志
     *
     * @param purchaseGoods
     * @return
     */
    int insertLogs(@Param("purchaseGoods") List<PurchaseGoods> purchaseGoods);

    /**
     * 查询日志
     *
     * @param purchaseGoods
     * @return
     */
    List<PurchaseGoods> findPurchaseGoodsLogList(PurchaseGoods purchaseGoods);

    /**
     * 批量插入数据
     *
     * @param purchaseGoods
     * @return
     */
    int inserts(@Param("purchaseGoods") List<PurchaseGoods> purchaseGoods);

    /**
     * 根据采购ID删除物品信息
     *
     * @param purchaseId
     * @return
     */
    int deleteGoods(@Param("purchaseId") String purchaseId);

    /**
     * 查询物品历史信息
     *
     * @param purchaseId
     * @return
     */
    List<PurchaseGoods> findGoodsLog(@Param("purchaseId") String purchaseId);

}