package com.kuyuner.bg.approval.controller;

import com.kuyuner.bg.approval.entity.CarApply;
import com.kuyuner.bg.approval.service.CarApplyService;
import com.kuyuner.common.controller.BaseController;
import com.kuyuner.common.controller.ListJson;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.common.lang.StringUtils;
import com.kuyuner.common.mapper.JsonMapper;
import com.kuyuner.common.utils.GfJsonUtil;
import com.kuyuner.core.sys.entity.Menu;
import com.kuyuner.core.sys.entity.User;
import com.kuyuner.core.sys.security.UserUtils;
import com.kuyuner.core.sys.service.UserService;

import com.kuyuner.shiro.UsernamePasswordToken;
import com.kuyuner.workflow.api.bean.TaskBean;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 车辆申请Controller层
 *
 * @author administrator
 */
@Controller
@RequestMapping("${kuyuner.admin-path}/carapply")
public class CarApplyController extends BaseController {

    @Autowired
    private CarApplyService carApplyService;

    @Autowired
    private UserService userService;

    private String officeManager = "办公室主任";

    /**
     * 显示待办
     *
     * @return
     */
    @RequestMapping("pending")
    public String showPendingList() {
        return "approval/carApply/taskPendingList";
    }

    /**
     * 显示历史审批
     *
     * @return
     */
    @RequestMapping("historic")
    public String showHistoricList() {
        return "approval/carApply/taskHistoricList";
    }

    /**
     * 显示历史审批
     *
     * @return
     */
    @RequestMapping("send")
    public String showSendHistoricList() {
        return "approval/carApply/taskSendList";
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
        String str = null;
        int tag = 0;
        if (StringUtils.isNotBlank(UserUtils.getPrincipal().getId())) {
            List<String> list = userService.getRoleName(UserUtils.getPrincipal().getId());
            if (CollectionUtils.isNotEmpty(list) && list.contains(officeManager)) {
                str = officeManager;
            }
        }
        if (officeManager.equals(str)) {
            tag = 1;
        } else if ("historic".equals(type) || "pending".equals(type)) {
            tag = 2;
        }
        if ("historic".equals(type)) {
            modelMap.addAttribute("carApply", carApplyService.getLog(businessLogId));
        } else {
            if (StringUtils.isNotBlank(businessId)) {
                CarApply carApply = carApplyService.get(businessId);
                if (carApply.getSenderId().equals(UserUtils.getPrincipal().getId()) && !officeManager.equals(str)) {
                    tag = 3;
                }
                modelMap.addAttribute("carApply", carApply);
            } else {
                User user = userService.get(UserUtils.getPrincipal().getId());
                CarApply carApply = new CarApply();
                carApply.setSenderName(user.getName());
                carApply.setSenderDeptName(user.getDept() != null ? user.getDept().getName() : "");

                modelMap.addAttribute("carApply", carApply);
            }
        }
        modelMap.addAttribute("tag", tag);
        return "approval/carApply/taskForm";
    }

    /**
     * 显示表单页面
     *
     * @return
     */
    @RequestMapping("record/{id}")
    public String showRecordList(ModelMap modelMap, @PathVariable("id") String id) {
        modelMap.addAttribute("carApply", carApplyService.get(id));
        return "approval/carApply/detailForm";
    }

    /**
     * 查询待办数据
     *
     * @param carApply
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping("pending/list")
    public PageJson pendingList(CarApply carApply, String pageNum, String pageSize) {
        return carApplyService.findPendingList(pageNum, pageSize, carApply);
    }

    /**
     * 查询审批历史
     *
     * @param carApply
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping("historic/list")
    public PageJson historicList(CarApply carApply, String pageNum, String pageSize) {
        return carApplyService.findHistoricList(pageNum, pageSize, carApply);
    }

    /**
     * 查询申请历史
     *
     * @param carApply
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping("send/list")
    public PageJson sendList(CarApply carApply, String pageNum, String pageSize) {
        return carApplyService.findSendList(pageNum, pageSize, carApply);
    }

    /**
     * 提交申请
     *
     * @param carApply
     * @return
     */
    @ResponseBody
    @RequestMapping("submit")
    public ResultJson submit(CarApply carApply, String taskResult, String userId) {
        TaskBean taskBean = JsonMapper.fromJsonString(taskResult, TaskBean.class);
        taskBean.setSequenceFlowName(SequenceFlowNameUtil.getSequenceFlowName(userId, null, userService));
        return carApplyService.submitForm(carApply, GfJsonUtil.toJSONString(taskBean), userId);
    }

    /**
     * 审批
     *
     * @param carApply
     * @param approvalResult
     * @param taskResult
     * @return
     */
    @ResponseBody
    @RequestMapping("approval")
    public ResultJson approval(CarApply carApply, String approvalResult, String taskResult, String userId) {
        approvalResult = StringUtils.isBlank(approvalResult) ? "无" : approvalResult;
        return carApplyService.approvalForm(carApply, approvalResult, taskResult, userId);
    }

    /**
     * 查询驾驶员
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("finddrivers")
    public ListJson findDrivers(String userId) {
        return carApplyService.findDrivers(userId);
    }

    /**
     * 查询车辆
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("findcars")
    public ListJson findCars() {
        return carApplyService.findCars();
    }

    /**
     * APP客户端加载用户权限标识
     *
     * @param userId
     * @param businessId
     * @param type
     * @return
     */
    @RequestMapping("checkTag")
    @ResponseBody
    public ResultJson checkTag(String userId, String businessId, String type) {
        String str = null;
        int tag = 0;
        if (StringUtils.isNotBlank(userId)) {
            List<String> list = userService.getRoleName(userId);
            if (CollectionUtils.isNotEmpty(list) && list.contains(officeManager)) {
                str = officeManager;
            }
        }
        if (officeManager.equals(str)) {
            tag = 1;
        } else if ("1".equals(type) || "2".equals(type)) {
            tag = 2;
        }
        if (StringUtils.isNotBlank(businessId)) {
            CarApply carApply = carApplyService.get(businessId);
            if (carApply.getSenderId().equals(userId) && !officeManager.equals(str)) {
                tag = 3;
            }
        }
        return ResultJson.ok(tag);
    }
}