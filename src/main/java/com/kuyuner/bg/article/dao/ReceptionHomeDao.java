package com.kuyuner.bg.article.dao;

import com.kuyuner.bg.article.entity.ReceptionHome;
import com.kuyuner.common.persistence.CrudDao;
import com.kuyuner.common.persistence.annotation.MyBatisDao;

import java.util.List;

/**
 * 公务接待Dao层接口
 *
 * @author administrator
 */
@MyBatisDao
public interface ReceptionHomeDao extends CrudDao<ReceptionHome> {
    List<ReceptionHome> findData(String userId);

    Integer dataCount(String userId);
}