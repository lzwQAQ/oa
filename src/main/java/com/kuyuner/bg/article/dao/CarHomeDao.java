package com.kuyuner.bg.article.dao;

import com.kuyuner.bg.article.entity.CarHome;
import com.kuyuner.common.persistence.CrudDao;
import com.kuyuner.common.persistence.annotation.MyBatisDao;

import java.util.List;

/**
 * 用车Dao层接口
 *
 * @author administrator
 */
@MyBatisDao
public interface CarHomeDao extends CrudDao<CarHome> {

    List<CarHome> findData(String userId);

    Integer dataCount(String userId);
}