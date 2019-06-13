package com.kuyuner.bg.msg.dao;

import com.kuyuner.bg.msg.entity.Message;
import com.kuyuner.common.persistence.CrudDao;
import com.kuyuner.common.persistence.annotation.MyBatisDao;

import java.util.List;
import java.util.Map;


/**
 * 收文Dao层接口
 *
 * @author administrator
 */
@MyBatisDao
public interface MessageDao extends CrudDao<Message> {
    Message getById(String id);

    List<Map<String,Object>> getAllDeptPerson();

    List<String> getAllDeptPhoneByDeptId(String deptId);
}