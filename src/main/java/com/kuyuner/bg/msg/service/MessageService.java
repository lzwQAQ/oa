package com.kuyuner.bg.msg.service;


import com.kuyuner.bg.msg.dto.GroupDTO;
import com.kuyuner.bg.msg.entity.GroupUser;
import com.kuyuner.bg.msg.entity.Message;
import com.kuyuner.common.controller.ListJson;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import org.quartz.JobKey;

import java.util.List;

public interface MessageService {

	ResultJson saveOrUpdate(Message message);

	Message getById(String id);

	PageJson findMessageList(String pageNum, String pageSize,  Message message);
	/**
	 * 发送定时短信
	 *
	 * @param scheduleTaskId
	 * @param emailId
	 */
	void sendScheduleMessage(JobKey scheduleTaskId, String emailId);

    /**
     * 获取部门人员树形结构
     * @return
     */
	ListJson getAllDeptPerson();

	/** 短信发送 */
	String sendMessage(String messageId,String receiver,String content);

	/**
	 * 获取分组数据
	 * @return
	 */
	List<GroupUser> userGroupList(String groupId);

	/**
	 * 新增用户组
	 * @return
	 */
	ResultJson saveOrUpdateGroup(GroupDTO groupDTO);

	ResultJson saveGroupUser(GroupUser groupUser);

	ResultJson removeGroupUser(GroupUser groupUser);
	/**
	 * 删除用户组
	 * @param ids
	 */
	ResultJson deleteGroup(String ids);

	GroupUser getGroupUserById(String id);

	List<GroupUser> getGroupUserAll();

}
