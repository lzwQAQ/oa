package com.kuyuner.core.sys.controller;

import com.kuyuner.common.lang.StringUtils;
import com.kuyuner.common.controller.BaseController;
import com.kuyuner.common.controller.ListJson;
import com.kuyuner.common.persistence.TreeEntity;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.core.sys.entity.Menu;
import com.kuyuner.core.sys.service.MenuService;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 菜单Service层接口
 *
 * @author administrator
 */
@Controller
@RequestMapping("${kuyuner.admin-path}/menu")
public class MenuController extends BaseController {

    @Autowired
    private MenuService menuService;

    /**
     * 显示列表页面
     *
     * @return
     */
    @RequestMapping("menu")
    @RequiresPermissions("sys:menu:view")
    public String showMenuList() {
        return "sys/menuList";
    }

    /**
     * 显示表单页面
     *
     * @param id
     * @param modelMap
     * @return
     */
    @RequestMapping({"form/{id}", "form"})
    @RequiresPermissions("sys:menu:edit")
    public String showMenuForm(@PathVariable(name = "id", required = false) String id, ModelMap modelMap) {
        if (StringUtils.isNotBlank(id)) {
            modelMap.addAttribute("menu", menuService.get(id));
        } else {
            modelMap.addAttribute("menu", new Menu());
        }
        return "sys/menuForm";
    }

    /**
     * 跳转创建子节点页面
     *
     * @param id
     * @param modelMap
     * @return
     */
	@RequestMapping("child/create/{id}")
    @RequiresPermissions("sys:menu:edit")
    public String createChildNode(@PathVariable("id") String id, ModelMap modelMap) {
        Menu parentMenu = menuService.get(id);
        Menu menu = new Menu();
        menu.setParentId(parentMenu.getId());
        menu.getParent().setName(parentMenu.getName());
        modelMap.addAttribute("menu", menu);
        return "sys/menuForm";
    }

    /**
     * 查询分页集合JSON数据
     *
     * @param menu
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping("list")
    @RequiresPermissions("sys:menu:view")
    public PageJson list(Menu menu, String pageNum, String pageSize) {
        return menuService.findPageList(pageNum, pageSize, menu);
    }

    /**
     * 新增或修改数据
     *
     * @param menu
     * @return
     */
    @ResponseBody
    @RequestMapping("save")
    @RequiresPermissions("sys:menu:edit")
    public ResultJson save(Menu menu) {
        return menuService.saveOrUpdate(menu);
    }

    /**
     * 删除数据
     *
     * @param ids
     * @return
     */
    @ResponseBody
    @RequestMapping("deletes")
    @RequiresPermissions("sys:menu:edit")
    public ResultJson delete(String ids) {
        return menuService.deletes(ids.split(","));
    }

    /**
     * 查询单个节点下的所有子节点
     *
     * @param menu
     * @param nodeid 父节点ID
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "tree/list")
    @RequiresPermissions("sys:menu:view")
    public ListJson listTree(Menu menu, String nodeid) {
        menu.setParentId(nodeid);
        if (StringUtils.isBlank(menu.getParentId())) {
            menu.setTreeLevel(TreeEntity.ROOT_LEVEL);
        }
        return menuService.findList(menu);
    }

    @ResponseBody
    @RequestMapping(value = "treeall/list")
    @RequiresPermissions("sys:menu:edit")
    public ListJson findMenuAll() {
        return menuService.findMenuAll();
    }
}