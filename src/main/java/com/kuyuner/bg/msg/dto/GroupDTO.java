package com.kuyuner.bg.msg.dto;

import com.kuyuner.bg.msg.entity.GroupUser;

import java.util.List;

/**
 *
 * @author administrator
 */
public class GroupDTO {

    private String groupId;

    private String groupName;

    private List<GroupUser> groupUsers;


    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<GroupUser> getGroupUsers() {
        return groupUsers;
    }

    public void setGroupUsers(List<GroupUser> groupUsers) {
        this.groupUsers = groupUsers;
    }
}