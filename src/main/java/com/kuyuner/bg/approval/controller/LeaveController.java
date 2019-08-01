package com.kuyuner.bg.approval.controller;

import com.kuyuner.bg.approval.entity.Leave;
import com.kuyuner.bg.approval.service.LeaveService;
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
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 请假Service层接口
 *
 * @author administrator
 */
@Controller
@RequestMapping("${kuyuner.admin-path}/leave")
public class LeaveController extends BaseController {

    @Autowired
    private LeaveService leaveService;

    @Autowired
    private UserService userService;

    /**
     * 显示待办
     *
     * @return
     */
    @RequestMapping("pending")
    public String showPendingList() {
        return "approval/leave/taskPendingList";
    }

    /**
     * 显示历史审批
     *
     * @return
     */
    @RequestMapping("historic")
    public String showHistoricList() {
        return "approval/leave/taskHistoricList";
    }

    /**
     * 显示历史审批
     *
     * @return
     */
    @RequestMapping("send")
    public String showSendHistoricList() {
        return "approval/leave/taskSendList";
    }

    /**
     * 显示表单页面
     *
     * @param businessId
     * @param modelMap
     * @return
     */
    @RequestMapping("form")
    public String showLeaveForm(String businessId, String businessLogId, String type, ModelMap modelMap) {
        if ("historic".equals(type)) {
            modelMap.addAttribute("leave", leaveService.getLog(businessLogId));
        } else {
            if (StringUtils.isNotBlank(businessId)) {
                modelMap.addAttribute("leave", leaveService.get(businessId));
            } else {
                User user = userService.get(UserUtils.getPrincipal().getId());
                Leave leave = new Leave();
                leave.setSenderName(user.getName());
                leave.setSenderDeptName(user.getDept() != null ? user.getDept().getName() : "");
                modelMap.addAttribute("leave", leave);
            }
        }
        return "approval/leave/taskForm";
    }

    /**
     * 显示表单页面
     *
     * @return
     */
    @RequestMapping("record/{id}")
    public String showRecordList(ModelMap modelMap, @PathVariable("id") String id) {
        modelMap.addAttribute("leave", leaveService.get(id));
        return "approval/leave/detailForm";
    }

    /**
     * 查询待办数据
     *
     * @param leave
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "查询请假代办数据列表")
    @ResponseBody
    @RequestMapping("pending/list")
    public PageJson pendingList(@ApiParam(value = "请假类型type字段",required = false) Leave leave,
                                @ApiParam(value = "分页",required = true) String pageNum,
                                @ApiParam(value = "分页数目",required = true) String pageSize,String userId) {
        return leaveService.findPendingList(pageNum, pageSize, leave,userId);
    }

    /**
     * 查询审批历史
     *
     * @param leave
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "查询审批历史")
    @ResponseBody
    @RequestMapping("historic/list")
    public PageJson historicList( Leave leave,
                                 @ApiParam(value = "分页",required = true) String pageNum,
                                 @ApiParam(value = "分页数目",required = true) String pageSize,String userId) {
        return leaveService.findHistoricList(pageNum, pageSize, leave,userId);
    }

    /**
     * 查询申请历史
     *
     * @param leave
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "查询申请历史")
    @ResponseBody
    @RequestMapping("send/list")
    public PageJson sendList(Leave leave,
                             @ApiParam(value = "分页",required = true) String pageNum,
                             @ApiParam(value = "分页数目",required = true) String pageSize,String userId) {
        return leaveService.findSendList(pageNum, pageSize, leave,userId);
    }

    /**
     * 提交申请
     *
     * @param leave
     * @return
     */
    @ResponseBody
    @RequestMapping("submit")
    public ResultJson submit(Leave leave, String taskResult,String userId) {
        TaskBean taskBean = JsonMapper.fromJsonString(taskResult, TaskBean.class);
        taskBean.setSequenceFlowName(SequenceFlowNameUtil.getSequenceFlowName(userId,null,userService));
        return leaveService.submitForm(leave, GfJsonUtil.toJSONString(taskBean),userId);
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
        return leaveService.approvalForm(id, approvalResult, taskResult,userId);
    }



}