package com.kuyuner.bg.approval.controller;

import com.kuyuner.bg.approval.entity.LeaveRecord;
import com.kuyuner.bg.approval.service.LeaveRecordService;
import com.kuyuner.common.controller.BaseController;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.common.excel.ExcelExport;
import com.kuyuner.core.common.dict.DictHolder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

/**
 * 请假Service层接口
 *
 * @author administrator
 */
@Controller
@RequestMapping("${kuyuner.admin-path}/leaverecord")
public class LeaveRecordController extends BaseController {

    @Autowired
    private LeaveRecordService leaveService;

    @Autowired
    private DictHolder dictHolder;

    /**
     * excel文件名称
     */
    private static final String EXCEL_NAME = "员工请假记录统计表";

    /**
     * 显示请假记录列表页面
     *
     * @return
     */
    @RequestMapping("record")
    public String showRecordList() {
        return "approval/leave/leaveRecordList";
    }

    /**
     * 显示表单页面
     *
     * @return
     */
    @RequestMapping("form")
    public String showForm(ModelMap modelMap) {
        modelMap.addAttribute("leaveRecord", new LeaveRecord());
        modelMap.addAttribute("users", leaveService.findUsers());
        return "approval/leave/leaveRecordForm";
    }

    /**
     * 显示表单页面
     *
     * @return
     */
    @RequestMapping("record/{id}")
    public String showRecordList(ModelMap modelMap, @PathVariable("id") String id) {
        modelMap.addAttribute("leaveRecord", leaveService.get(id));
        return "approval/leave/leaveRecordForm";
    }

    /**
     * 添加请假记录
     *
     * @param leaveRecord
     * @return
     */
    @ResponseBody
    @RequestMapping("save")
    public ResultJson save(LeaveRecord leaveRecord) {
        return leaveService.save(leaveRecord);
    }

    /**
     * 显示请假记录集合
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("record/list")
    public PageJson recordList(LeaveRecord leaveRecord, String pageNum, String pageSize) {
        return leaveService.findRecordList(pageNum, pageSize, leaveRecord);
    }

    /**
     * 导出查询记录
     *
     * @param leaveRecord
     */
    @RequestMapping("exportrecord")
    public void exportRecord(LeaveRecord leaveRecord, HttpServletResponse response) {
        List<LeaveRecord> list = leaveService.findAllRecordList(leaveRecord);
        list.forEach(l -> {
            l.setType(dictHolder.getValue(l.getType(), "LEAVE_TYPE"));
        });
        new ExcelExport(EXCEL_NAME, LeaveRecord.class).setDataList(list).write(response, EXCEL_NAME + ".xlsx").dispose();
    }

}