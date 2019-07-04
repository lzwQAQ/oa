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
public class ProduceFinanceServiceFacade implements ProduceFaced {

    @Autowired
    private FinanceDao financeDao;




    @Override
    public PageJson findPendingList(String pageNum, String pageSize,String senderName,String businessName,String leaveType,String userId) {
        Page<FinancePendingListView> page = new Page<>(pageNum, pageSize);
        page.start();
        Finance finance = new Finance();
        finance.setSenderName(senderName);
        finance.setBusiness(businessName);
        financeDao.findPendingList(finance, UserUtils.getPrincipal().getId());
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

}