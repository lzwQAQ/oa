package com.kuyuner.core.sys.controller;

import com.kuyuner.common.controller.BaseController;
import com.kuyuner.common.controller.ListJson;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.common.lang.StringUtils;
import com.kuyuner.core.sys.entity.Dept;
import com.kuyuner.core.sys.service.DeptService;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 部门Service层接口
 *
 * @author administrator
 */
@Controller
@RequestMapping("${kuyuner.admin-path}/dept")
public class DeptController extends BaseController {

    @Autowired
    private DeptService deptService;

    /**
     * 显示列表页面
     *
     * @return
     */
    @RequestMapping("dept")
    @RequiresPermissions("sys:dept:view")
    public String showDeptList() {
        return "sys/deptList";
    }

    /**
     * 显示表单页面
     *
     * @param id
     * @param modelMap
     * @return
     */
    @RequestMapping({"form/{id}", "form"})
    @RequiresPermissions("sys:dept:edit")
    public String showDeptForm(@PathVariable(name = "id", required = false) String id, ModelMap modelMap) {
        if (StringUtils.isNotBlank(id)) {
            modelMap.addAttribute("dept", deptService.get(id));
        } else {
            modelMap.addAttribute("dept", new Dept());
        }
        return "sys/deptForm";
    }

    /**
     * 显示选择面板
     *
     * @return
     */
    @RequestMapping("panel")
    @RequiresPermissions("sys:dept:edit")
    public String showDeptPanel() {
        return "sys/deptPanel";
    }

    /**
     * 跳转创建子节点页面
     *
     * @param id
     * @param modelMap
     * @return
     */
    @RequestMapping("child/create/{id}")
    @RequiresPermissions("sys:dept:edit")
    public String createChildNode(@PathVariable("id") String id, ModelMap modelMap) {
        Dept parentDept = deptService.get(id);
        Dept dept = new Dept();
        dept.setParentId(parentDept.getId());
        dept.getParent().setName(parentDept.getName());
        modelMap.addAttribute("dept", dept);
        return "sys/deptForm";
    }

    /**
     * 查询分页集合JSON数据
     *
     * @param dept
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping("list")
    @RequiresPermissions("sys:dept:view")
    public PageJson list(Dept dept, String pageNum, String pageSize) {
        return deptService.findPageList(pageNum, pageSize, dept);
    }

    /**
     * 新增或修改数据
     *
     * @param dept
     * @return
     */
    @ResponseBody
    @RequestMapping("save")
    @RequiresPermissions("sys:dept:edit")
    public ResultJson save(Dept dept) {
        return deptService.saveOrUpdate(dept);
    }

    /**
     * 删除数据
     *
     * @param ids
     * @return
     */
    @ResponseBody
    @RequestMapping("deletes")
    @RequiresPermissions("sys:dept:edit")
    public ResultJson delete(String ids) {
        return deptService.deletes(ids.split(","));
    }

    @ResponseBody
    @RequestMapping("depttreelist")
    public ListJson findDeptTreeList() {
        return deptService.findDeptTreeList();
    }
}