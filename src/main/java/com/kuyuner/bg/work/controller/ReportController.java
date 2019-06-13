package com.kuyuner.bg.work.controller;

import com.kuyuner.common.controller.ListJson;
import com.kuyuner.common.lang.StringUtils;
import com.kuyuner.common.controller.BaseController;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.bg.work.entity.Report;
import com.kuyuner.bg.work.service.ReportService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 工作日报Controller层
 *
 * @author administrator
 */
@Controller
@RequestMapping("${kuyuner.admin-path}/report")
public class ReportController extends BaseController {

    @Autowired
    private ReportService reportService;

    /**
     * 显示列表页面
     *
     * @return
     */
    @RequestMapping("reportsend")
    public String showSendReportList() {
        return "work/reportSend";
    }

    /**
     * 显示列表页面
     *
     * @return
     */
    @RequestMapping("report")
    public String showReportList() {
        return "work/reportList";
    }

    /**
     * 显示表单页面
     *
     * @param id
     * @param modelMap
     * @return
     */
    @RequestMapping({"form/{id}", "form"})
    public String showReportForm(@PathVariable(name = "id", required = false) String id, ModelMap modelMap) {
        if (StringUtils.isNotBlank(id)) {
            modelMap.addAttribute("report", reportService.get(id));
        } else {
            modelMap.addAttribute("report", new Report());
        }
        modelMap.addAttribute("planList", reportService.findPlanList());
        return "work/reportForm";
    }

    /**
     * 显示表单页面
     *
     * @param id
     * @param modelMap
     * @return
     */
    @RequestMapping("detail/{id}")
    public String showReportDetail(@PathVariable(name = "id", required = false) String id, ModelMap modelMap) {
        modelMap.addAttribute("report", reportService.get(id));
        return "work/reportDetail";
    }

    /**
     * 查询分页集合JSON数据
     *
     * @param report
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping("list")
    public PageJson list(Report report, String pageNum, String pageSize) {
        return reportService.findPageList(pageNum, pageSize, report);
    }

    /**
     * 查询分页集合JSON数据
     *
     * @param report
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping("sendlist")
    public PageJson sendList(Report report, String pageNum, String pageSize) {
        return reportService.findSendPageList(pageNum, pageSize, report);
    }

    /**
     * 新增或修改数据
     *
     * @param report
     * @return
     */
    @ResponseBody
    @RequestMapping("save")
    public ResultJson save(Report report) {
        return reportService.saveOrUpdate(report);
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
        return reportService.deletes(ids.split(","));
    }

    /**
     * 计划信息
     *
     * @param planId
     * @return
     */
    @ResponseBody
    @RequestMapping("planchargepeoples")
    public ListJson planChargePeoples(String planId) {
        return reportService.findPlanChargePeoples(planId);
    }

}