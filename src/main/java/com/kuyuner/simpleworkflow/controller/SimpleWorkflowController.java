package com.kuyuner.simpleworkflow.controller;

import com.kuyuner.common.controller.BaseController;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.common.lang.StringUtils;
import com.kuyuner.core.sys.entity.User;
import com.kuyuner.core.sys.security.UserUtils;
import com.kuyuner.core.sys.service.UserService;
import com.kuyuner.simpleworkflow.entity.SimpleWorkflow;
import com.kuyuner.simpleworkflow.service.SimpleWorkflowService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 简单流程Service层接口
 *
 * @author administrator
 */
@Controller
@RequestMapping("${kuyuner.admin-path}/simpleworkflow")
public class SimpleWorkflowController extends BaseController {

    @Autowired
    private SimpleWorkflowService simpleWorkflowService;

    @Autowired
    private UserService userService;

    /**
     * 显示待办
     *
     * @return
     */
    @RequestMapping("pending")
    public String showPendingList() {
        return "simpleworkflow/taskPendingList";
    }

    /**
     * 显示历史审批
     *
     * @return
     */
    @RequestMapping("historic")
    public String showHistoricList() {
        return "simpleworkflow/taskHistoricList";
    }

    /**
     * 显示历史审批
     *
     * @return
     */
    @RequestMapping("send")
    public String showSendHistoricList() {
        return "simpleworkflow/taskSendList";
    }

    /**
     * 显示表单页面
     *
     * @param businessId
     * @param modelMap
     * @return
     */
    @RequestMapping("form")
    public String showSimpleWorkflowForm(String businessId, String businessLogId, String type, ModelMap modelMap) {
        if ("historic".equals(type)) {
            modelMap.addAttribute("workflow", simpleWorkflowService.getLog(businessLogId));
        } else {
            if (StringUtils.isNotBlank(businessId)) {
                modelMap.addAttribute("workflow", simpleWorkflowService.get(businessId));
            } else {
                User user = userService.get(UserUtils.getPrincipal().getId());
                SimpleWorkflow workflow = new SimpleWorkflow();
                workflow.setSenderName(user.getUsername());
                workflow.setSenderDeptName(user.getDept() != null ? user.getDept().getName() : "");
                modelMap.addAttribute("workflow", workflow);
            }
        }
        return "simpleworkflow/taskForm";
    }


    /**
     * 查询待办数据
     *
     * @param simpleWorkflow
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping("pending/list")
    public PageJson pendingList(SimpleWorkflow simpleWorkflow, String pageNum, String pageSize) {
        return simpleWorkflowService.findPendingList(pageNum, pageSize, simpleWorkflow);
    }

    /**
     * 查询审批历史
     *
     * @param simpleWorkflow
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping("historic/list")
    public PageJson historicList(SimpleWorkflow simpleWorkflow, String pageNum, String pageSize) {
        return simpleWorkflowService.findHistoricList(pageNum, pageSize, simpleWorkflow);
    }

    /**
     * 查询申请历史
     *
     * @param simpleWorkflow
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping("send/list")
    public PageJson sendList(SimpleWorkflow simpleWorkflow, String pageNum, String pageSize) {
        return simpleWorkflowService.findSendList(pageNum, pageSize, simpleWorkflow);
    }

    /**
     * 提交时申请
     *
     * @param simpleWorkflow
     * @return
     */
    @ResponseBody
    @RequestMapping("sbumit")
    public ResultJson sbumit(SimpleWorkflow simpleWorkflow, String taskResult, String modelKey,String userId) {
        return simpleWorkflowService.submitForm(simpleWorkflow, taskResult, modelKey,userId);
    }

    /**
     * 审批
     *
     * @param id
     * @param approvalResult
     * @param taskResult
     * @param modelKey
     * @return
     */
    @ResponseBody
    @RequestMapping("approval")
    public ResultJson approval(String id, String approvalResult, String taskResult, String modelKey,String userId) {
        approvalResult = StringUtils.isBlank(approvalResult) ? "无" : approvalResult;
        return simpleWorkflowService.approvalForm(id, approvalResult, taskResult, modelKey,userId);
    }

    /**
     * 删除数据
     *
     * @param ids
     * @return
     */
    @ResponseBody
    @RequestMapping("deletes")
    public ResultJson delete(String ids) {
        return simpleWorkflowService.deletes(ids.split(","));
    }

}