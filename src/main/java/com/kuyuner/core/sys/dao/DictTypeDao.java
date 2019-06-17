package com.kuyuner.core.sys.dao;

import com.kuyuner.common.persistence.CrudDao;
import com.kuyuner.common.persistence.annotation.MyBatisDao;
import com.kuyuner.core.sys.entity.DictType;

import java.util.List;

/**
 * 字典类型Dao层接口
 *
 * @author administrator
 */
@MyBatisDao
public interface DictTypeDao extends CrudDao<DictType>{

}