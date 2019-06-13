package com.kuyuner.workflow.manage.dao;

import com.kuyuner.common.persistence.annotation.MyBatisDao;
import com.kuyuner.workflow.manage.bean.Deployment;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 流程管理-已发布流程
 *
 * @author tangzj
 */
@MyBatisDao
public interface ReleasedDao {

    /**
     * 查询已发布的工作流
     *
     * @param modelKey
     * @param modelName
     * @return
     */
    List<Deployment> findReleasedWorkFlow(@Param("modelKey") String modelKey, @Param("modelName") String modelName);

}
