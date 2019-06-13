package com.kuyuner.workflow.runtime.dao;

import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.persistence.annotation.MyBatisDao;
import com.kuyuner.workflow.api.bean.BusinessKey;

import org.apache.ibatis.annotations.Param;

/**
 * 运行中流程
 *
 * @author tangzj
 */
@MyBatisDao
public interface PendingTaskDao {

    /**
     * 查询指定用户能够处理的任务
     *
     * @param userId
     * @param processDefinitionName
     * @param taskName
     * @param modelKey
     * @return
     */
    PageJson findTask(@Param("userId") String userId, @Param("processDefinitionName") String processDefinitionName,
                      @Param("taskName") String taskName, @Param("modelKey") String modelKey);

    /**
     * 根据流程实例ID找到业务ID
     *
     * @param processInstanceId
     * @return
     */
    BusinessKey getTaskBusinessKey(@Param("processInstanceId") String processInstanceId);

}
