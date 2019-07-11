package com.kuyuner.bg.approval.service.impl;

import com.kuyuner.bg.approval.bean.LeaveHistoricListView;
import com.kuyuner.bg.approval.bean.LeavePendingListView;
import com.kuyuner.bg.approval.dao.LeaveDao;
import com.kuyuner.bg.approval.entity.Leave;
import com.kuyuner.bg.approval.service.LeaveService;
import com.kuyuner.bg.approval.service.ProduceFaced;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.common.idgen.IdGenerate;
import com.kuyuner.common.lang.StringUtils;
import com.kuyuner.common.mapper.JsonMapper;
import com.kuyuner.common.persistence.Page;
import com.kuyuner.common.utils.GfJsonUtil;
import com.kuyuner.core.sys.entity.User;
import com.kuyuner.core.sys.security.UserUtils;
import com.kuyuner.core.sys.service.UserService;
import com.kuyuner.workflow.api.bean.TaskBean;
import com.kuyuner.workflow.runtime.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 请假Service层实现
 *
 * @author administrator
 */
@Component
public class ProduceLeaveServiceFacade implements ProduceFaced {

    @Autowired
    private LeaveDao leaveDao;
    @Autowired
    private LeaveService leaveService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private UserService userService;


    @Override
    public PageJson findPendingList(String pageNum, String pageSize, String senderName,String businessName,String leaveType,String userId) {
        Page<LeavePendingListView> page = new Page<>(pageNum, pageSize);
        page.start();
        Leave leave = new Leave();
        leave.setSenderName(senderName);
        leave.setType(leaveType);
        leaveDao.findPendingList(leave,  UserUtils.getPrincipal()==null?userId:UserUtils.getPrincipal().getId());
        page.end();
        return new PageJson(page);
    }

    @Override
    public PageJson findHistoricList(String pageNum, String pageSize,String senderName,String businessName,String leaveType,String userId) {
        Page<LeaveHistoricListView> page = new Page<>(pageNum, pageSize);
        page.start();
        Leave leave = new Leave();
        leave.setType(leaveType);
        leave.setSenderName(senderName);
        leaveDao.findHistoricList(leave,  UserUtils.getPrincipal()==null?userId:UserUtils.getPrincipal().getId());
        page.end();
        return new PageJson(page);
    }

    @Override
    public PageJson findSendList(String pageNum, String pageSize,String senderName,String businessName,String leaveType,String userId) {
        Page<LeavePendingListView> page = new Page<>(pageNum, pageSize);
        page.start();
        Leave leave = new Leave();
        leave.setType(leaveType);
        leave.setSenderName(senderName);
        leaveDao.findSendList(leave,  UserUtils.getPrincipal()==null?userId:UserUtils.getPrincipal().getId());
        page.end();
        return new PageJson(page);
    }

    @Override
    public String getCode() {
        return "leave";
    }

    @Override
    public ResultJson submit(String body, String taskResult, String userId,String goods, String modelKey) {
        //保存业务数据
        if(UserUtils.getPrincipal()==null && StringUtils.isBlank(userId)){
            return ResultJson.failed("请传入用户信息");
        }
        Leave leave = GfJsonUtil.parseObject(body,Leave.class);
        if (StringUtils.isBlank(leave.getId())) {
            leave.setId(IdGenerate.uuid());
            leave.setSenderId(UserUtils.getPrincipal()==null?userId:UserUtils.getPrincipal().getId());
            leave.setCreater(UserUtils.getPrincipal()==null?userId:UserUtils.getPrincipal().getId());
            leaveDao.insert(leave);
        } else {
            leaveDao.update(leave);
            leave = leaveDao.get(new Leave(leave.getId()));
        }
        leaveService.handleForm(leave, taskResult,userId);

        return ResultJson.ok();
    }

    @Override
    public ResultJson approvalForm(String id, String approvalResult, String taskResult, String userId) {
        Leave leave = new Leave();
        leave.setId(id);
        String approvalResultContent = leaveDao.getApprovalResult(leave.getId());
        TaskBean taskBean = JsonMapper.fromJsonString(taskResult, TaskBean.class);
        User user = userService.get(userId);
        String str = "<span class=\"people_name\">%s</span>的处理意见：</br>%s；<span class=\"audit_result\">%s</span></br>";
        leave.setApprovalResult((approvalResultContent == null ? "" : approvalResultContent) + String.format(str, user.getName(), approvalResult, taskBean.getSequenceFlowName()));
        leaveDao.update(leave);

        leave = leaveDao.get(new Leave(leave.getId()));
        leaveService.handleForm(leave, taskResult,userId);

        return ResultJson.ok();
    }

    @Override
    public ResultJson detail(String produceType, String businessId, String firstTask, String type, String taskId,
                             String modelKey, String startSequenceFlowName, String userId) {
        Map map = new HashMap();
        ResultJson result = taskService.getFormPath(modelKey,startSequenceFlowName,taskId);
        map.put("work",result.getData());
        if ("historic".equals(type)) {
            map.put("data", leaveService.getLog(businessId));
        } else {
            if (StringUtils.isNotBlank(businessId)) {
                map.put("data", leaveService.get(businessId));
            } else {
                User user = userService.get(UserUtils.getPrincipal()==null?userId:UserUtils.getPrincipal().getId());
                Leave leave = new Leave();
                leave.setSenderName(user.getName());
                leave.setSenderDeptName(user.getDept() != null ? user.getDept().getName() : "");
                map.put("data", leave);
            }
        }
        return ResultJson.ok(map);
    }
}