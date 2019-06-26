package com.kuyuner.bg.article.dao;

import com.kuyuner.bg.article.entity.LeaveHome;
import com.kuyuner.common.persistence.CrudDao;
import com.kuyuner.common.persistence.annotation.MyBatisDao;

import java.util.List;

/**
 * 收文Dao层接口
 *
 * @author administrator
 */
@MyBatisDao
public interface LeaveHomeDao extends CrudDao<LeaveHome> {
    List<LeaveHome> findData(String userId);

    Integer dataCount(String userId);
}