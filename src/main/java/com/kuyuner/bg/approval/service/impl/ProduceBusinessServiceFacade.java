package com.kuyuner.bg.approval.service.impl;

import com.kuyuner.bg.approval.bean.BusinessHistoricListView;
import com.kuyuner.bg.approval.bean.BusinessPendingListView;
import com.kuyuner.bg.approval.dao.AccountsDao;
import com.kuyuner.bg.approval.dao.BusinessDao;
import com.kuyuner.bg.approval.entity.Accounts;
import com.kuyuner.bg.approval.entity.Business;
import com.kuyuner.bg.approval.service.BusinessService;
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
import com.kuyuner.workflow.api.bean.BusinessKey;
import com.kuyuner.workflow.api.bean.TaskBean;
import com.kuyuner.workflow.api.bean.TaskInfo;
import com.kuyuner.workflow.api.service.WorkFlowService;
import com.kuyuner.workflow.runtime.service.PendingTaskService;
import com.kuyuner.workflow.runtime.service.TaskService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 业务申请Service层实现
 *
 * @author administrator
 */
@Component
public class ProduceBusinessServiceFacade implements ProduceFaced {

    @Autowired
    private BusinessDao businessDao;
    @Autowired
    private BusinessService businessService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private UserService userService;

    @Override
    public PageJson findPendingList(String pageNum, String pageSize, String senderName, String businessName, String leaveType, String userId) {
        Page<BusinessPendingListView> page = new Page<>(pageNum, pageSize);
        page.start();
        Business business = new Business();
        business.setSenderName(senderName);
        business.setBusinessName(businessName);
        businessDao.findPendingList(business, UserUtils.getPrincipal() == null  ? userId : UserUtils.getPrincipal().getId());
        page.end();
        return new PageJson(page);
    }

    @Override
    public PageJson findHistoricList(String pageNum, String pageSize, String senderName, String businessName, String leaveType, String userId) {
        Page<BusinessHistoricListView> page = new Page<>(pageNum, pageSize);
        page.start();
        Business business = new Business();
        business.setSenderName(senderName);
        business.setBusinessName(businessName);
        businessDao.findHistoricList(business,UserUtils.getPrincipal() == null  ? userId : UserUtils.getPrincipal().getId());
        page.end();
        return new PageJson(page);
    }

    @Override
    public PageJson findSendList(String pageNum, String pageSize, String senderName, String businessName, String leaveType, String userId) {
        Page<BusinessHistoricListView> page = new Page<>(pageNum, pageSize);
        page.start();
        Business business = new Business();
        business.setSenderName(senderName);
        business.setBusinessName(businessName);
        businessDao.findSendList(business, UserUtils.getPrincipal() == null  ? userId : UserUtils.getPrincipal().getId());
        page.end();
        return new PageJson(page);
    }
    @Override

    public String getCode() {
        return "business";
    }

    @Override
    public ResultJson submit(String body, String taskResult, String userId,String goods, String modelKey) {
        if(UserUtils.getPrincipal()==null && StringUtils.isBlank(userId)){
            return ResultJson.failed("请传入用户信息");
        }
        Business business = GfJsonUtil.parseObject(body,Business.class);
        //保存业务数据
        if (StringUtils.isBlank(business.getId())) {
            business.setId(IdGenerate.uuid());
            business.setSenderId(UserUtils.getPrincipal() == null ? userId : UserUtils.getPrincipal().getId());
            business.setCreater(UserUtils.getPrincipal() == null ? userId : UserUtils.getPrincipal().getId());
            businessDao.insert(business);
        } else {
            businessDao.update(business);
            business = businessDao.get(new Business(business.getId()));
        }
        businessService.handleForm(business, taskResult,userId);
        return ResultJson.ok();
    }

    @Override
    public ResultJson approvalForm(String id, String approvalResult, String taskResult, String userId) {
        Business business = new Business();
        business.setId(id);
        String approvalResultContent = businessDao.getApprovalResult(business.getId());
        TaskBean taskBean = JsonMapper.fromJsonString(taskResult, TaskBean.class);
        String str = "<span class=\"people_name\">%s</span>的处理意见：</br>%s；<span class=\"audit_result\">%s</span></br>";
        User user = userService.get(userId);
        business.setApprovalResult((approvalResultContent == null ? "" : approvalResultContent) + String.format(str, user.getName(), approvalResult, taskBean.getSequenceFlowName()));
        businessDao.update(business);

        business = businessDao.get(new Business(business.getId()));
        businessService.handleForm(business, taskResult,userId);


        return ResultJson.ok();
    }

    @Override
    public ResultJson detail(String produceType, String businessId, String firstTask, String type, String taskId,
                             String modelKey, String startSequenceFlowName, String userId) {
        Map map = new HashMap();
        ResultJson result = taskService.getFormPath(modelKey,startSequenceFlowName,taskId);
        map.put("work",result.getData());
        if ("historic".equals(type)) {
            map.put("data", businessService.getLog(businessId));
        } else {
            if (StringUtils.isNotBlank(businessId)) {
                map.put("data", businessService.get(businessId));
            } else {
                User user = userService.get(UserUtils.getPrincipal() == null ? userId : UserUtils.getPrincipal().getId());
                Business business = new Business();
                business.setSenderName(user.getName());
                business.setSenderDeptName(user.getDept() != null ? user.getDept().getName() : "");
                map.put("data", business);
            }
        }
        return ResultJson.ok(map);
    }
}