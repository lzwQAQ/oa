package com.kuyuner.bg.work.dao;

import com.kuyuner.common.persistence.CrudDao;
import com.kuyuner.common.persistence.annotation.MyBatisDao;
import com.kuyuner.bg.work.entity.Contract;

/**
 * 合同管理Dao层接口
 *
 * @author administrator
 */
@MyBatisDao
public interface ContractDao extends CrudDao<Contract>{

}