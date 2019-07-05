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
import com.kuyuner.core.sys.security.UserUtils;
import com.kuyuner.workflow.api.bean.BusinessKey;
import com.kuyuner.workflow.api.bean.TaskBean;
import com.kuyuner.workflow.api.bean.TaskInfo;
import com.kuyuner.workflow.api.service.WorkFlowService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}