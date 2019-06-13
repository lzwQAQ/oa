package com.kuyuner.bg.approval.dao;

import com.kuyuner.bg.approval.entity.LeaveRecord;
import com.kuyuner.common.persistence.CrudDao;
import com.kuyuner.common.persistence.annotation.MyBatisDao;

/**
 * 请假Dao层接口
 *
 * @author administrator
 */
@MyBatisDao
public interface LeaveRecordDao extends CrudDao<LeaveRecord> {

}