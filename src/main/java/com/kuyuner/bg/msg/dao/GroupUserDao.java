package com.kuyuner.bg.msg.dao;

import com.kuyuner.bg.msg.dto.GroupUserDTO;
import com.kuyuner.bg.msg.entity.GroupUser;
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
public interface GroupUserDao extends CrudDao<GroupUser> {

    void insertGroupUser(GroupUser groupUser);

    void updateGroupUser(GroupUser groupUser);

    List<GroupUser> getTreeGroup(Map map);

    void deleteUserByGroupId(String groupId);

    void deleteGroupByGroupId(String groupId);

    void deleteUserById(String id);

    GroupUser getGroupUserById(String id);

    List<GroupUser> getGroupUserAll();
}