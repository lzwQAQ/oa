package com.kuyuner.workflow.api.dao;

import com.kuyuner.common.persistence.annotation.MyBatisDao;
import com.kuyuner.workflow.api.bean.TaskBusinessKey;
import com.kuyuner.workflow.manage.bean.TaskLog;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author tangzj
 */
@MyBatisDao
public interface WorkflowDao {

    /**
     * 查询所有候选人id
     *
     * @param modelKey
     * @param taskKey
     * @return
     */
    List<String> findAllCandidateUsers(@Param("modelKey") String modelKey, @Param("taskKey") String taskKey);

    /**
     * 查询所有候选人信息
     *
     * @param modelKey
     * @param taskKey
     * @param searchText
     * @return
     */
    List<Map<String, Object>> findAllCandidateUserInfos(@Param("modelKey") String modelKey,
                                                        @Param("taskKey") String taskKey,
                                                        @Param("searchText") String searchText);

    /**
     * 查询默认表单路径
     *
     * @param taskKey
     * @param modelKey
     * @return
     */
    String getDefaultFormPath(@Param("taskKey") String taskKey, @Param("modelKey") String modelKey);

    /**
     * 给任务批量添加候选人
     *
     * @param taskId
     * @param users
     */
    void addCandidateUsers(@Param("taskId") String taskId, @Param("users") List<String> users);

    int insertTaskBusinessKey(TaskBusinessKey taskBusinessKey);

    int insertTaskLog(TaskLog taskLog);
}
