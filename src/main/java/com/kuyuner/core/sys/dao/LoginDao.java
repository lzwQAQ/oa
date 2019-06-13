package com.kuyuner.core.sys.dao;

import com.kuyuner.common.persistence.annotation.MyBatisDao;
import com.kuyuner.core.sys.entity.User;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 登陆Dao层
 *
 * @author Administrator
 */
@MyBatisDao
public interface LoginDao {
    /**
     * 登陆
     *
     * @param username
     * @return
     */
    User login(@Param("username") String username);

    /**
     * 查询用户权限
     *
     * @param userId
     * @return
     */
    List<String> findUserPermissions(@Param("userId") String userId);
}
