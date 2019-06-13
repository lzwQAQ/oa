package com.kuyuner.core.sys.dao;

import com.kuyuner.common.persistence.CrudDao;
import com.kuyuner.common.persistence.annotation.MyBatisDao;
import com.kuyuner.core.sys.entity.Role;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 角色Dao层接口
 *
 * @author administrator
 */
@MyBatisDao
public interface RoleDao extends CrudDao<Role> {

    /**
     * 查询所有的权限
     *
     * @param roleId 角色ID
     * @return
     */
    List<Map<String, Object>> findAllPermissions(@Param("roleId") String roleId);

    /**
     * 查询所有的菜单
     *
     * @return
     */
    List<Map<String, Object>> findAllMenus();

    /**
     * 根据角色删除权限
     *
     * @param roleId
     * @return
     */
    int deleteByRoleId(@Param("roleId") String roleId);

    /**
     * 保存权限信息
     *
     * @param roleId
     * @param menuIds
     * @return
     */
    int savePermissions(@Param("roleId") String roleId, @Param("menuIds") String[] menuIds);
}