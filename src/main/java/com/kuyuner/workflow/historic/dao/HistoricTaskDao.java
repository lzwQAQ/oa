package com.kuyuner.workflow.historic.dao;

import com.kuyuner.common.persistence.annotation.MyBatisDao;
import com.kuyuner.workflow.api.bean.BusinessKey;

import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 历史任务
 *
 * @author tangzj
 */
@MyBatisDao
public interface HistoricTaskDao {

    /**
     * 查询历史任务
     *
     * @param userId
     * @param processDefinitionName
     * @param name
     * @param modelKey
     * @param taskType
     * @return
     */
    List<Map<String, Object>> findHistoricTask(@Param("userId") String userId,
                                               @Param("processDefinitionName") String processDefinitionName,
                                               @Param("name") String name,
                                               @Param("modelKey") String modelKey,
                                               @Param("taskType") String taskType);

    /**
     * 查询历史日志信息
     *
     * @param processInstanceId
     * @return
     */
    List<Map<String, Object>> findHistoricTrackInfo(@Param("processInstanceId") String processInstanceId);

    /**
     * 根据任务ID获得BusinessKey
     *
     * @param historicTaskId
     * @return
     */
    BusinessKey getHistoricTaskBusinessKey(@Param("historicTaskId") String historicTaskId);

    /**
     * 获得最后一个任务耗时
     *
     * @param processInstanceId
     * @return
     */
    Date getLastTaskTime(@Param("processInstanceId") String processInstanceId);

    /**
     * 查询历史流程实例信息
     *
     * @param processInstanceId
     * @return
     */
    Map<String, Object> getHistoricProcessInstanceInfo(@Param("processInstanceId") String processInstanceId);
}
