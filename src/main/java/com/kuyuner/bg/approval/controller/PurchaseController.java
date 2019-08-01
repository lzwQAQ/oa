package com.kuyuner.bg.approval.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.kuyuner.bg.approval.entity.Purchase;
import com.kuyuner.bg.approval.entity.PurchaseGoods;
import com.kuyuner.bg.approval.service.PurchaseService;
import com.kuyuner.common.controller.BaseController;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.common.lang.StringUtils;
import com.kuyuner.common.mapper.JsonMapper;
import com.kuyuner.common.utils.GfJsonUtil;
import com.kuyuner.core.sys.entity.User;
import com.kuyuner.core.sys.security.UserUtils;
import com.kuyuner.core.sys.service.UserService;

import com.kuyuner.workflow.api.bean.TaskBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;

/**
 * 采购申请Service层接口
 *
 * @author administrator
 */
@Controller
@RequestMapping("${kuyuner.admin-path}/purchase")
public class PurchaseController extends BaseController {

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private UserService userService;

    /**
     * 显示待办
     *
     * @return
     */
    @RequestMapping("pending")
    public String showPendingList() {
        return "approval/purchase/taskPendingList";
    }

    /**
     * 显示历史审批
     *
     * @return
     */
    @RequestMapping("historic")
    public String showHistoricList() {
        return "approval/purchase/taskHistoricList";
    }

    /**
     * 显示历史审批
     *
     * @return
     */
    @RequestMapping("send")
    public String showSendHistoricList() {
        return "approval/purchase/taskSendList";
    }

    /**
     * 显示表单页面
     *
     * @param businessId
     * @param modelMap
     * @return
     */
    @RequestMapping("form")
    public String showPurchaseForm(String businessId, String businessLogId, String type, ModelMap modelMap, String firstTask) {
        if ("historic".equals(type)) {
            modelMap.addAttribute("purchase", purchaseService.getLog(businessLogId));
            modelMap.addAttribute("goodsList", purchaseService.findGoodsLog(businessLogId));
        } else {
            if (StringUtils.isNotBlank(businessId)) {
                modelMap.addAttribute("purchase", purchaseService.get(businessId));
                modelMap.addAttribute("goodsList", purchaseService.findGoods(businessId));
            } else {
                User user = userService.get(UserUtils.getPrincipal().getId());
                Purchase purchase = new Purchase();
                purchase.setSenderName(user.getName());
                purchase.setSenderDeptName(user.getDept() != null ? user.getDept().getName() : "");
                modelMap.addAttribute("purchase", purchase);
            }
        }
        modelMap.addAttribute("firstTask", StringUtils.isNotBlank(firstTask) ? firstTask : "false");
        modelMap.addAttribute("taskType", type);
        return "approval/purchase/taskForm";
    }

    /**
     * 查询待办数据
     *
     * @param purchase
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping("pending/list")
    public PageJson pendingList(Purchase purchase, String pageNum, String pageSize) {
        return purchaseService.findPendingList(pageNum, pageSize, purchase);
    }

    /**
     * 查询审批历史
     *
     * @param purchase
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping("historic/list")
    public PageJson historicList(Purchase purchase, String pageNum, String pageSize) {
        return purchaseService.findHistoricList(pageNum, pageSize, purchase);
    }

    /**
     * 查询申请历史
     *
     * @param purchase
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping("send/list")
    public PageJson sendList(Purchase purchase, String pageNum, String pageSize) {
        return purchaseService.findSendList(pageNum, pageSize, purchase);
    }

    /**
     * 提交申请
     *
     * @param purchase
     * @return
     */
    @ResponseBody
    @RequestMapping("submit")
    public ResultJson submit(Purchase purchase, String taskResult, String goods,String userId) throws IOException {
        List<PurchaseGoods> purchaseGoods = JsonMapper.getInstance().readValue(goods, new TypeReference<List<PurchaseGoods>>() {});
        TaskBean taskBean = JsonMapper.fromJsonString(taskResult, TaskBean.class);
        taskBean.setSequenceFlowName(SequenceFlowNameUtil.getSequenceFlowName(userId,null,userService));
        return purchaseService.submitForm(purchase, GfJsonUtil.toJSONString(taskBean), purchaseGoods,userId);
    }

    /**
     * 审批
     *
     * @param id
     * @param approvalResult
     * @param taskResult
     * @return
     */
    @ResponseBody
    @RequestMapping("approval")
    public ResultJson approval(String id, String approvalResult, String taskResult,String userId) {
        approvalResult = StringUtils.isBlank(approvalResult) ? "无" : approvalResult;
        return purchaseService.approvalForm(id, approvalResult, taskResult,userId);
    }

}