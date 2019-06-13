package com.kuyuner.bg.work.dao;

import com.kuyuner.common.persistence.CrudDao;
import com.kuyuner.common.persistence.annotation.MyBatisDao;
import com.kuyuner.bg.work.entity.Train;
import com.kuyuner.core.sys.entity.User;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 培训通知Dao层接口
 *
 * @author administrator
 */
@MyBatisDao
public interface TrainDao extends CrudDao<Train> {

    /**
     * 查询用户部门树
     *
     * @return
     */
    List<Map<String, Object>> findUserTree();

    /**
     * 查询参与人员
     *
     * @param joinPeoples
     * @return
     */
    List<User> findJoinPeoples(@Param("joinPeoples") String[] joinPeoples);
}