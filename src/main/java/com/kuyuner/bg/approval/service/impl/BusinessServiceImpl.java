package com.kuyuner.bg.approval.service.impl;

import com.kuyuner.bg.approval.bean.BusinessHistoricListView;
import com.kuyuner.bg.approval.bean.BusinessPendingListView;
import com.kuyuner.bg.approval.dao.AccountsDao;
import com.kuyuner.bg.approval.dao.BusinessDao;
import com.kuyuner.bg.approval.entity.Accounts;
import com.kuyuner.bg.approval.entity.Business;
import com.kuyuner.bg.approval.service.BusinessService;
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

import java.util.List;
import java.util.Map;

/**
 * 业务申请Service层实现
 *
 * @author administrator
 */
@Service
public class BusinessServiceImpl implements BusinessService {

    @Autowired
    private BusinessDao businessDao;

    @Autowired
    private WorkFlowService workFlowService;

    @Autowired
    private AccountsDao accountsDao;

    @Override
    public Business get(String id) {
        return businessDao.get(new Business(id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultJson deletes(String... ids) {
        businessDao.deletes(ids);
        return ResultJson.ok();
    }

    @Override
    public Business getLog(String businessLogId) {
        Business businessLog = new Business();
        businessLog.setId(businessLogId);
        return businessDao.getLog(businessLog);
    }

    @Override
    public PageJson findPendingList(String pageNum, String pageSize, Business business,String userId) {
        Page<BusinessPendingListView> page = new Page<>(pageNum, pageSize);
        page.start();
        businessDao.findPendingList(business, UserUtils.getPrincipal() == null  ? userId : UserUtils.getPrincipal().getId());
        page.end();
        return new PageJson(page);
    }

    @Override
    public PageJson findHistoricList(String pageNum, String pageSize, Business business,String userId) {
        Page<BusinessHistoricListView> page = new Page<>(pageNum, pageSize);
        page.start();
        businessDao.findHistoricList(business,UserUtils.getPrincipal() == null  ? userId : UserUtils.getPrincipal().getId());
        page.end();
        return new PageJson(page);
    }

    @Override
    public PageJson findSendList(String pageNum, String pageSize, Business business,String userId) {
        Page<BusinessHistoricListView> page = new Page<>(pageNum, pageSize);
        page.start();
        businessDao.findSendList(business, UserUtils.getPrincipal() == null  ? userId : UserUtils.getPrincipal().getId());
        page.end();
        return new PageJson(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultJson submitForm(Business business, String taskResult) {
        //保存业务数据
        if (StringUtils.isBlank(business.getId())) {
            business.setId(IdGenerate.uuid());
            business.setSenderId(UserUtils.getPrincipal().getId());
            business.setCreater(UserUtils.getPrincipal().getId());
            businessDao.insert(business);
        } else {
            businessDao.update(business);
            business = businessDao.get(new Business(business.getId()));
        }


        handleForm(business, taskResult);

        return ResultJson.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultJson approvalForm(String id, String approvalResult, String taskResult) {
        Business business = new Business();
        business.setId(id);
        String approvalResultContent = businessDao.getApprovalResult(business.getId());
        TaskBean taskBean = JsonMapper.fromJsonString(taskResult, TaskBean.class);
        String str = "<span class=\"people_name\">%s</span>的处理意见：</br>%s；<span class=\"audit_result\">%s</span></br>";
        business.setApprovalResult((approvalResultContent == null ? "" : approvalResultContent) + String.format(str, UserUtils.getPrincipal().getName(), approvalResult, taskBean.getSequenceFlowName()));
        businessDao.update(business);

        business = businessDao.get(new Business(business.getId()));
        handleForm(business, taskResult);


        return ResultJson.ok();
    }

    @Override
    public List<Map<String, Object>> findAllUsers() {
        return businessDao.findAllUsers();
    }

    @Override
    public List<Map<String, Object>> findAllDepts() {
        return businessDao.findAllDepts();
    }

    /**
     * 提交任务
     *
     * @param business
     * @param taskResult
     */
    private void handleForm(Business business, String taskResult) {
        //记录日志
        Business businessLog = new Business();
        BeanUtils.copyProperties(business, businessLog);
        businessLog.setId(IdGenerate.uuid());
        businessDao.insertLog(businessLog);

        //提交流程
        BusinessKey businessKey = new BusinessKey();
        businessKey.setId(business.getId());
        businessKey.setLogId(businessLog.getId());
        TaskInfo taskInfo = workFlowService.submitTask(taskResult, businessKey);

        //更新业务表的当前环节字段
        Business business1 = new Business();
        business1.setId(business.getId());
        business1.setTaskName(taskInfo.getTaskName());
        businessDao.update(business1);

        if ("结束".equals(taskInfo.getTaskName())) {
            Business business2 = businessDao.get(new Business(business.getId()));
            Accounts accounts = new Accounts();
            accounts.setId(IdGenerate.uuid());
            accounts.setName(business2.getBusinessName());
            accounts.setSenderId(business2.getSenderId());
            accounts.setTotalPrice(String.valueOf(business2.getFunds()));
            accounts.setCreateDate(business2.getCreateDate());
            accountsDao.insert(accounts);
        }
    }
}