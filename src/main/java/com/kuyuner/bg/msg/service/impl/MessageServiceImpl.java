package com.kuyuner.bg.msg.service.impl;

import com.kuyuner.bg.article.entity.Article;
import com.kuyuner.bg.job.TaskManager;
import com.kuyuner.bg.job.entity.ScheduleTask;
import com.kuyuner.bg.msg.dao.GroupUserDao;
import com.kuyuner.bg.msg.dao.MessageDao;
import com.kuyuner.bg.msg.dao.MessageSendDao;
import com.kuyuner.bg.msg.dto.GroupDTO;
import com.kuyuner.bg.msg.entity.GroupUser;
import com.kuyuner.bg.msg.entity.Message;
import com.kuyuner.bg.msg.entity.MessageSend;
import com.kuyuner.bg.msg.job.SendMessageJob;
import com.kuyuner.bg.msg.service.MessageService;
import com.kuyuner.bg.msg.util.Msgclient;
import com.kuyuner.common.controller.ListJson;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.common.exception.ServiceException;
import com.kuyuner.common.idgen.IdGenerate;
import com.kuyuner.common.lang.StringUtils;
import com.kuyuner.common.persistence.Page;
import com.kuyuner.core.sys.security.UserUtils;
import com.linkage.netmsg.NetMsgclient;
import org.quartz.CronExpression;
import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class MessageServiceImpl implements MessageService {

	@Autowired
	private MessageDao messageDao;
	@Autowired
	private MessageSendDao messageSendDao;
	@Autowired
	private TaskManager taskManager;
	@Autowired
	private GroupUserDao groupUserDao;

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public ResultJson saveOrUpdate(Message message) {
		String receiver = message.getReceiver();
		if(StringUtils.isBlank(message.getSendType()) || "0".equals(message.getSendType())){//人员发送
			if(StringUtils.isBlank(receiver)){
				return ResultJson.ok("请添加收件人");
			}
		}
		if( "1".equals(message.getSendType())){//机构发送
			if(StringUtils.isBlank(message.getDeptId())){
				return ResultJson.ok("请添加收件人");
			}
			String[] deptIds = message.getDeptId().split(",");
			List<String> deptPhones = new ArrayList<>();
			for(String deptId : deptIds){
				List<String> deptPhone = messageDao.getAllDeptPhoneByDeptId(deptId);
				deptPhones.addAll(deptPhone);
			}
			message.setReceiver(StringUtils.join(deptPhones.toArray()));
		}

		if( "2".equals(message.getSendType())){//组机构发送

		}
		String result = "";
		MessageSend messageSend = new MessageSend();
		if (StringUtils.isBlank(message.getId())) {
			message.setId(IdGenerate.uuid());
			message.setCreater(UserUtils.getPrincipal().getId());
			messageSend.setMsgId(message.getId());
			messageSend.setId(IdGenerate.uuid());
			if ("1".equals(message.getSchedule())) {
				sendBefore(message,"0");;//待发送
				addTask(message.getId(), message.getScheduleTime());
//				messageSendDao.insert(messageSend);
			}else {
				sendBefore(message,"3");;//已发送，待验证
				result = sendMessage(message.getId(),receiver,message.getContent());
//				messageSendDao.insert(messageSend);
			}
			message.setSendMsg(result);
			messageDao.insert(message);
		} else {
			if ("1".equals(message.getSchedule())) {
				sendBefore(message,"0");//发送状态0.待发送
				addTask(message.getId(), message.getScheduleTime());
				messageSendDao.updateByMsgId(messageSend);
			}else {
				sendBefore(message,"3");//已发送，待验证
				result = sendMessage(message.getId(),receiver,message.getContent());
			}
			message.setSendMsg(result);
			messageDao.update(message);
		}
		return ResultJson.ok();
	}

	@Override
	public Message getById(String id) {
		Message message = messageDao.getById(id);
		if(message != null && StringUtils.isNotBlank(message.getReceiver())){
            message.setReceiverList(Arrays.asList(message.getReceiver().split(",")));
        }
		return message;
	}


	@Override
	public PageJson findMessageList(String pageNum, String pageSize, Message message) {
		Page<Article> page = new Page<>(pageNum, pageSize);
		page.start();
		messageDao.findList(message);
		page.end();
		return new PageJson(page);
	}

	@Override
	public void sendScheduleMessage(JobKey jobKey, String messageId) {
		Message message = messageDao.getById(messageId);
		message.setId(messageId);
		message.setSchedule("0");
		sendBefore(message,"3");
		String result = sendMessage(message.getId(),message.getReceiver(),message.getContent());
		message.setSendMsg(result);
		messageDao.update(message);
		taskManager.deleteInStorage(jobKey);
	}

	@Override
	public ListJson getAllDeptPerson() {
		return new ListJson(messageDao.getAllDeptPerson());
	}

	private void sendBefore(Message message,String sendType){
		MessageSend messageSend = new MessageSend();
		String receiver = message.getReceiver();
		if(StringUtils.isBlank(receiver)){
			return;
		}
		String[] receivers = receiver.split(",");
		messageSend.setStatus(sendType);//发送状态0.待发送1.成功2.失败3.待验证
		messageSendDao.deleteByMsgId(messageSend);
		for(String r : receivers){
			messageSend.setId(IdGenerate.uuid());
			messageSend.setMsgId(message.getId());
			messageSend.setReceiver(r);
			messageSendDao.saveOrUpdateReportByDuplicateKey(messageSend);
		}
	}

	/**
	 * 添加定时任务
	 *
	 * @param messageId
	 * @param scheduleTime
	 */
	private void addTask(String messageId, String scheduleTime) {
		ScheduleTask scheduleTask = new ScheduleTask();
		scheduleTask.setId(IdGenerate.uuid());
		scheduleTask.setClassName(SendMessageJob.class.getName());
		scheduleTask.setMethodName("send");
		scheduleTask.setTaskType("send_message");
		scheduleTask.setData(messageId);
		String cron = dateToCron(scheduleTime);
		scheduleTask.setCron(cron);
		CronExpression cronExpression;
		try {
			cronExpression = new CronExpression(cron);
		} catch (ParseException e) {
			throw new ServiceException("短信发送定时任务时间设置有误，请重新设置！");
		}
		if (cronExpression.getTimeAfter(new Date()) == null) {
			throw new ServiceException("短信发送定时任务时间小于当前时间，请重新设置！");
		}
		taskManager.addTasks(scheduleTask);
	}

	/**
	 * 将时间点转换成cron表达式
	 *
	 * @param dateStr
	 * @return
	 */
	private String dateToCron(String dateStr) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date;
		try {
			date = dateFormat.parse(dateStr);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		Calendar calendar = new java.util.GregorianCalendar(TimeZone.getDefault());
		calendar.setTime(date);
		return String.format("%d %d %d %d %d ? %d", calendar.get(Calendar.SECOND), calendar.get(Calendar.MINUTE), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));
	}

	@Override
	public String sendMessage(String messageId,String receiver,String content){
		logger.info("开始发送短信...内容:{},接收人:{}",content,receiver);
		NetMsgclient client = Msgclient.getClient();
		/**
		 * sendType 0：计费号码发送 1：绑定号码轮询
		 * msg 发送内容 500个字节 汉字最多250个
		 * isNeedReport 是否需要回执 0:不需要 1：需要
		 */
		String result = null;
		String[] receivers = receiver.split(",");
		for (String rec : receivers) {
			String seqId = client.sendMsg(client, 0, rec, content, 1);
			if("16".equals(seqId)){//连接出现异常
				/*try {
					client.closeConn();
				} catch (Exception e) {
					e.printStackTrace();
				}*/
				result += (rec + ":短信连接服务异常，发送失败");
				logger.error(result);
			}else {
				MessageSend messageSend = new MessageSend();
				messageSend.setMsgId(messageId);
				messageSend.setReceiver(rec);
				messageSend.setSeqId(seqId);
				messageSendDao.updateSeqIdByMsgIdAndReceiver(messageSend);
			}
		}
		logger.info("发送短信结束。");
		return result;
	}

	@Override
	public List<GroupUser> userGroupList(String groupId) {
		Map map = new HashMap();
		map.put("groupId",groupId);
		List<GroupUser> list = groupUserDao.getTreeGroup(map);
		list.forEach(u->{
			if(StringUtils.isBlank(u.getParentId())){
				u.setLeaf(false);
			}else {
				u.setLeaf(true);
			}
		});
		return list;
	}

	@Transactional
	@Override
	public ResultJson saveOrUpdateGroup(GroupDTO groupDTO) {
		if(StringUtils.isBlank(groupDTO.getGroupId())){
			saveGroup(groupDTO);
		}else {
			deleteGroup(groupDTO.getGroupId());
			saveGroup(groupDTO);
		}
		return ResultJson.ok();
	}

	private void saveGroup(GroupDTO groupDTO){
		String groupId = IdGenerate.uuid();
		GroupUser groupUser = new GroupUser();
		groupUser.setId(groupId);
		groupUser.setTreeLeaf("1");
		groupUser.setTreeLevel("1");
		groupUser.setLeaf(false);
		groupUser.setName(groupDTO.getGroupName());
		groupUserDao.insertGroupUser(groupUser);
		List<GroupUser> list = groupDTO.getGroupUsers();
		list.forEach(g->{
			g.setId(IdGenerate.uuid());
			g.setLeaf(true);
			g.setParentId(groupId);
			g.setTreeLevel("2");
			g.setTreeLeaf("2");
			groupUserDao.insertGroupUser(g);
		});
	}


	@Transactional
	@Override
	public ResultJson saveGroupUser(GroupUser groupUser) {
		groupUserDao.insertGroupUser(groupUser);
		return ResultJson.ok();
	}

	@Transactional
	@Override
	public ResultJson removeGroupUser(GroupUser groupUser) {
		groupUserDao.deleteUserById(groupUser.getId());
		return ResultJson.ok();
	}

	@Transactional
	@Override
	public ResultJson deleteGroup(String ids) {
		String[] groupIds = ids.split(",");
		for (String groupId : groupIds){
			GroupUser userGroup = getGroupUserById(groupId);
			if(StringUtils.isBlank(userGroup.getParentId())){
				groupUserDao.deleteUserByGroupId(groupId);
			}
			groupUserDao.deleteGroupByGroupId(groupId);
		}
		return ResultJson.ok();
	}
	@Transactional
	@Override
	public GroupUser getGroupUserById(String id){
		return groupUserDao.getGroupUserById(id);
	}

	@Override
	public List<GroupUser> getGroupUserAll() {
		return groupUserDao.getGroupUserAll();
	}
}
