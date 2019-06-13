package com.kuyuner.bg.work.dao;

import com.kuyuner.common.persistence.CrudDao;
import com.kuyuner.common.persistence.annotation.MyBatisDao;
import com.kuyuner.bg.work.entity.Meeting;
import com.kuyuner.core.sys.entity.User;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 会议通知Dao层接口
 *
 * @author administrator
 */
@MyBatisDao
public interface MeetingDao extends CrudDao<Meeting> {

    /**
     * 查询参与人员
     *
     * @param joinPeoples
     * @return
     */
    List<User> findJoinPeoples(@Param("joinPeoples") String[] joinPeoples);
}