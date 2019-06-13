package com.kuyuner.core.sys.controller;

import com.kuyuner.common.lang.StringUtils;
import com.kuyuner.common.controller.BaseController;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.core.sys.entity.DictType;
import com.kuyuner.core.sys.service.DictTypeService;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 字典类型Service层接口
 *
 * @author administrator
 */
@Controller
@RequestMapping("${kuyuner.admin-path}/dicttype")
public class DictTypeController extends BaseController {

    @Autowired
    private DictTypeService dictTypeService;

    /**
     * 显示列表页面
     *
     * @return
     */
    @RequestMapping("dicttype")
    @RequiresPermissions("sys:dict:view")
    public String showDictTypeList() {
        return "sys/dictTypeList";
    }

    /**
     * 显示表单页面
     *
     * @param id
     * @param modelMap
     * @return
     */
    @RequestMapping({"form/{id}", "form"})
    @RequiresPermissions("sys:dict:edit")
    public String showDictTypeForm(@PathVariable(name = "id", required = false) String id, ModelMap modelMap) {
        if (StringUtils.isNotBlank(id)) {
            modelMap.addAttribute("dictType", dictTypeService.get(id));
        } else {
            modelMap.addAttribute("dictType", new DictType());
        }
        return "sys/dictTypeForm";
    }


    /**
     * 查询分页集合JSON数据
     *
     * @param dictType
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping("list")
    @RequiresPermissions("sys:dict:view")
    public PageJson list(DictType dictType, String pageNum, String pageSize) {
        return dictTypeService.findPageList(pageNum, pageSize, dictType);
    }

    /**
     * 新增或修改数据
     *
     * @param dictType
     * @return
     */
    @ResponseBody
    @RequestMapping("save")
    @RequiresPermissions("sys:dict:edit")
    public ResultJson save(DictType dictType) {
        return dictTypeService.saveOrUpdate(dictType);
    }

    /**
     * 删除数据
     *
     * @param ids
     * @return
     */
    @ResponseBody
    @RequestMapping("deletes")
    @RequiresPermissions("sys:dict:edit")
    public ResultJson delete(String ids) {
        return dictTypeService.deletes(ids.split(","));
    }

}