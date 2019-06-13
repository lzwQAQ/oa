package com.kuyuner.core.sys.dao;

import com.kuyuner.common.persistence.CrudDao;
import com.kuyuner.common.persistence.annotation.MyBatisDao;
import com.kuyuner.core.sys.entity.User;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 用户Dao层接口
 *
 * @author administrator
 */
@MyBatisDao
public interface UserDao extends CrudDao<User> {

    /**
     * 查询用户拥有的角色
     *
     * @param userId
     * @param roleName
     * @return
     */
    List<Map<String, Object>> findUserRoles(@Param("userId") String userId, @Param("roleName") String roleName);

    /**
     * 查询未选择的角色
     *
     * @param userId
     * @param roleName
     * @return
     */
    List<Map<String, Object>> findUnSelectedRoles(@Param("userId") String userId, @Param("roleName") String roleName);

    /**
     * 保存角色信息
     *
     * @param userId
     * @param roles
     * @return
     */
    int saveRoles(@Param("userId") String userId, @Param("roles") String[] roles);

    /**
     * 删除角色信息
     *
     * @param ids
     * @return
     */
    int deleteRoles(@Param("ids") String[] ids);
}