package com.kuyuner.simpleworkflow.service.impl;

import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.common.idgen.IdGenerate;
import com.kuyuner.common.lang.StringUtils;
import com.kuyuner.common.mapper.JsonMapper;
import com.kuyuner.common.persistence.Page;
import com.kuyuner.core.sys.security.UserUtils;
import com.kuyuner.simpleworkflow.bean.SimpleWorkflowHistoricListView;
import com.kuyuner.simpleworkflow.bean.SimpleWorkflowPendingListView;
import com.kuyuner.simpleworkflow.dao.SimpleWorkflowDao;
import com.kuyuner.simpleworkflow.entity.SimpleWorkflow;
import com.kuyuner.simpleworkflow.service.SimpleWorkflowService;
import com.kuyuner.workflow.api.bean.BusinessKey;
import com.kuyuner.workflow.api.bean.TaskBean;
import com.kuyuner.workflow.api.bean.TaskInfo;
import com.kuyuner.workflow.api.service.WorkFlowService;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 简单流程Service层实现
 *
 * @author administrator
 */
@Service
public class SimpleWorkflowServiceImpl implements SimpleWorkflowService {

    @Autowired
    private SimpleWorkflowDao simpleWorkflowDao;

    @Autowired
    private WorkFlowService workFlowService;

    @Override
    public SimpleWorkflow get(String id) {
        return simpleWorkflowDao.get(new SimpleWorkflow(id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultJson submitForm(SimpleWorkflow simpleWorkflow, String taskResult, String modelKey,String userId) {

        //保存业务数据
        if (StringUtils.isBlank(simpleWorkflow.getId())) {
            simpleWorkflow.setId(IdGenerate.uuid());
            simpleWorkflow.setCreater(UserUtils.getPrincipal().getId());
            simpleWorkflow.setSenderId(UserUtils.getPrincipal().getId());
            simpleWorkflow.setModelId(simpleWorkflowDao.getModelId(modelKey));
            simpleWorkflowDao.insertSimple(simpleWorkflow);
        } else {
            simpleWorkflowDao.update(simpleWorkflow);
            simpleWorkflow = simpleWorkflowDao.get(new SimpleWorkflow(simpleWorkflow.getId()));
        }


        submitForm(simpleWorkflow, taskResult,userId);

        return ResultJson.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultJson deletes(String... ids) {
        simpleWorkflowDao.deletes(ids);
        return ResultJson.ok();
    }

    @Override
    public PageJson findPendingList(String pageNum, String pageSize, SimpleWorkflow simpleWorkflow) {
        Page<SimpleWorkflowPendingListView> page = new Page<>(pageNum, pageSize);
        page.start();
        simpleWorkflowDao.findPendingList(simpleWorkflow, UserUtils.getPrincipal().getId());
        page.end();
        return new PageJson(page);
    }

    @Override
    public PageJson findHistoricList(String pageNum, String pageSize, SimpleWorkflow simpleWorkflow) {
        Page<SimpleWorkflowHistoricListView> page = new Page<>(pageNum, pageSize);
        page.start();
        simpleWorkflowDao.findHistoricList(simpleWorkflow, UserUtils.getPrincipal().getId());
        page.end();
        return new PageJson(page);
    }

    @Override
    public PageJson findSendList(String pageNum, String pageSize, SimpleWorkflow simpleWorkflow) {
        Page<SimpleWorkflowHistoricListView> page = new Page<>(pageNum, pageSize);
        page.start();
        simpleWorkflowDao.findSendList(simpleWorkflow, UserUtils.getPrincipal().getId());
        page.end();
        return new PageJson(page);
    }

    @Override
    public SimpleWorkflow getLog(String businessLogId) {
        SimpleWorkflow workflowLog = new SimpleWorkflow();
        workflowLog.setId(businessLogId);
        return simpleWorkflowDao.getLog(workflowLog);
    }

    @Override
    public ResultJson approvalForm(String id, String approvalResult, String taskResult, String modelKey,String userId) {
        SimpleWorkflow simpleWorkflow = new SimpleWorkflow();
        simpleWorkflow.setId(id);
        String approvalResultContent = simpleWorkflowDao.getApprovalResult(simpleWorkflow.getId());
        TaskBean taskBean = JsonMapper.fromJsonString(taskResult, TaskBean.class);
        String str = "<span class=\"people_name\">%s</span>的处理意见：</br>%s；<span class=\"audit_result\">%s</span></br>";
        simpleWorkflow.setApprovalResult((approvalResultContent == null ? "" : approvalResultContent) + String.format(str, UserUtils.getPrincipal().getName(), approvalResult, taskBean.getSequenceFlowName()));
        simpleWorkflowDao.update(simpleWorkflow);

        simpleWorkflow = simpleWorkflowDao.get(new SimpleWorkflow(simpleWorkflow.getId()));
        submitForm(simpleWorkflow, taskResult,userId);


        return ResultJson.ok();
    }

    /**
     * 提交任务
     *
     * @param simpleWorkflow
     * @param taskResult
     */
    @Override
    public void submitForm(SimpleWorkflow simpleWorkflow, String taskResult, String userId) {
        //记录日志
        SimpleWorkflow simpleWorkflowLog = new SimpleWorkflow();
        BeanUtils.copyProperties(simpleWorkflow, simpleWorkflowLog);
        simpleWorkflowLog.setId(IdGenerate.uuid());
        simpleWorkflowDao.insertLog(simpleWorkflowLog);

        //提交流程
        BusinessKey businessKey = new BusinessKey();
        businessKey.setId(simpleWorkflow.getId());
        businessKey.setLogId(simpleWorkflowLog.getId());
        TaskInfo taskInfo = workFlowService.submitTask(taskResult, businessKey,userId);

        //更新业务表的当前环节字段
        SimpleWorkflow workflow = new SimpleWorkflow();
        workflow.setId(simpleWorkflow.getId());
        workflow.setTaskName(taskInfo.getTaskName());
        simpleWorkflowDao.update(workflow);
    }

}