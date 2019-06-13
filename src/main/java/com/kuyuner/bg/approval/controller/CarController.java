package com.kuyuner.bg.approval.controller;

import com.kuyuner.bg.approval.entity.Car;
import com.kuyuner.bg.approval.service.CarService;
import com.kuyuner.common.controller.BaseController;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.common.lang.StringUtils;

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
@RequestMapping("${kuyuner.admin-path}/car")
public class CarController extends BaseController {

    @Autowired
    private CarService carService;

    /**
     * 显示列表页面
     *
     * @return
     */
    @RequestMapping("car")
    public String showCarList() {
        return "approval/carList";
    }

    /**
     * 显示表单页面
     *
     * @param id
     * @param modelMap
     * @return
     */
    @RequestMapping({"form/{id}", "form"})
    public String showCarForm(@PathVariable(name = "id", required = false) String id, ModelMap modelMap) {
        if (StringUtils.isNotBlank(id)) {
            modelMap.addAttribute("car", carService.get(id));
        } else {
            modelMap.addAttribute("car", new Car());
        }
        return "approval/carForm";
    }


    /**
     * 查询分页集合JSON数据
     *
     * @param car
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping("list")
    public PageJson list(Car car, String pageNum, String pageSize) {
        return carService.findPageList(pageNum, pageSize, car);
    }

    /**
     * 新增或修改数据
     *
     * @param car
     * @return
     */
    @ResponseBody
    @RequestMapping("save")
    public ResultJson save(Car car) {
        return carService.saveOrUpdate(car);
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
        return carService.deletes(ids.split(","));
    }

}