package com.kuyuner.bg.approval.controller;

import com.kuyuner.bg.approval.entity.OfficialReception;
import com.kuyuner.bg.approval.service.OfficialReceptionService;
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
 * 公务接待申请Controller层
 *
 * @author administrator
 */
@Controller
@RequestMapping("${kuyuner.admin-path}/reception")
public class OfficialReceptionController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private OfficialReceptionService officialReceptionService;

    /**
     * 显示待办
     *
     * @return
     */
    @RequestMapping("pending")
    public String showPendingList() {
        return "approval/officialReception/taskPendingList";
    }

    /**
     * 显示历史审批
     *
     * @return
     */
    @RequestMapping("historic")
    public String showHistoricList() {
        return "approval/officialReception/taskHistoricList";
    }

    /**
     * 显示历史审批
     *
     * @return
     */
    @RequestMapping("send")
    public String showSendHistoricList() {
        return "approval/officialReception/taskSendList";
    }

    /**
     * 显示表单页面
     *
     * @param businessId
     * @param modelMap
     * @return
     */
    @RequestMapping("form")
    public String showCarApplyForm(String businessId, String businessLogId, String type, ModelMap modelMap) {

        if ("historic".equals(type)) {
            modelMap.addAttribute("officialReception", officialReceptionService.getLog(businessLogId));
        } else {
            if (StringUtils.isNotBlank(businessId)) {
                OfficialReception officialReception = officialReceptionService.get(businessId);
                modelMap.addAttribute("officialReception", officialReception);
            } else {
                User user = userService.get(UserUtils.getPrincipal().getId());
                OfficialReception officialReception = new OfficialReception();
                officialReception.setSenderName(user.getName());
                officialReception.setSenderDeptName(user.getDept() != null ? user.getDept().getName() : "");

                modelMap.addAttribute("officialReception", officialReception);
            }
        }
        return "approval/officialReception/taskForm";
    }

    /**
     * 显示表单页面
     *
     * @return
     */
    @RequestMapping("record/{id}")
    public String showRecordList(ModelMap modelMap, @PathVariable("id") String id) {
        modelMap.addAttribute("officialReception", officialReceptionService.get(id));
        return "approval/officialReception/detailForm";
    }

    /**
     * 查询待办数据
     *
     * @param officialReception
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping("pending/list")
    public PageJson pendingList(OfficialReception officialReception, String pageNum, String pageSize) {
        return officialReceptionService.findPendingList(pageNum, pageSize, officialReception);
    }

    /**
     * 查询审批历史
     *
     * @param officialReception
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping("historic/list")
    public PageJson historicList(OfficialReception officialReception, String pageNum, String pageSize) {
        return officialReceptionService.findHistoricList(pageNum, pageSize, officialReception);
    }

    /**
     * 查询申请历史
     *
     * @param officialReception
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping("send/list")
    public PageJson sendList(OfficialReception officialReception, String pageNum, String pageSize) {
        return officialReceptionService.findSendList(pageNum, pageSize, officialReception);
    }

    /**
     * 提交申请
     *
     * @param officialReception
     * @return
     */
    @ResponseBody
    @RequestMapping("submit")
    public ResultJson submit(OfficialReception officialReception, String taskResult, String userId) {
        TaskBean taskBean = JsonMapper.fromJsonString(taskResult, TaskBean.class);
        taskBean.setSequenceFlowName(SequenceFlowNameUtil.getSequenceFlowName(userId, null, userService));
        return officialReceptionService.submitForm(officialReception, GfJsonUtil.toJSONString(taskBean), userId);
    }

    /**
     * 审批
     *
     * @param officialReception
     * @param approvalResult
     * @param taskResult
     * @return
     */
    @ResponseBody
    @RequestMapping("approval")
    public ResultJson approval(OfficialReception officialReception, String approvalResult, String taskResult, String userId) {
        approvalResult = StringUtils.isBlank(approvalResult) ? "无" : approvalResult;
        return officialReceptionService.approvalForm(officialReception, approvalResult, taskResult, userId);
    }


}