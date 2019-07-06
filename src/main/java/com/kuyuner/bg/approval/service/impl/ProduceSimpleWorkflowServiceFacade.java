package com.kuyuner.bg.approval.service.impl;

import com.kuyuner.bg.approval.service.ProduceFaced;
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
public class ProduceSimpleWorkflowServiceFacade implements ProduceFaced {

    @Autowired
    private SimpleWorkflowDao simpleWorkflowDao;



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


}