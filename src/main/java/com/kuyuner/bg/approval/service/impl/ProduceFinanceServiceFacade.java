package com.kuyuner.bg.approval.service.impl;

import com.kuyuner.bg.approval.bean.FinanceHistoricListView;
import com.kuyuner.bg.approval.bean.FinancePendingListView;
import com.kuyuner.bg.approval.dao.AccountsDao;
import com.kuyuner.bg.approval.dao.FinanceDao;
import com.kuyuner.bg.approval.entity.Accounts;
import com.kuyuner.bg.approval.entity.Finance;
import com.kuyuner.bg.approval.service.FinanceService;
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
 * 财务申请Service层实现
 *
 * @author administrator
 */
@Component
public class ProduceFinanceServiceFacade implements ProduceFaced {

    @Autowired
    private FinanceDao financeDao;
    @Autowired
    private FinanceService financeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private UserService userService;


    @Override
    public PageJson findPendingList(String pageNum, String pageSize,String senderName,String businessName,String leaveType,String userId) {
        Page<FinancePendingListView> page = new Page<>(pageNum, pageSize);
        page.start();
        Finance finance = new Finance();
        finance.setSenderName(senderName);
        finance.setBusiness(businessName);
        financeDao.findPendingList(finance,  UserUtils.getPrincipal()==null?userId:UserUtils.getPrincipal().getId());
        page.end();
        return new PageJson(page);
    }

    @Override
    public PageJson findHistoricList(String pageNum, String pageSize,String senderName,String businessName,String leaveType,String userId) {
        Page<FinanceHistoricListView> page = new Page<>(pageNum, pageSize);
        page.start();
        Finance finance = new Finance();
        finance.setSenderName(senderName);
        finance.setBusiness(businessName);
        financeDao.findHistoricList(finance, UserUtils.getPrincipal() == null?userId:UserUtils.getPrincipal().getId());
        page.end();
        return new PageJson(page);
    }

    @Override
    public PageJson findSendList(String pageNum, String pageSize,String senderName,String businessName,String leaveType,String userId) {
        Page<FinancePendingListView> page = new Page<>(pageNum, pageSize);
        page.start();
        Finance finance = new Finance();
        finance.setSenderName(senderName);
        finance.setBusiness(businessName);
        financeDao.findHistoricList(finance, UserUtils.getPrincipal() == null?userId:UserUtils.getPrincipal().getId());
        financeDao.findSendList(finance,UserUtils.getPrincipal() == null?userId:UserUtils.getPrincipal().getId());
        page.end();
        return new PageJson(page);
    }

    @Override
    public String getCode() {
        return "finance";
    }

    @Override
    public ResultJson submit(String body, String taskResult, String userId,String goods, String modelKey) {
        if(UserUtils.getPrincipal()==null && StringUtils.isBlank(userId)){
            return ResultJson.failed("请传入用户信息");
        }
        Finance finance = GfJsonUtil.parseObject(body,Finance.class);
        //保存业务数据
        if (StringUtils.isBlank(finance.getId())) {
            finance.setId(IdGenerate.uuid());
            finance.setSenderId(UserUtils.getPrincipal() == null ? userId : UserUtils.getPrincipal().getId());
            finance.setCreater(UserUtils.getPrincipal() == null ? userId : UserUtils.getPrincipal().getId());
            financeDao.insert(finance);
        } else {
            financeDao.update(finance);
            finance = financeDao.get(new Finance(finance.getId()));
        }


        financeService.handleForm(finance, taskResult,userId);

        return ResultJson.ok();
    }

    @Override
    public ResultJson approvalForm(String id, String approvalResult, String taskResult, String userId) {
        Finance finance = new Finance();
        finance.setId(id);
        String approvalResultContent = financeDao.getApprovalResult(finance.getId());
        TaskBean taskBean = JsonMapper.fromJsonString(taskResult, TaskBean.class);
        User user = userService.get(userId);
        String str = "<span class=\"people_name\">%s</span>的处理意见：</br>%s；<span class=\"audit_result\">%s</span></br>";
        finance.setApprovalResult((approvalResultContent == null ? "" : approvalResultContent) + String.format(str, user.getName(), approvalResult, taskBean.getSequenceFlowName()));
        financeDao.update(finance);

        finance = financeDao.get(new Finance(finance.getId()));
        financeService.handleForm(finance, taskResult,userId);

        return ResultJson.ok();
    }

    @Override
    public ResultJson detail(String produceType, String businessId, String firstTask, String type, String taskId,
                             String modelKey, String startSequenceFlowName, String userId) {
        Map map = new HashMap();
        ResultJson result = taskService.getFormPath(modelKey,startSequenceFlowName,taskId);
        map.put("work",result.getData());
        if ("historic".equals(type)) {
            map.put("data", financeService.getLog(businessId));
        } else {
            if (StringUtils.isNotBlank(businessId)) {
                map.put("data", financeService.get(businessId));
            } else {
                User user = userService.get(UserUtils.getPrincipal() == null ? userId : UserUtils.getPrincipal().getId());
                Finance finance = new Finance();
                finance.setSenderName(user.getName());
                finance.setSenderDeptName(user.getDept() != null ? user.getDept().getName() : "");
                map.put("data", finance);
            }
        }
        return ResultJson.ok(map);
    }

}