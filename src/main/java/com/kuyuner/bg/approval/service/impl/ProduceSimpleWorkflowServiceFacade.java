package com.kuyuner.bg.approval.service.impl;

import com.kuyuner.bg.approval.service.ProduceFaced;
import com.kuyuner.bg.msg.util.HtmlRegexpUtil;
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
import com.kuyuner.simpleworkflow.bean.SimpleWorkflowHistoricListView;
import com.kuyuner.simpleworkflow.bean.SimpleWorkflowPendingListView;
import com.kuyuner.simpleworkflow.dao.SimpleWorkflowDao;
import com.kuyuner.simpleworkflow.entity.SimpleWorkflow;
import com.kuyuner.simpleworkflow.service.SimpleWorkflowService;
import com.kuyuner.workflow.api.bean.BusinessKey;
import com.kuyuner.workflow.api.bean.TaskBean;
import com.kuyuner.workflow.api.bean.TaskInfo;
import com.kuyuner.workflow.api.service.WorkFlowService;
import com.kuyuner.workflow.runtime.service.PendingTaskService;
import com.kuyuner.workflow.runtime.service.TaskService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 简单流程Service层实现
 *
 * @author administrator
 */
@Service
public class ProduceSimpleWorkflowServiceFacade implements ProduceFaced {

    @Autowired
    private SimpleWorkflowDao simpleWorkflowDao;
    @Autowired
    private SimpleWorkflowService simpleWorkflowService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private UserService userService;


    @Override
    public PageJson findPendingList(String pageNum, String pageSize, String senderName,String businessName,String leaveType, String userId) {
        Page<SimpleWorkflowPendingListView> page = new Page<>(pageNum, pageSize);
        page.start();
        SimpleWorkflow simpleWorkflow = new SimpleWorkflow();
        simpleWorkflow.setTitle(businessName);
        simpleWorkflowDao.findPendingList(simpleWorkflow, UserUtils.getPrincipal()==null?userId:UserUtils.getPrincipal().getId());
        page.end();
        return new PageJson(page);
    }

    @Override
    public PageJson findHistoricList(String pageNum, String pageSize, String senderName,String businessName,String leaveType, String userId) {
        Page<SimpleWorkflowHistoricListView> page = new Page<>(pageNum, pageSize);
        page.start();
        SimpleWorkflow simpleWorkflow = new SimpleWorkflow();
        simpleWorkflow.setTitle(businessName);
        simpleWorkflowDao.findHistoricList(simpleWorkflow,UserUtils.getPrincipal()==null?userId:UserUtils.getPrincipal().getId());
        page.end();
        return new PageJson(page);
    }

    @Override
    public PageJson findSendList(String pageNum, String pageSize, String senderName,String businessName,String leaveType, String userId) {
        Page<SimpleWorkflowHistoricListView> page = new Page<>(pageNum, pageSize);
        page.start();
        SimpleWorkflow simpleWorkflow = new SimpleWorkflow();
        simpleWorkflow.setTitle(businessName);
        simpleWorkflowDao.findSendList(simpleWorkflow, UserUtils.getPrincipal()==null?userId:UserUtils.getPrincipal().getId());
        page.end();
        return new PageJson(page);
    }

    @Override
    public String getCode() {
        return "simple";
    }

    @Override
    public ResultJson submit(String body, String taskResult, String userId,String goods,String modelKey) {
        if(UserUtils.getPrincipal()==null && StringUtils.isBlank(userId)){
            return ResultJson.failed("请传入用户信息");
        }
        SimpleWorkflow simpleWorkflow = GfJsonUtil.parseObject(body,SimpleWorkflow.class);
        //保存业务数据
        if (StringUtils.isBlank(simpleWorkflow.getId())) {
            simpleWorkflow.setId(IdGenerate.uuid());
            simpleWorkflow.setCreater(UserUtils.getPrincipal() == null ? userId : UserUtils.getPrincipal().getId());
            simpleWorkflow.setSenderId(UserUtils.getPrincipal() == null ? userId : UserUtils.getPrincipal().getId());
            simpleWorkflow.setModelId(simpleWorkflowDao.getModelId(modelKey));
            simpleWorkflowDao.insertSimple(simpleWorkflow);
        } else {
            simpleWorkflowDao.update(simpleWorkflow);
            simpleWorkflow = simpleWorkflowDao.get(new SimpleWorkflow(simpleWorkflow.getId()));
        }


        simpleWorkflowService.submitForm(simpleWorkflow, taskResult,userId);

        return ResultJson.ok();
    }

    @Override
    public ResultJson approvalForm(String id, String approvalResult, String taskResult, String userId) {
        SimpleWorkflow simpleWorkflow = new SimpleWorkflow();
        simpleWorkflow.setId(id);
        String approvalResultContent = simpleWorkflowDao.getApprovalResult(simpleWorkflow.getId());
        TaskBean taskBean = JsonMapper.fromJsonString(taskResult, TaskBean.class);
        String str = "<span class=\"people_name\">%s</span>的处理意见：</br>%s；<span class=\"audit_result\">%s</span></br>";
        User user = userService.get(userId);
        simpleWorkflow.setApprovalResult((approvalResultContent == null ? "" : approvalResultContent) + String.format(str, user.getName(), approvalResult, taskBean.getSequenceFlowName()));
        simpleWorkflowDao.update(simpleWorkflow);

        simpleWorkflow = simpleWorkflowDao.get(new SimpleWorkflow(simpleWorkflow.getId()));
        simpleWorkflowService.submitForm(simpleWorkflow, taskResult,userId);


        return ResultJson.ok();
    }

    @Override
    public ResultJson detail(String produceType, String businessId, String firstTask, String type, String taskId,
                             String modelKey, String startSequenceFlowName, String userId) {
        Map map = new HashMap();
        ResultJson result = taskService.getFormPath(modelKey,startSequenceFlowName,taskId);
        map.put("work",result.getData());
        if(StringUtils.isBlank(businessId) || "null".equals(businessId)){
            return ResultJson.ok(map);
        }
        if ("historic".equals(type)) {
            map.put("data", simpleWorkflowService.getLog(businessId));
        } else {
            if (StringUtils.isNotBlank(businessId)) {
                SimpleWorkflow simpleWorkflow = simpleWorkflowService.get(businessId);
                simpleWorkflow.setApprovalResult(HtmlRegexpUtil.filterHtml(simpleWorkflow.getApprovalResult()));
                map.put("data", simpleWorkflow);
            } else {
                User user = userService.get(UserUtils.getPrincipal()==null?userId:UserUtils.getPrincipal().getId());
                SimpleWorkflow workflow = new SimpleWorkflow();
                workflow.setSenderName(user.getUsername());
                workflow.setSenderDeptName(user.getDept() != null ? user.getDept().getName() : "");
                map.put("data", workflow);
            }
        }
        return ResultJson.ok(map);
    }


}