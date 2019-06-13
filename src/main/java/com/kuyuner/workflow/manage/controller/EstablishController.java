package com.kuyuner.workflow.manage.controller;

import com.kuyuner.common.controller.ListJson;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.workflow.manage.bean.TaskForm;
import com.kuyuner.workflow.manage.service.EstablishService;
import com.kuyuner.workflow.util.BpmnModelUtils;
import com.kuyuner.workflow.util.DownLoadUtil;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 流程管理-创建流程
 *
 * @author chenxy
 */
@Controller
@RequestMapping("${kuyuner.admin-path}/establish")
public class EstablishController {

    @Resource
    private EstablishService establishService;

    @RequestMapping("establish")
    public String showEstablishList() {
        return "workflow/establishList";
    }

    /**
     * 显示流程在线编辑器
     */
    @RequestMapping("/showModeler")
    public String showModeler() {
        return "workflow/modeler";
    }

    /**
     * 查询流程模板
     */
    @ResponseBody
    @RequestMapping("/findWorkFlowModel")
    public PageJson findWorkFlowModel(String pageNum, String pageSize, String modelKey, String modelName) {
        return establishService.findWorkFlowModel(pageNum, pageSize, modelKey, modelName);
    }

    /**
     * 保存流程模板
     */
    @ResponseBody
    @RequestMapping("/saveOrUpdateWorkFlowModel")
    public ResultJson saveWorkFlowModel(HttpServletRequest request, String modelId, String modelKey, String modelName) throws Exception {
        String bpmnModel = BpmnModelUtils.uploadFileToBpmnModel(request);
        ResultJson editJson = checkModelKey(false, modelId, modelKey);
        if (editJson != null) {
            return editJson;
        }
        if (StringUtils.isNotBlank(modelId)) {
            return establishService.updateWorkFlowModel(modelId, modelKey, modelName, bpmnModel);
        } else {
            return establishService.saveWorkFlowModel(modelKey, modelName, bpmnModel);
        }
    }

    /**
     * 复制流程模板
     */
    @ResponseBody
    @RequestMapping("/copyWorkFlowModel")
    public ResultJson copyWorkFlowModel(String modelId, String modelKey, String modelName) {
        ResultJson resultJson = checkModelKey(true, modelId, modelKey);
        if (resultJson != null) {
            return resultJson;
        }
        return establishService.copyWorkFlowModel(modelId, modelKey, modelName);
    }

    /**
     * 检查modelkey是否重复
     *
     * @param isCopy
     * @param modelId
     * @param modelKey
     * @return
     */
    private ResultJson checkModelKey(boolean isCopy, String modelId, String modelKey) {
        if (establishService.checkWorkFlowModelKey(isCopy, modelId, modelKey) > 0) {
            return ResultJson.failed("key重复");
        }
        return null;
    }

    /**
     * 查询流程模板详细
     */
    @ResponseBody
    @RequestMapping("/findWorkFlowModelById")
    public ResultJson findWorkFlowModelById(String id) {
        return establishService.findWorkFlowModelById(id);
    }

    /**
     * 删除流程模板
     */
    @ResponseBody
    @RequestMapping("/deleteWorkFlowModel")
    public ResultJson deleteWorkFlowModel(String ids) {
        return establishService.deleteWorkFlowModel(ids);
    }

    /**
     * 部署流程模板
     */
    @ResponseBody
    @RequestMapping("/deployWorkFlowModel")
    public ResultJson deployWorkFlowModel(String modelId) {
        return establishService.deployWorkFlowModel(modelId);
    }

    /**
     * 导出流程模板
     */
    @RequestMapping("/exportModel")
    public void exportModel(HttpServletRequest request, HttpServletResponse response) {
        List<Map<String, String>> list = establishService.findBpmnModels(request.getParameter("ids"));
        DownLoadUtil.downloadForBpmn(request, response, list);
    }

    /**
     * 查询权限信息
     */
    @ResponseBody
    @RequestMapping("/findRole")
    public ListJson findRole(String searchText) {
        return establishService.findRole(searchText);
    }

    /**
     * 查询部门信息
     */
    @ResponseBody
    @RequestMapping("/findDept")
    public ListJson findDept(String searchText) {
        return establishService.findDept(searchText);
    }

    /**
     * 查询机构信息
     */
    @ResponseBody
    @RequestMapping("/findOrg")
    public ListJson findOrg(String searchText) {
        return establishService.findOrg(searchText);
    }

    /**
     * 查询任务权限
     */
    @ResponseBody
    @RequestMapping("/findTaskAuth")
    public ResultJson findTaskAuth(String modelKey, String taskKey) {
        return establishService.findTaskAuth(modelKey, taskKey);
    }

    /**
     * 保存任务权限
     */
    @ResponseBody
    @RequestMapping("/saveFormAuth")
    public ResultJson saveFormAuth(String modelKey, String taskKey, String roles, String depts, String orgs) {
        return establishService.saveFormAuth(modelKey, taskKey, roles, depts, orgs);
    }

    /**
     * 查询表单路径
     */
    @ResponseBody
    @RequestMapping("/findFormPath")
    public ResultJson findFormPath(String modelKey, String taskKey) {
        return establishService.findFormPath(modelKey, taskKey);
    }

    /**
     * 保存表单路径
     */
    @ResponseBody
    @RequestMapping("/saveFormPath")
    public ResultJson saveFormPath(TaskForm taskForm) {
        return establishService.saveOrUpdateFormPath(taskForm);
    }

}
