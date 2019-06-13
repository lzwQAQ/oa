package com.kuyuner.core.sys.controller;

import com.kuyuner.common.lang.StringUtils;
import com.kuyuner.common.controller.BaseController;
import com.kuyuner.common.controller.ListJson;
import com.kuyuner.common.persistence.TreeEntity;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.core.sys.entity.Area;
import com.kuyuner.core.sys.service.AreaService;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 区划Service层接口
 *
 * @author administrator
 */
@Controller
@RequestMapping("${kuyuner.admin-path}/area")
public class AreaController extends BaseController {

    @Autowired
    private AreaService areaService;

    /**
     * 显示列表页面
     *
     * @return
     */
    @RequestMapping("area")
    @RequiresPermissions("sys:area:view")
    public String showAreaList() {
        return "sys/areaList";
    }

    /**
     * 显示表单页面
     *
     * @param id
     * @param modelMap
     * @return
     */
    @RequestMapping({"form/{id}", "form"})
    @RequiresPermissions("sys:area:edit")
    public String showAreaForm(@PathVariable(name = "id", required = false) String id, ModelMap modelMap) {
        if (StringUtils.isNotBlank(id)) {
            modelMap.addAttribute("area", areaService.get(id));
        } else {
            modelMap.addAttribute("area", new Area());
        }
        return "sys/areaForm";
    }

    /**
     * 显示选择面板
     *
     * @return
     */
    @RequestMapping("panel")
    @RequiresPermissions("sys:area:edit")
    public String showAreaPanel() {
        return "sys/areaPanel";
    }

    /**
     * 跳转创建子节点页面
     *
     * @param id
     * @param modelMap
     * @return
     */
	@RequestMapping("child/create/{id}")
    @RequiresPermissions("sys:area:edit")
    public String createChildNode(@PathVariable("id") String id, ModelMap modelMap) {
        Area parentArea = areaService.get(id);
        Area area = new Area();
        area.setParentId(parentArea.getId());
        area.getParent().setName(parentArea.getName());
        modelMap.addAttribute("area", area);
        return "sys/areaForm";
    }

    /**
     * 查询分页集合JSON数据
     *
     * @param area
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping("list")
    @RequiresPermissions("sys:area:view")
    public PageJson list(Area area, String pageNum, String pageSize) {
        return areaService.findPageList(pageNum, pageSize, area);
    }

    /**
     * 新增或修改数据
     *
     * @param area
     * @return
     */
    @ResponseBody
    @RequestMapping("save")
    @RequiresPermissions("sys:area:edit")
    public ResultJson save(Area area) {
        return areaService.saveOrUpdate(area);
    }

    /**
     * 删除数据
     *
     * @param ids
     * @return
     */
    @ResponseBody
    @RequestMapping("deletes")
    @RequiresPermissions("sys:area:edit")
    public ResultJson delete(String ids) {
        return areaService.deletes(ids.split(","));
    }

    /**
     * 查询单个节点下的所有子节点
     *
     * @param area
     * @param nodeid 父节点ID
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "tree/list")
    @RequiresPermissions("sys:area:view")
    public ListJson listTree(Area area, String nodeid) {
        area.setParentId(nodeid);
        if (StringUtils.isBlank(area.getParentId())) {
            area.setTreeLevel(TreeEntity.ROOT_LEVEL);
        }
        return areaService.findList(area);
    }
}