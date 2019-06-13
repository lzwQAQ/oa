package com.kuyuner.core.sys.controller;

import com.kuyuner.common.controller.BaseController;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.common.lang.StringUtils;
import com.kuyuner.core.sys.entity.Role;
import com.kuyuner.core.sys.service.RoleService;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 角色Service层接口
 *
 * @author administrator
 */
@Controller
@RequestMapping("${kuyuner.admin-path}/role")
public class RoleController extends BaseController {

    @Autowired
    private RoleService roleService;

    /**
     * 显示列表页面
     *
     * @return
     */
    @RequestMapping("role")
    @RequiresPermissions("sys:role:view")
    public String showRoleList() {
        return "sys/roleList";
    }

    /**
     * 显示表单页面
     *
     * @param id
     * @param modelMap
     * @return
     */
    @RequestMapping({"form/{id}", "form"})
    @RequiresPermissions("sys:role:edit")
    public String showRoleForm(@PathVariable(name = "id", required = false) String id, ModelMap modelMap) {
        if (StringUtils.isNotBlank(id)) {
            modelMap.addAttribute("role", roleService.get(id));
        } else {
            modelMap.addAttribute("role", new Role());
        }
        return "sys/roleForm";
    }


    /**
     * 查询分页集合JSON数据
     *
     * @param role
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping("list")
    @RequiresPermissions("sys:role:view")
    public PageJson list(Role role, String pageNum, String pageSize) {
        return roleService.findPageList(pageNum, pageSize, role);
    }

    /**
     * 新增或修改数据
     *
     * @param role
     * @return
     */
    @ResponseBody
    @RequestMapping("save")
    @RequiresPermissions("sys:role:edit")
    public ResultJson save(Role role) {
        return roleService.saveOrUpdate(role);
    }

    /**
     * 删除数据
     *
     * @param ids
     * @return
     */
    @ResponseBody
    @RequestMapping("deletes")
    @RequiresPermissions("sys:role:edit")
    public ResultJson delete(String ids) {
        return roleService.deletes(ids.split(","));
    }

    /**
     * 权限设置页面
     *
     * @param id
     * @param modelMap
     * @return
     */
    @RequestMapping("permission/form/{id}")
    @RequiresPermissions("sys:role:edit")
    public String permissionForm(@PathVariable("id") String id, ModelMap modelMap) {
        modelMap.addAttribute("role", roleService.get(id));
        return "sys/rolePermissionsForm";
    }

    /**
     * 查询所有的菜单和权限
     *
     * @param roleId
     * @return
     */
    @ResponseBody
    @RequestMapping("allpermission/{roleId}")
    @RequiresPermissions("sys:role:edit")
    public ResultJson findAllPermissions(@PathVariable("roleId") String roleId) {
        return roleService.findAllPermissions(roleId);
    }

    /**
     * 保存角色的权限信息
     *
     * @param roleId
     * @param menus
     * @return
     */
    @ResponseBody
    @RequestMapping("permissions/save")
    @RequiresPermissions("sys:role:edit")
    public ResultJson savePermissions(String roleId, String menus) {
        return roleService.savePermissions(roleId, menus.split(","));
    }
}