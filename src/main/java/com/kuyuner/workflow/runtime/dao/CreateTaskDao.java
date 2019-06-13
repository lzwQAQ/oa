package com.kuyuner.workflow.runtime.dao;

import com.kuyuner.common.persistence.annotation.MyBatisDao;

import java.util.List;
import java.util.Map;

/**
 * 创建任务
 *
 * @author tangzj
 */
@MyBatisDao
public interface CreateTaskDao {

    /**
     * 查询可创建的任务
     */
    List<Map<String, Object>> findCreatableTasks();

}
