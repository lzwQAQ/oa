package com.kuyuner.bg.approval.service.impl;

import com.kuyuner.bg.approval.bean.FinanceHistoricListView;
import com.kuyuner.bg.approval.bean.FinancePendingListView;
import com.kuyuner.bg.approval.dao.AccountsDao;
import com.kuyuner.bg.approval.dao.FinanceDao;
import com.kuyuner.bg.approval.entity.Accounts;
import com.kuyuner.bg.approval.entity.Finance;
import com.kuyuner.bg.approval.service.FinanceService;
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
 * 财务申请Service层实现
 *
 * @author administrator
 */
@Service
public class FinanceServiceImpl implements FinanceService {

    @Autowired
    private FinanceDao financeDao;

    @Autowired
    private AccountsDao accountsDao;

    @Autowired
    private WorkFlowService workFlowService;

    @Override
    public Finance get(String id) {
        return financeDao.get(new Finance(id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultJson deletes(String... ids) {
        financeDao.deletes(ids);
        return ResultJson.ok();
    }

    @Override
    public Finance getLog(String businessLogId) {
        Finance financeLog = new Finance();
        financeLog.setId(businessLogId);
        return financeDao.getLog(financeLog);
    }

    @Override
    public PageJson findPendingList(String pageNum, String pageSize, Finance finance) {
        Page<FinancePendingListView> page = new Page<>(pageNum, pageSize);
        page.start();
        financeDao.findPendingList(finance, UserUtils.getPrincipal().getId());
        page.end();
        return new PageJson(page);
    }

    @Override
    public PageJson findHistoricList(String pageNum, String pageSize, Finance finance) {
        Page<FinanceHistoricListView> page = new Page<>(pageNum, pageSize);
        page.start();
        financeDao.findHistoricList(finance, UserUtils.getPrincipal().getId());
        page.end();
        return new PageJson(page);
    }

    @Override
    public PageJson findSendList(String pageNum, String pageSize, Finance finance) {
        Page<FinancePendingListView> page = new Page<>(pageNum, pageSize);
        page.start();
        financeDao.findSendList(finance, UserUtils.getPrincipal().getId());
        page.end();
        return new PageJson(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultJson submitForm(Finance finance, String taskResult,String userId) {
        //保存业务数据
        if (StringUtils.isBlank(finance.getId())) {
            finance.setId(IdGenerate.uuid());
            finance.setSenderId(UserUtils.getPrincipal().getId());
            finance.setCreater(UserUtils.getPrincipal().getId());
            financeDao.insert(finance);
        } else {
            financeDao.update(finance);
            finance = financeDao.get(new Finance(finance.getId()));
        }


        handleForm(finance, taskResult,userId);

        return ResultJson.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultJson approvalForm(String id, String approvalResult, String taskResult,String userId) {
        Finance finance = new Finance();
        finance.setId(id);
        String approvalResultContent = financeDao.getApprovalResult(finance.getId());
        TaskBean taskBean = JsonMapper.fromJsonString(taskResult, TaskBean.class);
        String str = "<span class=\"people_name\">%s</span>的处理意见：</br>%s；<span class=\"audit_result\">%s</span></br>";
        finance.setApprovalResult((approvalResultContent == null ? "" : approvalResultContent) + String.format(str, UserUtils.getPrincipal().getName(), approvalResult, taskBean.getSequenceFlowName()));
        financeDao.update(finance);

        finance = financeDao.get(new Finance(finance.getId()));
        handleForm(finance, taskResult,userId);

        return ResultJson.ok();
    }

    /**
     * 提交任务
     *
     * @param finance
     * @param taskResult
     */
    @Override
    public void handleForm(Finance finance, String taskResult, String userId) {
        //记录日志
        Finance financeLog = new Finance();
        BeanUtils.copyProperties(finance, financeLog);
        financeLog.setId(IdGenerate.uuid());
        financeDao.insertLog(financeLog);

        //提交流程
        BusinessKey businessKey = new BusinessKey();
        businessKey.setId(finance.getId());
        businessKey.setLogId(financeLog.getId());
        TaskInfo taskInfo = workFlowService.submitTask(taskResult, businessKey,userId);

        //更新业务表的当前环节字段
        Finance finance1 = new Finance();
        finance1.setId(finance.getId());
        finance1.setTaskName(taskInfo.getTaskName());
        financeDao.update(finance1);

        if ("结束".equals(taskInfo.getTaskName())) {
            Finance finance2 = financeDao.get(new Finance(finance.getId()));
            Accounts accounts = new Accounts();
            accounts.setId(IdGenerate.uuid());
            accounts.setName(finance2.getBusiness());
            accounts.setSenderId(finance2.getSenderId());
            accounts.setTotalPrice(String.valueOf(finance2.getMoney()));
            accounts.setCreateDate(finance2.getCreateDate());
            accountsDao.insert(accounts);
        }
    }
}