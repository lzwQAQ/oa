package com.kuyuner.bg.approval.dao;

import com.kuyuner.bg.approval.entity.Driver;
import com.kuyuner.common.persistence.CrudDao;
import com.kuyuner.common.persistence.annotation.MyBatisDao;

/**
 * 车辆信息Dao层接口
 *
 * @author administrator
 */
@MyBatisDao
public interface DriverDao extends CrudDao<Driver> {

}