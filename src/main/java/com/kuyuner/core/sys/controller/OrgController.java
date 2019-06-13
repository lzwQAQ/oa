package com.kuyuner.core.sys.controller;


import com.kuyuner.common.lang.StringUtils;
import com.kuyuner.common.controller.BaseController;
import com.kuyuner.common.controller.ListJson;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.core.common.dict.DictHolder;
import com.kuyuner.common.persistence.TreeEntity;
import com.kuyuner.core.sys.entity.Org;
import com.kuyuner.core.sys.service.OrgService;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Administrator
 */
@Controller
@RequestMapping("${kuyuner.admin-path}/org")
public class OrgController extends BaseController {

    @Autowired
    private OrgService orgService;

    /**
     * 显示列表页面
     *
     * @return
     */
    @RequestMapping("org")
    @RequiresPermissions("sys:org:view")
    public String showOrgList() {
        return "sys/orgList";
    }

    /**
     * 显示表单页面
     *
     * @param id
     * @param modelMap
     * @return
     */
    @RequestMapping({"form/{id}", "form"})
    @RequiresPermissions("sys:org:edit")
    public String showOrgForm(@PathVariable(name = "id", required = false) String id, ModelMap modelMap) {
        if (StringUtils.isNotBlank(id)) {
            modelMap.addAttribute("org", orgService.get(new Org(id)));
        } else {
            modelMap.addAttribute("org", new Org());
        }
        modelMap.addAttribute("sayHello", new DictHolder());
        return "sys/orgForm";
    }

    /**
     * 显示选择面板
     *
     * @return
     */
    @RequestMapping("panel")
    @RequiresPermissions("sys:org:edit")
    public String showOrgPanel() {
        return "sys/orgPanel";
    }

    /**
     * 跳转创建子节点页面
     *
     * @param id
     * @param modelMap
     * @return
     */
    @RequestMapping("child/create/{id}")
    @RequiresPermissions("sys:org:edit")
    public String createChildNode(@PathVariable("id") String id, ModelMap modelMap) {
        Org parentOrg = orgService.get(new Org(id));
        Org org = new Org();
        org.setParentId(parentOrg.getId());
        org.getParent().setName(parentOrg.getName());
        modelMap.addAttribute("org", org);
        return "sys/orgForm";
    }

    /**
     * 查询分页集合JSON数据
     *
     * @param org
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping("list")
    @RequiresPermissions("sys:org:view")
    public PageJson list(Org org, String pageNum, String pageSize) {
        return orgService.findPageList(pageNum, pageSize, org);
    }

    /**
     * 新增或修改数据
     *
     * @param org
     * @return
     */
    @ResponseBody
    @RequestMapping("save")
    @RequiresPermissions("sys:org:edit")
    public ResultJson save(Org org) {
        return orgService.saveOrUpdate(org);
    }

    /**
     * 删除数据
     *
     * @param ids
     * @return
     */
    @ResponseBody
    @RequestMapping("deletes")
    @RequiresPermissions("sys:org:edit")
    public ResultJson delete(String ids) {
        orgService.deletes(ids.split(","));
        return ResultJson.ok();
    }

    /**
     * 查询单个节点下的所有子节点
     *
     * @param org
     * @param nodeid 父节点ID
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "tree/list")
    @RequiresPermissions("sys:org:view")
    public ListJson listTree(Org org, String nodeid) {
        org.setParentId(nodeid);
        if (StringUtils.isBlank(org.getParentId())) {
            org.setTreeLevel(TreeEntity.ROOT_LEVEL);
        }
        return orgService.findList(org);
    }
}
