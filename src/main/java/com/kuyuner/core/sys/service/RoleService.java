package com.kuyuner.core.sys.service;

import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.core.sys.entity.Role;

/**
 * 角色Service层接口
 *
 * @author administrator
 */
public interface RoleService {

    /**
     * 分页查询
     *
     * @param pageNum
     * @param pageSize
     * @param role
     * @return
     */
    PageJson findPageList(String pageNum, String pageSize, Role role);

    /**
     * 查询唯一
     *
     * @param id
     * @return
     */
    Role get(String id);

    /**
     * 插入数据
     *
     * @param role
     * @return
     */
    ResultJson saveOrUpdate(Role role);

    /**
     * 删除数据
     *
     * @param ids
     */
    ResultJson deletes(String... ids);

    /**
     * 查询所有的权限
     *
     * @param id
     * @return
     */
    ResultJson findAllPermissions(String roleId);

    /**
     * 保存权限
     *
     * @param roleId
     * @param split
     * @return
     */
    ResultJson savePermissions(String roleId, String[] split);
}