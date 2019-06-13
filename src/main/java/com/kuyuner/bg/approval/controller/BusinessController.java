package com.kuyuner.bg.approval.controller;

import com.kuyuner.bg.approval.entity.Business;
import com.kuyuner.bg.approval.service.BusinessService;
import com.kuyuner.common.controller.BaseController;
import com.kuyuner.common.controller.ListJson;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.common.lang.StringUtils;
import com.kuyuner.core.sys.entity.User;
import com.kuyuner.core.sys.security.UserUtils;
import com.kuyuner.core.sys.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 业务申请Service层接口
 *
 * @author administrator
 */
@Controller
@RequestMapping("${kuyuner.admin-path}/business")
public class BusinessController extends BaseController {

    @Autowired
    private BusinessService businessService;

    @Autowired
    private UserService userService;

    /**
     * 显示待办
     *
     * @return
     */
    @RequestMapping("pending")
    public String showPendingList() {
        return "approval/business/taskPendingList";
    }

    /**
     * 显示历史审批
     *
     * @return
     */
    @RequestMapping("historic")
    public String showHistoricList() {
        return "approval/business/taskHistoricList";
    }

    /**
     * 显示历史审批
     *
     * @return
     */
    @RequestMapping("send")
    public String showSendHistoricList() {
        return "approval/business/taskSendList";
    }

    /**
     * 显示表单页面
     *
     * @param businessId
     * @param modelMap
     * @return
     */
    @RequestMapping("form")
    public String showBusinessForm(String businessId, String businessLogId, String type, ModelMap modelMap) {
        if ("historic".equals(type)) {
            modelMap.addAttribute("business", businessService.getLog(businessLogId));
        } else {
            if (StringUtils.isNotBlank(businessId)) {
                modelMap.addAttribute("business", businessService.get(businessId));
            } else {
                User user = userService.get(UserUtils.getPrincipal().getId());
                Business business = new Business();
                business.setSenderName(user.getName());
                business.setSenderDeptName(user.getDept() != null ? user.getDept().getName() : "");
                modelMap.addAttribute("business", business);
            }
        }
        return "approval/business/taskForm";
    }


    /**
     * 查询待办数据
     *
     * @param business
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping("pending/list")
    public PageJson pendingList(Business business, String pageNum, String pageSize) {
        return businessService.findPendingList(pageNum, pageSize, business);
    }

    /**
     * 查询审批历史
     *
     * @param business
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping("historic/list")
    public PageJson historicList(Business business, String pageNum, String pageSize) {
        return businessService.findHistoricList(pageNum, pageSize, business);
    }

    /**
     * 查询申请历史
     *
     * @param business
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping("send/list")
    public PageJson sendList(Business business, String pageNum, String pageSize) {
        return businessService.findSendList(pageNum, pageSize, business);
    }

    /**
     * 提交申请
     *
     * @param business
     * @return
     */
    @ResponseBody
    @RequestMapping("submit")
    public ResultJson submit(Business business, String taskResult) {
        return businessService.submitForm(business, taskResult);
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
    public ResultJson approval(String id, String approvalResult, String taskResult) {
        approvalResult = StringUtils.isBlank(approvalResult) ? "无" : approvalResult;
        return businessService.approvalForm(id, approvalResult, taskResult);
    }

    /**
     * 所有用户
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("userlist")
    public ListJson userList() {
        return new ListJson(businessService.findAllUsers());
    }

    /**
     * 所有部门
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("deptlist")
    public ListJson deptlist() {
        return new ListJson(businessService.findAllDepts());
    }

}