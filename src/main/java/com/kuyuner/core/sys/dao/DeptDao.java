package com.kuyuner.core.sys.dao;

import com.kuyuner.common.persistence.TreeDao;
import com.kuyuner.common.persistence.annotation.MyBatisDao;
import com.kuyuner.core.sys.entity.Dept;

import java.util.List;
import java.util.Map;

/**
 * 部门Dao层接口
 *
 * @author administrator
 */
@MyBatisDao
public interface DeptDao extends TreeDao<Dept> {

    List<Map<String, Object>> findDeptTreeList();
}