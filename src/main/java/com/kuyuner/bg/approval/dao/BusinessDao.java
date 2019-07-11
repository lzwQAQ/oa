package com.kuyuner.bg.approval.dao;

import com.kuyuner.bg.approval.bean.BusinessHistoricListView;
import com.kuyuner.bg.approval.bean.BusinessPendingListView;
import com.kuyuner.bg.approval.entity.Business;
import com.kuyuner.common.persistence.CrudDao;
import com.kuyuner.common.persistence.annotation.MyBatisDao;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 业务申请Dao层接口
 *
 * @author administrator
 */
@MyBatisDao
public interface BusinessDao extends CrudDao<Business> {


    /**
     * 查询历史消息
     *
     * @param businessLog
     * @return
     */
    Business getLog(Business businessLog);

    /**
     * 查询待办事项
     *
     * @param business
     * @param userId
     * @return
     */
    List<BusinessPendingListView> findPendingList(@Param("business") Business business, @Param("userId") String userId);

    /**
     * 查询审批历史
     *
     * @param business
     * @param userId
     * @return
     */
    List<BusinessHistoricListView> findHistoricList(@Param("business") Business business, @Param("userId") String userId);

    /**
     * 查询申请历史
     *
     * @param business
     * @param userId
     * @return
     */
    List<BusinessHistoricListView> findSendList(@Param("business") Business business, @Param("userId") String userId);

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
     * @param businessLog
     * @return
     */
    int insertLog(Business businessLog);

    /**
     * 查询所有的用户
     *
     * @return
     */
    List<Map<String, Object>> findAllUsers();

    /**
     * 查询所有的部门
     *
     * @return
     */
    List<Map<String, Object>> findAllDepts();

    List<BusinessPendingListView> findPendingDetail(@Param("taskId") String taskId, @Param("userId") String userId);
}