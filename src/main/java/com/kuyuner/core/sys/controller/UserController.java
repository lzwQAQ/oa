package com.kuyuner.core.sys.controller;

import com.kuyuner.common.controller.BaseController;
import com.kuyuner.common.controller.ListJson;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.common.lang.StringUtils;
import com.kuyuner.common.security.PasswordUtils;
import com.kuyuner.core.sys.entity.User;
import com.kuyuner.core.sys.security.UserUtils;
import com.kuyuner.core.sys.service.UserService;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 用户Service层接口
 *
 * @author administrator
 */
@Controller
@RequestMapping("${kuyuner.admin-path}/user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    /**
     * 显示列表页面
     *
     * @return
     */
    @RequestMapping("user")
    @RequiresPermissions("sys:user:view")
    public String showUserList() {
        return "sys/userList";
    }

    /**
     * 显示用户角色设置界面
     *
     * @return
     */
    @RequestMapping("roles/form")
    @RequiresPermissions("sys:user:edit")
    public String showUserRoleForm() {
        return "sys/userRoleForm";
    }

    /**
     * 显示表单页面
     *
     * @param id
     * @param modelMap
     * @return
     */
    @RequestMapping({"form/{id}", "form"})
    @RequiresPermissions("sys:user:edit")
    public String showUserForm(@PathVariable(name = "id", required = false) String id, ModelMap modelMap) {
        if (StringUtils.isNotBlank(id)) {
            modelMap.addAttribute("user", userService.get(id));
        } else {
            modelMap.addAttribute("user", new User());
        }
        return "sys/userForm";
    }

    /**
     * 个人信息页面
     *
     * @param modelMap
     * @return
     */
    @RequestMapping("personalinfo")
    public String showPersonalInfoForm(ModelMap modelMap) {
        modelMap.addAttribute("user", userService.get(UserUtils.getPrincipal().getId()));
        return "sys/personalinfoForm";
    }

    /**
     * 保存个人信息
     *
     * @param user
     * @return
     */
    @ResponseBody
    @RequestMapping("personalinfo/save")
    public ResultJson savePersonalInfoForm(User user) {
        user.setId(UserUtils.getPrincipal().getId());
        return userService.saveOrUpdate(user);
    }

    /**
     * 查询分页集合JSON数据
     *
     * @param user
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping("list")
    @RequiresPermissions("sys:user:view")
    public PageJson list(User user, String pageNum, String pageSize) {
        return userService.findPageList(pageNum, pageSize, user);
    }

    @ResponseBody
    @RequestMapping("password/change")
    @RequiresPermissions("sys:user:edit")
    public ResultJson changeUserPassword(String id, String password) {
        User user = new User();
        user.setId(id);
        user.setPassword(PasswordUtils.entryptPassword(password));
        return userService.saveOrUpdate(user);
    }

    @ResponseBody
    @RequestMapping("mypassword/change")
    @RequiresPermissions("sys:user:edit")
    public ResultJson changeMyPassword(String oldPassword, String password) {
        return userService.changeMyPassword(oldPassword, password);
    }

    /**
     * 新增或修改数据
     *
     * @param user
     * @return
     */
    @ResponseBody
    @RequestMapping("save")
    @RequiresPermissions("sys:user:edit")
    public ResultJson save(User user) {
        return userService.saveOrUpdate(user);
    }

    /**
     * 删除数据
     *
     * @param ids
     * @return
     */
    @ResponseBody
    @RequestMapping("deletes")
    @RequiresPermissions("sys:user:edit")
    public ResultJson delete(String ids) {
        return userService.deletes(ids.split(","));
    }

    /**
     * 显示未选择的角色
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("role/list/{userId}")
    @RequiresPermissions("sys:user:edit")
    public ListJson findUnSelectedRoles(@PathVariable("userId") String userId, String roleName) {
        return userService.findUnSelectedRoles(userId, roleName);
    }

    /**
     * 显示已选择的角色
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("roles/{userId}")
    @RequiresPermissions("sys:user:edit")
    public ListJson findSelectedRoles(@PathVariable("userId") String userId, String roleName) {
        return userService.findUserRoles(userId, roleName);
    }

    /**
     * 保存角色信息
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("roles/{userId}/save")
    @RequiresPermissions("sys:user:edit")
    public ResultJson saveRoles(@PathVariable("userId") String userId, String roles) {
        return userService.saveRoles(userId, roles.split(","));
    }

    /**
     * 删除角色信息
     *
     * @param ids
     * @return
     */
    @ResponseBody
    @RequestMapping("roles/delete")
    @RequiresPermissions("sys:user:edit")
    public ResultJson deleteRoles(String ids) {
        return userService.deleteRoles(ids.split(","));
    }

}