package com.kuyuner.core.sys.dao;

import com.kuyuner.common.persistence.TreeDao;
import com.kuyuner.common.persistence.annotation.MyBatisDao;
import com.kuyuner.core.sys.entity.Area;

/**
 * 区划Dao层接口
 *
 * @author administrator
 */
@MyBatisDao
public interface AreaDao extends TreeDao<Area> {

}