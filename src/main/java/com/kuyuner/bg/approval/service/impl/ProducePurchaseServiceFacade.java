package com.kuyuner.bg.approval.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.kuyuner.bg.approval.bean.PurchaseHistoricListView;
import com.kuyuner.bg.approval.bean.PurchasePendingListView;
import com.kuyuner.bg.approval.dao.AccountsDao;
import com.kuyuner.bg.approval.dao.PurchaseDao;
import com.kuyuner.bg.approval.dao.PurchaseGoodsDao;
import com.kuyuner.bg.approval.entity.Accounts;
import com.kuyuner.bg.approval.entity.Business;
import com.kuyuner.bg.approval.entity.Purchase;
import com.kuyuner.bg.approval.entity.PurchaseGoods;
import com.kuyuner.bg.approval.service.ProduceFaced;
import com.kuyuner.bg.approval.service.PurchaseService;
import com.kuyuner.bg.msg.util.HtmlRegexpUtil;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.common.idgen.IdGenerate;
import com.kuyuner.common.lang.StringUtils;
import com.kuyuner.common.mapper.JsonMapper;
import com.kuyuner.common.persistence.Page;
import com.kuyuner.common.reflect.ReflectUtils;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 采购申请Service层实现
 *
 * @author administrator
 */
@Component
public class ProducePurchaseServiceFacade implements ProduceFaced {
    private static Logger logger = LoggerFactory.getLogger(ReflectUtils.class);
    @Autowired
    private PurchaseDao purchaseDao;
    @Autowired
    private PurchaseService purchaseService;
    @Autowired
    private PurchaseGoodsDao purchaseGoodsDao;
    @Autowired
    private TaskService taskService;
    @Autowired
    private UserService userService;

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

    @Override
    public ResultJson submit(String body, String taskResult, String userId,String goods, String modelKey) {
        if(UserUtils.getPrincipal()==null && StringUtils.isBlank(userId)){
            return ResultJson.failed("请传入用户信息");
        }
        Purchase purchase = GfJsonUtil.parseObject(body,Purchase.class);
        //保存业务数据
        if (StringUtils.isBlank(purchase.getId())) {
            purchase.setId(IdGenerate.uuid());
            purchase.setSenderId(UserUtils.getPrincipal() == null ? userId : UserUtils.getPrincipal().getId());
            purchase.setCreater(UserUtils.getPrincipal() == null ? userId : UserUtils.getPrincipal().getId());
            purchaseDao.insert(purchase);
        } else {
            purchaseDao.update(purchase);
            purchase = purchaseDao.get(new Purchase(purchase.getId()));
        }
        String purchaseId = purchase.getId();
        List<PurchaseGoods> purchaseGoods = null;
        try {
            purchaseGoods = JsonMapper.getInstance().readValue(goods, new TypeReference<List<PurchaseGoods>>() {});
            purchaseGoods.forEach(good -> good.setPurchaseId(purchaseId));
            purchaseGoodsDao.deleteGoods(purchaseId);
            purchaseGoodsDao.inserts(purchaseGoods);
            purchaseService.handleForm(purchase, taskResult, purchaseGoods,userId);
        } catch (IOException e) {
            logger.error("采购发起流程失败...userId={},error={}",userId,e.getMessage());
            return ResultJson.failed("采购发起流程失败...");
        }
        return ResultJson.ok();
    }

    @Override
    public ResultJson approvalForm(String id, String approvalResult, String taskResult, String userId) {
        Purchase purchase = new Purchase();
        purchase.setId(id);
        String approvalResultContent = purchaseDao.getApprovalResult(purchase.getId());
        TaskBean taskBean = JsonMapper.fromJsonString(taskResult, TaskBean.class);
        String str = "<span class=\"people_name\">%s</span>的处理意见：</br>%s；<span class=\"audit_result\">%s</span></br>";
        User user = userService.get(userId);
        purchase.setApprovalResult((approvalResultContent == null ? "" : approvalResultContent) + String.format(str,user.getName(), approvalResult, taskBean.getSequenceFlowName()));
        purchaseDao.update(purchase);

        purchase = purchaseDao.get(new Purchase(purchase.getId()));
        PurchaseGoods goods = new PurchaseGoods();
        goods.setPurchaseId(id);
        List<PurchaseGoods> purchaseGoods = purchaseGoodsDao.findList(goods);
        purchaseService.handleForm(purchase, taskResult, purchaseGoods,userId);

        return ResultJson.ok();
    }

    @Override
    public ResultJson detail(String produceType,String businessId, String firstTask, String type,String taskId,
                             String modelKey, String startSequenceFlowName,String userId) {
        Map map = new HashMap();
        ResultJson result = taskService.getFormPath(modelKey,startSequenceFlowName,taskId);
        map.put("work",result.getData());
        if(StringUtils.isBlank(businessId) || "null".equals(businessId)){
            return ResultJson.ok(map);
        }
        if ("historic".equals(type)) {
            map.put("purchase", purchaseService.getLog(businessId));
            map.put("goodsList", purchaseService.findGoodsLog(businessId));
        } else {
            if (StringUtils.isNotBlank(businessId)) {
                Purchase purchase = purchaseService.get(businessId);
                purchase.setApprovalResult(HtmlRegexpUtil.filterHtml(purchase.getApprovalResult()));
                map.put("purchase", purchase);
                map.put("goodsList", purchaseService.findGoods(businessId));
            } else {
                User user = userService.get(UserUtils.getPrincipal()==null?userId:UserUtils.getPrincipal().getId());
                Purchase purchase = new Purchase();
                purchase.setSenderName(user.getName());
                purchase.setSenderDeptName(user.getDept() != null ? user.getDept().getName() : "");
                map.put("purchase", purchase);
            }
        }
        map.put("firstTask", StringUtils.isNotBlank(firstTask) ? firstTask : "false");
        map.put("taskType", type);
        return ResultJson.ok(map);

    }

}