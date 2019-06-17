package com.kuyuner.core.sys.controller;

import com.kuyuner.common.lang.StringUtils;
import com.kuyuner.common.controller.BaseController;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.core.sys.entity.DictData;
import com.kuyuner.core.sys.service.DictDataService;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 字典数据Service层接口
 *
 * @author administrator
 */
@Controller
@RequestMapping("${kuyuner.admin-path}/dictdata")
public class DictDataController extends BaseController {

    @Autowired
    private DictDataService dictDataService;

    /**
     * 显示列表页面
     *
     * @return
     */
    @RequestMapping("dictdata")
    @RequiresPermissions("sys:dict:view")
    public String showDictDataList() {
        return "sys/dictDataList";
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
    public String showDictDataForm(@PathVariable(name = "id", required = false) String id, ModelMap modelMap) {
        if (StringUtils.isNotBlank(id)) {
            modelMap.addAttribute("dictData", dictDataService.get(id));
        } else {
            modelMap.addAttribute("dictData", new DictData());
        }
        modelMap.addAttribute("dictTypes", dictDataService.findAllTypes());
        return "sys/dictDataForm";
    }


    /**
     * 查询分页集合JSON数据
     *
     * @param dictData
     * @param dictType
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping("list/{dictType}")
    @RequiresPermissions("sys:dict:view")
    public PageJson list(DictData dictData, @PathVariable("dictType") String dictType, String pageNum, String pageSize) {
        dictData.setDictType(dictType);
        return dictDataService.findPageList(pageNum, pageSize, dictData);
    }

    /**
     * 新增或修改数据
     *
     * @param dictData
     * @return
     */
    @ResponseBody
    @RequestMapping("save")
    @RequiresPermissions("sys:dict:edit")
    public ResultJson save(DictData dictData) {
        return dictDataService.saveOrUpdate(dictData);
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
        return dictDataService.deletes(ids.split(","));
    }

    /**
     * 获取字典信息
     * @param dictType
     * @return
     */
    @RequestMapping("getDictByType")
    @ResponseBody
    public ResultJson getDictByType(String dictType) {
        List<DictData> data = dictDataService.getDictByType(dictType);
        return ResultJson.ok(data);
    }
}