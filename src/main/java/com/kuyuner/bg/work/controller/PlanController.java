package com.kuyuner.bg.work.controller;

import com.kuyuner.common.controller.ListJson;
import com.kuyuner.common.lang.StringUtils;
import com.kuyuner.common.controller.BaseController;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.bg.work.entity.Plan;
import com.kuyuner.bg.work.service.PlanService;
import com.kuyuner.core.sys.entity.User;
import com.kuyuner.core.sys.security.UserUtils;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 任务计划Controller层
 *
 * @author administrator
 */
@Controller
@RequestMapping("${kuyuner.admin-path}/plan")
public class PlanController extends BaseController {

    @Autowired
    private PlanService planService;

    /**
     * 显示列表页面
     *
     * @return
     */
    @RequestMapping("plansend")
    public String showPlanList() {
        return "work/planSend";
    }

    /**
     * 显示列表页面
     *
     * @return
     */
    @RequestMapping("plan")
    public String showPersonalPlanList() {
        return "work/planList";
    }

    /**
     * 显示表单页面
     *
     * @param id
     * @param modelMap
     * @return
     */
    @RequestMapping({"form/{id}", "form"})
    public String showPlanForm(@PathVariable(name = "id", required = false) String id, ModelMap modelMap) {
        if (StringUtils.isNotBlank(id)) {
            modelMap.addAttribute("plan", planService.get(id));
        } else {
            Plan plan = new Plan();
            plan.setProcess(-1);
            plan.setSender(UserUtils.getUser().getName());
            modelMap.addAttribute("plan", plan);
        }
        modelMap.addAttribute("userList", planService.findAllUsers());
        return "work/planForm";
    }

    /**
     * 显示详情页面
     *
     * @param id
     * @param modelMap
     * @return
     */
    @RequestMapping("detail/{id}")
    public String showPlanDetailForm(@PathVariable(name = "id", required = false) String id, ModelMap modelMap) {
        modelMap.addAttribute("plan", planService.get(id));
        modelMap.addAttribute("joinPeoples", planService.findPeopleList(id));
        return "work/planDetail";
    }


    /**
     * 查询分页集合JSON数据
     *
     * @param plan
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping("list")
    public PageJson list(Plan plan, String pageNum, String pageSize,String userId) {
        if(StringUtils.isBlank(pageNum) || StringUtils.isBlank(pageSize)){
            pageNum = "1";
            pageSize = "10000";
        }
        return planService.findPageList(pageNum, pageSize, plan,userId);
    }

    /**
     * 查询分页集合JSON数据
     *
     * @param plan
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping("sendlist")
    public PageJson sendList(Plan plan, String pageNum, String pageSize,String userId) {
        if(StringUtils.isBlank(pageNum) || StringUtils.isBlank(pageSize)){
            pageNum = "1";
            pageSize = "10000";
        }
        return planService.findSendPageList(pageNum, pageSize, plan,userId);
    }

    /**
     * 新增或修改数据
     *
     * @param plan
     * @return
     */
    @ResponseBody
    @RequestMapping("save")
    public ResultJson save(Plan plan, String joinPeople, String chargePeoples,String userId) {
        return planService.saveOrUpdate(plan, joinPeople, chargePeoples,userId);
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
        return planService.deletes(ids.split(","));
    }

    /**
     * 查询已经选择的责任人
     *
     * @param planId
     * @return
     */
    @ResponseBody
    @RequestMapping("chargepeoples")
    public ListJson chargePeoples(String planId) {
        return planService.findChargePeoples(planId);
    }

    /**
     * 查询已经选择的参与人
     *
     * @param planId
     * @return
     */
    @ResponseBody
    @RequestMapping("joinpeoples")
    public ListJson joinPeoples(String planId) {
        return planService.findJoinPeoples(planId);
    }

    /**
     * 详情
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("sendDetail")
    public ResultJson findPeopleList(String id,String userId) {
        Map map = new HashMap();
        if (StringUtils.isNotBlank(id)) {
            map.put("plan", planService.get(id));
            map.put("joinPeoples", planService.findPeopleList(id));
        } else {
            Plan plan = new Plan();
            plan.setProcess(-1);
            plan.setSender(UserUtils.getUserFromDB(userId).getName());
            map.put("plan", plan);
        }
        return ResultJson.ok(map);
    }

    @ResponseBody
    @RequestMapping("findUsers")
    public ResultJson findPeopleList() {
        List<User> users = planService.findAllUsers();
        return ResultJson.ok(users);
    }

}