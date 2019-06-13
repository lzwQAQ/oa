package com.kuyuner.bg.approval.controller;

import com.kuyuner.bg.approval.entity.Driver;
import com.kuyuner.bg.approval.service.DriverService;
import com.kuyuner.common.controller.BaseController;
import com.kuyuner.common.controller.ListJson;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.common.lang.StringUtils;
import com.kuyuner.core.sys.entity.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 车辆信息Controller层
 *
 * @author administrator
 */
@Controller
@RequestMapping("${kuyuner.admin-path}/driver")
public class DriverController extends BaseController {

    @Autowired
    private DriverService driverService;

    /**
     * 显示列表页面
     *
     * @return
     */
    @RequestMapping("driver")
    public String showDriverList() {
        return "approval/driverList";
    }

    /**
     * 显示表单页面
     *
     * @param id
     * @param modelMap
     * @return
     */
    @RequestMapping({"form/{id}", "form"})
    public String showDriverForm(@PathVariable(name = "id", required = false) String id, ModelMap modelMap) {
        if (StringUtils.isNotBlank(id)) {
            modelMap.addAttribute("driver", driverService.get(id));
        } else {
            modelMap.addAttribute("driver", new Driver());
        }
        modelMap.addAttribute("users", driverService.findAllUsers());
        return "approval/driverForm";
    }

    /**
     * 查询用户信息
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("findusers")
    public ListJson findUsers() {
        return driverService.findAllUsers();
    }


    /**
     * 查询分页集合JSON数据
     *
     * @param name
     * @param phone
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping("list")
    public PageJson list(String name, String phone, String pageNum, String pageSize) {
        User user = new User();
        user.setName(name);
        user.setPhone(phone);
        Driver driver = new Driver();
        driver.setUser(user);
        return driverService.findPageList(pageNum, pageSize, driver);
    }

    /**
     * 新增或修改数据
     *
     * @param driver
     * @return
     */
    @ResponseBody
    @RequestMapping("save")
    public ResultJson save(Driver driver) {
        return driverService.saveOrUpdate(driver);
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
        return driverService.deletes(ids.split(","));
    }

}