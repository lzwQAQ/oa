package com.kuyuner.bg.approval.service.impl;

import com.kuyuner.bg.approval.bean.PurchaseHistoricListView;
import com.kuyuner.bg.approval.bean.PurchasePendingListView;
import com.kuyuner.bg.approval.dao.AccountsDao;
import com.kuyuner.bg.approval.dao.PurchaseDao;
import com.kuyuner.bg.approval.dao.PurchaseGoodsDao;
import com.kuyuner.bg.approval.entity.Accounts;
import com.kuyuner.bg.approval.entity.Purchase;
import com.kuyuner.bg.approval.entity.PurchaseGoods;
import com.kuyuner.bg.approval.service.ProduceFaced;
import com.kuyuner.bg.approval.service.PurchaseService;
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

/**
 * 采购申请Service层实现
 *
 * @author administrator
 */
@Component
public class ProducePurchaseServiceFacade implements ProduceFaced {

    @Autowired
    private PurchaseDao purchaseDao;

    @Override
    public PageJson findPendingList(String pageNum, String pageSize, String senderName,String businessName,String leaveType, String userId) {
        Page<PurchasePendingListView> page = new Page<>(pageNum, pageSize);
        page.start();
        Purchase purchase = new Purchase();
        purchase.setSenderName(senderName);
        purchase.setName(businessName);
        purchaseDao.findPendingList(purchase, UserUtils.getPrincipal() == null?userId:UserUtils.getPrincipal().getId());
        page.end();
        return new PageJson(page);
    }

    @Override
    public PageJson findHistoricList(String pageNum, String pageSize, String senderName,String businessName,String leaveType, String userId) {
        Page<PurchaseHistoricListView> page = new Page<>(pageNum, pageSize);
        page.start();
        Purchase purchase = new Purchase();
        purchase.setSenderName(senderName);
        purchase.setName(businessName);
        purchaseDao.findHistoricList(purchase,UserUtils.getPrincipal() == null?userId:UserUtils.getPrincipal().getId());
        page.end();
        return new PageJson(page);
    }

    @Override
    public PageJson findSendList(String pageNum, String pageSize, String senderName,String businessName,String leaveType, String userId) {
        Page<PurchaseHistoricListView> page = new Page<>(pageNum, pageSize);
        page.start();
        Purchase purchase = new Purchase();
        purchase.setSenderName(senderName);
        purchase.setName(businessName);
        purchaseDao.findSendList(purchase,UserUtils.getPrincipal() == null?userId:UserUtils.getPrincipal().getId());
        page.end();
        return new PageJson(page);
    }

    @Override
    public String getCode() {
        return "purchase";
    }

}