package com.kuyuner.workflow.manage.service;

import com.kuyuner.common.controller.ListJson;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.workflow.manage.bean.TaskForm;

import java.util.List;
import java.util.Map;

/**
 * 流程管理-创建流程
 *
 * @author chenxy
 */
public interface EstablishService {

    /**
     * 查询流程模板
     */
    public PageJson findWorkFlowModel(String start, String limit, String modelKey, String modelName);

    /**
     * 检查流程Key是否存在
     */
    public int checkWorkFlowModelKey(boolean isCopy, String modelId, String modelKey);

    /**
     * 保存流程模板
     *
     * @param modelKey
     * @param modelName
     * @param bpmnModelr
     */
    public ResultJson saveWorkFlowModel(String modelKey, String modelName, String bpmnModelr);

    /**
     * 复制流程模板
     */
    public ResultJson copyWorkFlowModel(String modelId, String modelKey, String modelName);

    /**
     * 查询流程模板详细
     */
    public ResultJson findWorkFlowModelById(String id);

    /**
     * 修改流程模板
     *
     * @param bpmnModel
     */
    public ResultJson updateWorkFlowModel(String modelId, String modelKey, String modelName, String bpmnModel);

    /**
     * 删除流程模板
     */
    public ResultJson deleteWorkFlowModel(String ids);

    /**
     * 部署流程模板
     */
    public ResultJson deployWorkFlowModel(String modelId);

    /**
     * 根据流程模板ID查询模型
     *
     * @param ids
     * @return
     */
    public List<Map<String, String>> findBpmnModels(String ids);

    /**
     * 查询角色信息
     *
     * @param searchText
     * @return
     */
    public ListJson findRole(String searchText);

    /**
     * 查询角色信息
     *
     * @param searchText
     * @return
     */
    public ListJson findDept(String searchText);

    /**
     * 查询机构信息
     *
     * @param searchText
     * @return
     */
    public ListJson findOrg(String searchText);

    /**
     * 保存任务权限
     *
     * @param modelKey
     * @param taskKey
     * @param depts
     * @param orgs
     * @return
     */
    public ResultJson saveFormAuth(String modelKey, String taskKey, String roles, String depts, String orgs);

    /**
     * 查询任务权限
     *
     * @param modelKey
     * @param taskKey
     * @return
     */
    public ResultJson findTaskAuth(String modelKey, String taskKey);

    /**
     * 保存表单路径
     *
     * @param taskForm
     * @return
     */
    public ResultJson saveOrUpdateFormPath(TaskForm taskForm);

    /**
     * 查询表单路径
     *
     * @param modelKey
     * @param taskKey
     * @return
     */
    public ResultJson findFormPath(String modelKey, String taskKey);

}
