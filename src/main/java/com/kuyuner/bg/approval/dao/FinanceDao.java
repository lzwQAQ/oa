package com.kuyuner.bg.approval.dao;

import com.kuyuner.bg.approval.bean.FinanceHistoricListView;
import com.kuyuner.bg.approval.bean.FinancePendingListView;
import com.kuyuner.bg.approval.entity.Finance;
import com.kuyuner.common.persistence.CrudDao;
import com.kuyuner.common.persistence.annotation.MyBatisDao;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 财务申请Dao层接口
 *
 * @author administrator
 */
@MyBatisDao
public interface FinanceDao extends CrudDao<Finance> {

    /**
     * 查询历史消息
     *
     * @param financeLog
     * @return
     */
    Finance getLog(Finance financeLog);

    /**
     * 查询待办事项
     *
     * @param finance
     * @param userId
     * @return
     */
    List<FinancePendingListView> findPendingList(@Param("finance") Finance finance, @Param("userId") String userId);

    /**
     * 查询审批历史
     *
     * @param finance
     * @param userId
     * @return
     */
    List<FinanceHistoricListView> findHistoricList(@Param("finance") Finance finance, @Param("userId") String userId);

    /**
     * 查询申请历史
     *
     * @param finance
     * @param userId
     * @return
     */
    List<FinanceHistoricListView> findSendList(@Param("finance") Finance finance, @Param("userId") String userId);

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
     * @param financeLog
     * @return
     */
    int insertLog(Finance financeLog);

}