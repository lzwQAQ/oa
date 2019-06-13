package com.kuyuner.bg.approval.dao;

import com.kuyuner.bg.approval.bean.AdjustmentHistoricListView;
import com.kuyuner.bg.approval.bean.AdjustmentPendingListView;
import com.kuyuner.bg.approval.entity.PersonnelAdjustment;
import com.kuyuner.common.persistence.CrudDao;
import com.kuyuner.common.persistence.annotation.MyBatisDao;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 人事调度Dao层接口
 *
 * @author administrator
 */
@MyBatisDao
public interface PersonnelAdjustmentDao extends CrudDao<PersonnelAdjustment> {
    /**
     * 查询历史消息
     *
     * @param personnelAdjustment
     * @return
     */
    PersonnelAdjustment getLog(PersonnelAdjustment personnelAdjustment);

    /**
     * 查询待办事项
     *
     * @param personnelAdjustment
     * @param userId
     * @return
     */
    List<AdjustmentPendingListView> findPendingList(@Param("personnelAdjustment") PersonnelAdjustment personnelAdjustment, @Param("userId") String userId);

    /**
     * 查询审批历史
     *
     * @param personnelAdjustment
     * @param userId
     * @return
     */
    List<AdjustmentHistoricListView> findHistoricList(@Param("personnelAdjustment") PersonnelAdjustment personnelAdjustment, @Param("userId") String userId);

    /**
     * 查询申请历史
     *
     * @param personnelAdjustment
     * @param userId
     * @return
     */
    List<AdjustmentHistoricListView> findSendList(@Param("personnelAdjustment") PersonnelAdjustment personnelAdjustment, @Param("userId") String userId);

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
     * @param personnelAdjustment
     * @return
     */
    int insertLog(PersonnelAdjustment personnelAdjustment);
}