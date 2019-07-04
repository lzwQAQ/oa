package com.kuyuner.bg.approval.controller;

import com.kuyuner.bg.approval.entity.PersonnelAdjustment;
import com.kuyuner.bg.approval.service.PersonnelAdjustmentService;
import com.kuyuner.common.controller.BaseController;
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
 * 人事调度Service层接口
 *
 * @author administrator
 */
@Controller
@RequestMapping("${kuyuner.admin-path}/personneladjustment")
public class PersonnelAdjustmentController extends BaseController {

    @Autowired
    private PersonnelAdjustmentService personnelAdjustmentService;

    @Autowired
    private UserService userService;

    /**
     * 显示待办
     *
     * @return
     */
    @RequestMapping("pending")
    public String showPendingList() {
        return "approval/personneladjustment/taskPendingList";
    }

    /**
     * 显示历史审批
     *
     * @return
     */
    @RequestMapping("historic")
    public String showHistoricList() {
        return "approval/personneladjustment/taskHistoricList";
    }

    /**
     * 显示历史审批
     *
     * @return
     */
    @RequestMapping("send")
    public String showSendHistoricList() {
        return "approval/personneladjustment/taskSendList";
    }

    /**
     * 显示表单页面
     *
     * @param modelMap
     * @return
     */
    @RequestMapping("form")
    public String showPersonnelAdjustmentForm(String businessId, String businessLogId, String type, ModelMap modelMap) {
        if ("historic".equals(type)) {
            modelMap.addAttribute("adjustment", personnelAdjustmentService.getLog(businessLogId));
        } else {
            if (StringUtils.isNotBlank(businessId)) {
                modelMap.addAttribute("adjustment", personnelAdjustmentService.get(businessId));
            } else {
                User user = userService.get(UserUtils.getPrincipal().getId());
                PersonnelAdjustment personnelAdjustment = new PersonnelAdjustment();
                personnelAdjustment.setSenderName(user.getName());
                personnelAdjustment.setSenderDeptName(user.getDept() != null ? user.getDept().getName() : "");
                personnelAdjustment.setBirthday(user.getBirthday());
                personnelAdjustment.setHomePlace(user.getHomePlace());
                personnelAdjustment.setIsMarry(user.getIsMarry());
                personnelAdjustment.setEntryDate(user.getEntryDate());
                personnelAdjustment.setIsParty(user.getIsParty());
                personnelAdjustment.setOldPosition(user.getPosition());
                personnelAdjustment.setSchool(user.getSchool());
                personnelAdjustment.setSex(user.getSex());
                modelMap.addAttribute("adjustment", personnelAdjustment);
            }
        }
        return "approval/personneladjustment/taskForm";
    }


    /**
     * 查询待办数据
     *
     * @param personnelAdjustment
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping("pending/list")
    public PageJson pendingList(PersonnelAdjustment personnelAdjustment, String pageNum, String pageSize,String userId) {
        return personnelAdjustmentService.findPendingList(pageNum, pageSize, personnelAdjustment,userId);
    }

    /**
     * 查询审批历史
     *
     * @param personnelAdjustment
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping("historic/list")
    public PageJson historicList(PersonnelAdjustment personnelAdjustment, String pageNum, String pageSize,String userId) {
        return personnelAdjustmentService.findHistoricList(pageNum, pageSize, personnelAdjustment,userId);
    }

    /**
     * 查询申请历史
     *
     * @param personnelAdjustment
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping("send/list")
    public PageJson sendList(PersonnelAdjustment personnelAdjustment, String pageNum, String pageSize,String userId) {
        return personnelAdjustmentService.findSendList(pageNum, pageSize, personnelAdjustment,userId);
    }

    /**
     * 提交申请
     *
     * @param personnelAdjustment
     * @return
     */
    @ResponseBody
    @RequestMapping("submit")
    public ResultJson submit(PersonnelAdjustment personnelAdjustment, String taskResult) {
        return personnelAdjustmentService.submitForm(personnelAdjustment, taskResult);
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
        return personnelAdjustmentService.approvalForm(id, approvalResult, taskResult);
    }
}