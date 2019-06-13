package com.kuyuner.bg.approval.dao;

import com.kuyuner.bg.approval.entity.Accounts;
import com.kuyuner.common.persistence.CrudDao;
import com.kuyuner.common.persistence.annotation.MyBatisDao;

/**
 * @author admin
 */
@MyBatisDao
public interface AccountsDao extends CrudDao<Accounts> {
}
