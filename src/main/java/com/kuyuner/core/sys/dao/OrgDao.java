package com.kuyuner.core.sys.dao;

import com.kuyuner.common.persistence.TreeDao;
import com.kuyuner.common.persistence.annotation.MyBatisDao;
import com.kuyuner.core.sys.entity.Org;

/**
 * @author administrator
 */
@MyBatisDao
public interface OrgDao extends TreeDao<Org> {
    
}
