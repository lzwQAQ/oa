package com.kuyuner.bg.approval.controller;

import com.kuyuner.bg.approval.entity.Finance;
import com.kuyuner.bg.approval.service.FinanceService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 财务申请Controller层
 *
 * @author administrator
 */
@Controller
@RequestMapping("${kuyuner.admin-path}/finance")
public class FinanceController extends BaseController {

    @Autowired
    private FinanceService financeService;

    @Autowired
    private UserService userService;

    /**
     * 显示待办
     *
     * @return
     */
    @RequestMapping("pending")
    public String showPendingList() {
        return "approval/finance/taskPendingList";
    }

    /**
     * 显示历史审批
     *
     * @return
     */
    @RequestMapping("historic")
    public String showHistoricList() {
        return "approval/finance/taskHistoricList";
    }

    /**
     * 显示历史审批
     *
     * @return
     */
    @RequestMapping("send")
    public String showSendHistoricList() {
        return "approval/finance/taskSendList";
    }

    /**
     * 显示表单页面
     *
     * @param businessId
     * @param modelMap
     * @return
     */
    @RequestMapping("form")
    public String showFinanceForm(String businessId, String businessLogId, String type, ModelMap modelMap) {
        if ("historic".equals(type)) {
            modelMap.addAttribute("finance", financeService.getLog(businessLogId));
        } else {
            if (StringUtils.isNotBlank(businessId)) {
                modelMap.addAttribute("finance", financeService.get(businessId));
            } else {
                User user = userService.get(UserUtils.getPrincipal().getId());
                Finance finance = new Finance();
                finance.setSenderName(user.getName());
                finance.setSenderDeptName(user.getDept() != null ? user.getDept().getName() : "");
                modelMap.addAttribute("finance", finance);
            }
        }
        return "approval/finance/taskForm";
    }

    /**
     * 显示表单页面
     *
     * @return
     */
    @RequestMapping("record/{id}")
    public String showRecordList(ModelMap modelMap, @PathVariable("id") String id) {
        modelMap.addAttribute("finance", financeService.get(id));
        return "approval/finance/detailForm";
    }

    /**
     * 查询待办数据
     *
     * @param finance
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping("pending/list")
    public PageJson pendingList(Finance finance, String pageNum, String pageSize) {
        return financeService.findPendingList(pageNum, pageSize, finance);
    }

    /**
     * 查询审批历史
     *
     * @param finance
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping("historic/list")
    public PageJson historicList(Finance finance, String pageNum, String pageSize) {
        return financeService.findHistoricList(pageNum, pageSize, finance);
    }

    /**
     * 查询申请历史
     *
     * @param finance
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping("send/list")
    public PageJson sendList(Finance finance, String pageNum, String pageSize) {
        return financeService.findSendList(pageNum, pageSize, finance);
    }

    /**
     * 提交申请
     *
     * @param finance
     * @return
     */
    @ResponseBody
    @RequestMapping("submit")
    public ResultJson submit(Finance finance, String taskResult,String userId) {
        TaskBean taskBean = JsonMapper.fromJsonString(taskResult, TaskBean.class);
        taskBean.setSequenceFlowName(SequenceFlowNameUtil.getSequenceFlowName(userId,null,userService));
        return financeService.submitForm(finance, GfJsonUtil.toJSONString(taskBean),userId);
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
        return financeService.approvalForm(id, approvalResult, taskResult,userId);
    }

}