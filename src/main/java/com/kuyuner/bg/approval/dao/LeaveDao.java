package com.kuyuner.bg.approval.dao;

import com.kuyuner.bg.approval.bean.LeaveHistoricListView;
import com.kuyuner.bg.approval.bean.LeavePendingListView;
import com.kuyuner.bg.approval.entity.Leave;
import com.kuyuner.common.persistence.CrudDao;
import com.kuyuner.common.persistence.annotation.MyBatisDao;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 请假Dao层接口
 *
 * @author administrator
 */
@MyBatisDao
public interface LeaveDao extends CrudDao<Leave> {

    /**
     * 查询历史消息
     *
     * @param leaveLog
     * @return
     */
    Leave getLog(Leave leaveLog);

    /**
     * 查询待办事项
     *
     * @param leave
     * @param userId
     * @return
     */
    List<LeavePendingListView> findPendingList(@Param("leave") Leave leave, @Param("userId") String userId);

    /**
     * 查询审批历史
     *
     * @param leave
     * @param userId
     * @return
     */
    List<LeaveHistoricListView> findHistoricList(@Param("leave") Leave leave, @Param("userId") String userId);

    /**
     * 查询申请历史
     *
     * @param leave
     * @param userId
     * @return
     */
    List<LeaveHistoricListView> findSendList(@Param("leave") Leave leave, @Param("userId") String userId);

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
     * @param leaveLog
     * @return
     */
    int insertLog(Leave leaveLog);

    List<LeavePendingListView> findPendingDetail(@Param("taskId") String taskId, @Param("userId") String userId);
}