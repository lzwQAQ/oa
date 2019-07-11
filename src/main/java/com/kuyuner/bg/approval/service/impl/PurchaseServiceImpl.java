package com.kuyuner.bg.approval.service.impl;

import com.kuyuner.bg.approval.bean.PurchaseHistoricListView;
import com.kuyuner.bg.approval.bean.PurchasePendingListView;
import com.kuyuner.bg.approval.dao.AccountsDao;
import com.kuyuner.bg.approval.dao.PurchaseDao;
import com.kuyuner.bg.approval.dao.PurchaseGoodsDao;
import com.kuyuner.bg.approval.entity.Accounts;
import com.kuyuner.bg.approval.entity.Purchase;
import com.kuyuner.bg.approval.entity.PurchaseGoods;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 采购申请Service层实现
 *
 * @author administrator
 */
@Service
public class PurchaseServiceImpl implements PurchaseService {

    @Autowired
    private PurchaseDao purchaseDao;

    @Autowired
    private PurchaseGoodsDao purchaseGoodsDao;

    @Autowired
    private WorkFlowService workFlowService;

    @Autowired
    private AccountsDao accountsDao;

    @Override
    public Purchase get(String id) {
        return purchaseDao.get(new Purchase(id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultJson deletes(String... ids) {
        purchaseDao.deletes(ids);
        return ResultJson.ok();
    }

    @Override
    public Purchase getLog(String businessLogId) {
        Purchase purchase = new Purchase();
        purchase.setId(businessLogId);
        return purchaseDao.getLog(purchase);
    }

    @Override
    public PageJson findPendingList(String pageNum, String pageSize, Purchase purchase) {
        Page<PurchasePendingListView> page = new Page<>(pageNum, pageSize);
        page.start();
        purchaseDao.findPendingList(purchase, UserUtils.getPrincipal().getId());
        page.end();
        return new PageJson(page);
    }

    @Override
    public PageJson findHistoricList(String pageNum, String pageSize, Purchase purchase) {
        Page<PurchaseHistoricListView> page = new Page<>(pageNum, pageSize);
        page.start();
        purchaseDao.findHistoricList(purchase, UserUtils.getPrincipal().getId());
        page.end();
        return new PageJson(page);
    }

    @Override
    public PageJson findSendList(String pageNum, String pageSize, Purchase purchase) {
        Page<PurchaseHistoricListView> page = new Page<>(pageNum, pageSize);
        page.start();
        purchaseDao.findSendList(purchase, UserUtils.getPrincipal().getId());
        page.end();
        return new PageJson(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultJson submitForm(Purchase purchase, String taskResult, List<PurchaseGoods> purchaseGoods,String userId) {
        //保存业务数据
        if (StringUtils.isBlank(purchase.getId())) {
            purchase.setId(IdGenerate.uuid());
            purchase.setSenderId(UserUtils.getPrincipal().getId());
            purchase.setCreater(UserUtils.getPrincipal().getId());
            purchaseDao.insert(purchase);
        } else {
            purchaseDao.update(purchase);
            purchase = purchaseDao.get(new Purchase(purchase.getId()));
        }
        String purchaseId = purchase.getId();
        purchaseGoods.forEach(goods -> goods.setPurchaseId(purchaseId));
        purchaseGoodsDao.deleteGoods(purchaseId);
        purchaseGoodsDao.inserts(purchaseGoods);

        handleForm(purchase, taskResult, purchaseGoods,userId);

        return ResultJson.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultJson approvalForm(String id, String approvalResult, String taskResult,String userId) {
        Purchase purchase = new Purchase();
        purchase.setId(id);
        String approvalResultContent = purchaseDao.getApprovalResult(purchase.getId());
        TaskBean taskBean = JsonMapper.fromJsonString(taskResult, TaskBean.class);
        String str = "<span class=\"people_name\">%s</span>的处理意见：</br>%s；<span class=\"audit_result\">%s</span></br>";
        purchase.setApprovalResult((approvalResultContent == null ? "" : approvalResultContent) + String.format(str, UserUtils.getPrincipal().getName(), approvalResult, taskBean.getSequenceFlowName()));
        purchaseDao.update(purchase);

        purchase = purchaseDao.get(new Purchase(purchase.getId()));
        PurchaseGoods goods = new PurchaseGoods();
        goods.setPurchaseId(id);
        List<PurchaseGoods> purchaseGoods = purchaseGoodsDao.findList(goods);
        handleForm(purchase, taskResult, purchaseGoods,userId);

        return ResultJson.ok();
    }

    @Override
    public List<PurchaseGoods> findGoodsLog(String businessLogId) {
        return purchaseGoodsDao.findGoodsLog(businessLogId);
    }

    @Override
    public List<PurchaseGoods> findGoods(String businessId) {
        PurchaseGoods goods = new PurchaseGoods();
        goods.setPurchaseId(businessId);
        return purchaseGoodsDao.findList(goods);
    }

    /**
     * 提交任务
     *
     * @param purchase
     * @param taskResult
     * @param purchaseGoods
     */
    @Override
    public void handleForm(Purchase purchase, String taskResult, List<PurchaseGoods> purchaseGoods, String userId) {
        //记录日志
        Purchase purchaseLog = new Purchase();
        BeanUtils.copyProperties(purchase, purchaseLog);
        purchaseLog.setId(IdGenerate.uuid());
        purchaseDao.insertLog(purchaseLog);
        purchaseGoods.forEach(goods -> goods.setPurchaseId(purchaseLog.getId()));
        purchaseGoodsDao.insertLogs(purchaseGoods);

        //提交流程
        BusinessKey businessKey = new BusinessKey();
        businessKey.setId(purchase.getId());
        businessKey.setLogId(purchaseLog.getId());
        TaskInfo taskInfo = workFlowService.submitTask(taskResult, businessKey,userId);

        //更新业务表的当前环节字段
        Purchase purchase2 = new Purchase();
        purchase2.setId(purchase.getId());
        purchase2.setTaskName(taskInfo.getTaskName());
        purchaseDao.update(purchase2);

        if ("结束".equals(taskInfo.getTaskName())) {
            Purchase purchase1 = purchaseDao.get(new Purchase(purchase.getId()));
            Accounts accounts = new Accounts();
            accounts.setId(IdGenerate.uuid());
            accounts.setName(purchase1.getName());
            accounts.setSenderId(purchase1.getSenderId());
            accounts.setTotalPrice(purchase1.getTotalPrice());
            accounts.setCreateDate(purchase1.getCreateDate());
            accountsDao.insert(accounts);
        }
    }

}