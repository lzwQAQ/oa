package com.kuyuner.bg.approval.service.impl;

import com.kuyuner.bg.approval.bean.LeaveHistoricListView;
import com.kuyuner.bg.approval.bean.LeavePendingListView;
import com.kuyuner.bg.approval.dao.LeaveDao;
import com.kuyuner.bg.approval.dao.LeaveRecordDao;
import com.kuyuner.bg.approval.entity.Leave;
import com.kuyuner.bg.approval.entity.LeaveRecord;
import com.kuyuner.bg.approval.service.LeaveService;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.common.idgen.IdGenerate;
import com.kuyuner.common.lang.StringUtils;
import com.kuyuner.common.mapper.JsonMapper;
import com.kuyuner.common.persistence.Page;
import com.kuyuner.core.sys.security.UserUtils;
import com.kuyuner.workflow.api.bean.BusinessKey;
import com.kuyuner.workflow.api.bean.TaskBean;
import com.kuyuner.workflow.api.bean.TaskInfo;
import com.kuyuner.workflow.api.service.WorkFlowService;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 请假Service层实现
 *
 * @author administrator
 */
@Service
public class LeaveServiceImpl implements LeaveService {

    @Autowired
    private LeaveDao leaveDao;

    @Autowired
    private LeaveRecordDao leaveRecordDao;

    @Autowired
    private WorkFlowService workFlowService;

    @Override
    public Leave get(String id) {
        return leaveDao.get(new Leave(id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultJson deletes(String... ids) {
        leaveDao.deletes(ids);
        return ResultJson.ok();
    }

    @Override
    public Leave getLog(String businessLogId) {
        Leave leaveLog = new Leave();
        leaveLog.setId(businessLogId);
        return leaveDao.getLog(leaveLog);
    }

    @Override
    public PageJson findPendingList(String pageNum, String pageSize, Leave leave,String userId) {
        Page<LeavePendingListView> page = new Page<>(pageNum, pageSize);
        page.start();
        leaveDao.findPendingList(leave,  UserUtils.getPrincipal()==null?userId:UserUtils.getPrincipal().getId());
        page.end();
        return new PageJson(page);
    }

    @Override
    public PageJson findHistoricList(String pageNum, String pageSize, Leave leave,String userId) {
        Page<LeaveHistoricListView> page = new Page<>(pageNum, pageSize);
        page.start();
        leaveDao.findHistoricList(leave,  UserUtils.getPrincipal()==null?userId:UserUtils.getPrincipal().getId());
        page.end();
        return new PageJson(page);
    }

    @Override
    public PageJson findSendList(String pageNum, String pageSize, Leave leave,String userId) {
        Page<LeavePendingListView> page = new Page<>(pageNum, pageSize);
        page.start();
        leaveDao.findSendList(leave,  UserUtils.getPrincipal()==null?userId:UserUtils.getPrincipal().getId());
        page.end();
        return new PageJson(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultJson submitForm(Leave leave, String taskResult) {
        //保存业务数据
        if (StringUtils.isBlank(leave.getId())) {
            leave.setId(IdGenerate.uuid());
            leave.setSenderId(UserUtils.getPrincipal().getId());
            leave.setCreater(UserUtils.getPrincipal().getId());
            leaveDao.insert(leave);
        } else {
            leaveDao.update(leave);
            leave = leaveDao.get(new Leave(leave.getId()));
        }


        handleForm(leave, taskResult);

        return ResultJson.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultJson approvalForm(String id, String approvalResult, String taskResult) {
        Leave leave = new Leave();
        leave.setId(id);
        String approvalResultContent = leaveDao.getApprovalResult(leave.getId());
        TaskBean taskBean = JsonMapper.fromJsonString(taskResult, TaskBean.class);
        String str = "<span class=\"people_name\">%s</span>的处理意见：</br>%s；<span class=\"audit_result\">%s</span></br>";
        leave.setApprovalResult((approvalResultContent == null ? "" : approvalResultContent) + String.format(str, UserUtils.getPrincipal().getName(), approvalResult, taskBean.getSequenceFlowName()));
        leaveDao.update(leave);

        leave = leaveDao.get(new Leave(leave.getId()));
        handleForm(leave, taskResult);

        return ResultJson.ok();
    }

    /**
     * 提交任务
     *
     * @param leave
     * @param taskResult
     */
    private void handleForm(Leave leave, String taskResult) {
        //记录日志
        Leave leaveLog = new Leave();
        BeanUtils.copyProperties(leave, leaveLog);
        leaveLog.setId(IdGenerate.uuid());
        leaveDao.insertLog(leaveLog);

        //提交流程
        BusinessKey businessKey = new BusinessKey();
        businessKey.setId(leave.getId());
        businessKey.setLogId(leaveLog.getId());
        TaskInfo taskInfo = workFlowService.submitTask(taskResult, businessKey);

        //更新业务表的当前环节字段
        Leave leave2 = new Leave();
        leave2.setId(leave.getId());
        leave2.setTaskName(taskInfo.getTaskName());
        leaveDao.update(leave2);

        if ("结束".equals(taskInfo.getTaskName())) {
            Leave leaveInfo = leaveDao.get(new Leave(leave.getId()));
            LeaveRecord leaveRecord = new LeaveRecord();
            leaveRecord.setId(IdGenerate.uuid());
            leaveRecord.setType(leaveInfo.getType());
            leaveRecord.setStartTime(leaveInfo.getStartTime());
            leaveRecord.setEndTime(leaveInfo.getEndTime());
            leaveRecord.setLeaveDay(leaveInfo.getLeaveDay());
            leaveRecord.setSenderId(leaveInfo.getSenderId());
            leaveRecordDao.insert(leaveRecord);
        }
    }
}