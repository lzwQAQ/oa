package com.kuyuner.bg.article.dao;

import com.kuyuner.bg.article.entity.BusinessHome;
import com.kuyuner.common.persistence.CrudDao;
import com.kuyuner.common.persistence.annotation.MyBatisDao;

import java.util.List;

/**
 * 收文Dao层接口
 *
 * @author administrator
 */
@MyBatisDao
public interface BusinessHomeDao extends CrudDao<BusinessHome> {
    List<BusinessHome> findData(String userId);
}