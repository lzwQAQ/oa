package com.kuyuner.core.sys.service;

import com.kuyuner.common.controller.ListJson;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.core.sys.entity.User;

import java.util.List;

/**
 * 用户Service层接口
 *
 * @author administrator
 */
public interface UserService {

    /**
     * 分页查询
     *
     * @param pageNum
     * @param pageSize
     * @param user
     * @return
     */
    PageJson findPageList(String pageNum, String pageSize, User user);

    /**
     * 查询唯一
     *
     * @param id
     * @return
     */
    User get(String id);

    /**
     * 插入数据
     *
     * @param user
     * @return
     */
    ResultJson saveOrUpdate(User user);

    /**
     * 删除数据
     *
     * @param ids
     */
    ResultJson deletes(String... ids);

    /**
     * 修改个人密码
     *
     * @param oldPassword
     * @param password
     * @return
     */
    ResultJson changeMyPassword(String oldPassword, String password);

    /**
     * 查询用户所有的角色
     *
     * @param userId
     * @param roleName
     * @return
     */
    ListJson findUserRoles(String userId, String roleName);

    /**
     * 查询未选择的角色
     *
     * @param userId
     * @param roleName
     * @return
     */
    ListJson findUnSelectedRoles(String userId, String roleName);

    /**
     * 保存角色信息
     *
     * @param userId
     * @param split
     * @return
     */
    ResultJson saveRoles(String userId, String[] split);

    /**
     * 删除角色信息
     *
     * @param ids
     * @return
     */
    ResultJson deleteRoles(String[] ids);

    List<String> getRoleName(String userId);

}