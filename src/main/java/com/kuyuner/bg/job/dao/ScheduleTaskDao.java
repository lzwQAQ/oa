package com.kuyuner.bg.job.dao;

import com.kuyuner.common.persistence.CrudDao;
import com.kuyuner.common.persistence.annotation.MyBatisDao;
import com.kuyuner.bg.job.entity.ScheduleTask;

/**
 * 定时任务Dao层接口
 *
 * @author administrator
 */
@MyBatisDao
public interface ScheduleTaskDao extends CrudDao<ScheduleTask>{

}