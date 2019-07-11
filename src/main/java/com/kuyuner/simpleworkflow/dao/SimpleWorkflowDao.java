package com.kuyuner.simpleworkflow.dao;

import com.kuyuner.common.persistence.CrudDao;
import com.kuyuner.common.persistence.annotation.MyBatisDao;
import com.kuyuner.simpleworkflow.bean.SimpleWorkflowHistoricListView;
import com.kuyuner.simpleworkflow.bean.SimpleWorkflowPendingListView;
import com.kuyuner.simpleworkflow.entity.SimpleWorkflow;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 简单流程Dao层接口
 *
 * @author administrator
 */
@MyBatisDao
public interface SimpleWorkflowDao extends CrudDao<SimpleWorkflow> {
    /**
     * @param workflow
     * @return
     */
    int insertSimple(@Param("workflow") SimpleWorkflow workflow);

    /**
     * 查询待办事项
     *
     * @param simpleWorkflow
     * @return
     */
    List<SimpleWorkflowPendingListView> findPendingList(@Param("workflow") SimpleWorkflow simpleWorkflow, @Param("userId") String userId);

    /**
     * 查询审批历史
     *
     * @param simpleWorkflow
     * @param userId
     * @return
     */
    List<SimpleWorkflowHistoricListView> findHistoricList(@Param("workflow") SimpleWorkflow simpleWorkflow, @Param("userId") String userId);

    /**
     * 查询申请历史
     *
     * @param simpleWorkflow
     * @param userId
     * @return
     */
    List<SimpleWorkflowHistoricListView> findSendList(@Param("workflow") SimpleWorkflow simpleWorkflow, @Param("userId") String userId);

    /**
     * 插入业务日志
     *
     * @param workflowLog
     * @return
     */
    int insertLog(SimpleWorkflow workflowLog);

    /**
     * 获取业务日志
     *
     * @param workflowLog
     * @return
     */
    SimpleWorkflow getLog(SimpleWorkflow workflowLog);

    /**
     * 获得审批结果
     *
     * @param id
     * @return
     */
    String getApprovalResult(@Param("id") String id);

    /**
     * 获得模型的ID
     *
     * @param modelKey
     * @return
     */
    String getModelId(@Param("modelKey") String modelKey);

    List<SimpleWorkflowPendingListView> findPendingDetail(@Param("taskId") String taskId, @Param("userId") String userId);
}