package com.kuyuner.bg.approval.controller;

import com.kuyuner.bg.approval.entity.Accounts;
import com.kuyuner.bg.approval.entity.Purchase;
import com.kuyuner.bg.approval.service.AccountsService;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.common.excel.ExcelExport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

/**
 * @author admin
 */
@Controller
@RequestMapping("${kuyuner.admin-path}/accountsrecord")
public class AccountsController {

    /**
     * excel文件名称
     */
    private static final String EXCEL_NAME = "收支记录统计表";

    @Autowired
    private AccountsService accountsService;

    /**
     * 显示采购记录页面
     *
     * @return
     */
    @RequestMapping("record")
    public String showRecordList() {
        return "approval/purchase/accountsRecordList";
    }

    /**
     * 显示表单页面
     *
     * @return
     */
    @RequestMapping("form")
    public String showForm(ModelMap modelMap) {
        modelMap.addAttribute("accounts", new Accounts());
        modelMap.addAttribute("users", accountsService.findUsers());
        return "approval/purchase/accountsRecordForm";
    }

    /**
     * 显示表单页面
     *
     * @return
     */
    @RequestMapping("record/{id}")
    public String showRecordList(ModelMap modelMap, @PathVariable("id") String id) {
        modelMap.addAttribute("accounts", accountsService.get(id));
        return "approval/purchase/accountsRecordForm";
    }

    @ResponseBody
    @RequestMapping("save")
    public ResultJson save(Accounts accounts) {
        return accountsService.save(accounts);
    }

    /**
     * 显示收支记录集合
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("record/list")
    public PageJson recordList(Accounts accounts, String pageNum, String pageSize) {
        return accountsService.findRecordList(pageNum, pageSize, accounts);
    }

    /**
     * 导出查询记录
     *
     * @param accounts
     */
    @RequestMapping("exportrecord")
    public void exportRecord(Accounts accounts, HttpServletResponse response) {
        List<Accounts> list = accountsService.findAllRecordList(accounts);
        new ExcelExport(EXCEL_NAME, Purchase.class).setDataList(list).write(response, EXCEL_NAME + ".xlsx").dispose();
    }
}
