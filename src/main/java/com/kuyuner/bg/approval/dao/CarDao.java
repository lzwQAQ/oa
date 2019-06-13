package com.kuyuner.bg.approval.dao;

import com.kuyuner.bg.approval.entity.Car;
import com.kuyuner.common.persistence.CrudDao;
import com.kuyuner.common.persistence.annotation.MyBatisDao;

/**
 * 车辆信息Dao层接口
 *
 * @author administrator
 */
@MyBatisDao
public interface CarDao extends CrudDao<Car> {

}