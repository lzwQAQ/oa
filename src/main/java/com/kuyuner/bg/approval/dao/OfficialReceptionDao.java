package com.kuyuner.bg.approval.dao;

import com.kuyuner.bg.approval.bean.OfficialReceptionHistoricListView;
import com.kuyuner.bg.approval.bean.OfficialReceptionPendingListView;
import com.kuyuner.bg.approval.entity.OfficialReception;
import com.kuyuner.common.persistence.CrudDao;
import com.kuyuner.common.persistence.annotation.MyBatisDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 公务阶段申请Dao层接口
 *
 * @author administrator
 */
@MyBatisDao
public interface OfficialReceptionDao extends CrudDao<OfficialReception> {

    /**
     * 查询历史消息
     *
     * @param officialReception
     * @return
     */
    OfficialReception getLog(OfficialReception officialReception);

    /**
     * 查询待办事项
     *
     * @param officialReception
     * @param userId
     * @return
     */
    List<OfficialReceptionPendingListView> findPendingList(@Param("officialReception") OfficialReception officialReception, @Param("userId") String userId);

    /**
     * 查询审批历史
     *
     * @param officialReception
     * @param userId
     * @return
     */
    List<OfficialReceptionHistoricListView> findHistoricList(@Param("officialReception") OfficialReception officialReception, @Param("userId") String userId);

    /**
     * 查询申请历史
     *
     * @param officialReception
     * @param userId
     * @return
     */
    List<OfficialReceptionPendingListView> findSendList(@Param("officialReception") OfficialReception officialReception, @Param("userId") String userId);

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
     * @param officialReception
     * @return
     */
    int insertLog(OfficialReception officialReception);



    List<OfficialReceptionPendingListView> findPendingDetail(@Param("taskId") String taskId, @Param("userId") String userId);
}