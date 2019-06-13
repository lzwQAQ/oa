package com.kuyuner.workflow.manage.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kuyuner.common.controller.ListJson;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.common.idgen.IdGenerate;
import com.kuyuner.common.persistence.Page;
import com.kuyuner.core.sys.security.UserUtils;
import com.kuyuner.workflow.manage.bean.EditorSource;
import com.kuyuner.workflow.manage.bean.TaskAuth;
import com.kuyuner.workflow.manage.bean.TaskForm;
import com.kuyuner.workflow.manage.bean.activitimodel.ActivitiModel;
import com.kuyuner.workflow.manage.bean.activitimodel.Properties;
import com.kuyuner.workflow.manage.dao.EstablishDao;
import com.kuyuner.workflow.manage.service.EstablishService;
import com.kuyuner.workflow.util.BpmnModelUtils;
import com.kuyuner.workflow.util.BpmnValidationUtil;

import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ModelQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

import javax.annotation.Resource;

import static com.kuyuner.workflow.api.service.WorkFlowService.COUNTERSIGN_USER;
import static com.kuyuner.workflow.api.service.WorkFlowService.COUNTERSIGN_USERS;
import static com.kuyuner.workflow.api.service.WorkFlowService.SEQUENCE_NAME;

/**
 * 流程管理-创建流程
 *
 * @author chenxy
 */
@Service
public class EstablishServiceImpl implements EstablishService {

    @Resource
    private EstablishDao establishDao;

    @Autowired
    private RepositoryService repositoryService;

    @Resource
    private ProcessEngineConfiguration processEngineConfiguration;

    /**
     * 查询流程模板
     */
    @Override
    public PageJson findWorkFlowModel(String pageNum, String pageSize, String modelKey, String modelName) {
        Page page = new Page(pageNum, pageSize);
        page.start();
        establishDao.findWorkFlowModel(modelKey, modelName);
        page.end();
        return new PageJson(page);
    }

    /**
     * 检查流程Key是否存在
     */
    @Override
    public int checkWorkFlowModelKey(boolean isCopy, String modelId, String modelKey) {
        if (StringUtils.isBlank(modelId) || isCopy) {
            return establishDao.checkWorkFlowModelKey1(modelKey);
        } else {
            return establishDao.checkWorkFlowModelKey2(modelId, modelKey);
        }
    }

    /**
     * 保存流程模板
     */
    @Override
    public ResultJson saveWorkFlowModel(String modelKey, String modelName, String bpmnModel) {
        ObjectMapper objectMapper = new ObjectMapper();
        Model modelData = repositoryService.newModel();

        ObjectNode modelObjectNode = objectMapper.createObjectNode();
        modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, modelName);
        modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
        modelData.setMetaInfo(modelObjectNode.toString());
        modelData.setName(modelName);
        modelData.setKey(modelKey);
        repositoryService.saveModel(modelData);

        String modelId = modelData.getId();
        String json = "";
        if (bpmnModel == null) {
            ActivitiModel activitiModel = new ActivitiModel();
            activitiModel.getStencilset().setNamespace("http://b3mn.org/stencilset/bpmn2.0#");
            Properties properties = activitiModel.getProperties();
            properties.setName(modelName);
            properties.setProcess_id(modelKey);
            properties.setProcess_author(UserUtils.getPrincipal().getUsername());
            properties.setProcess_namespace("com.kuyuner.workflow");
            try {
                json = objectMapper.writeValueAsString(activitiModel);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        } else {
            json = updateBpmnModel(bpmnModel, modelKey, modelName).toString();
        }

        try {
            repositoryService.addModelEditorSource(modelId, json.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("modelId", modelId);
        map.put("modelName", modelName);
        return ResultJson.ok(map);
    }

    /**
     * 修改流程模板
     */
    @Override
    public ResultJson updateWorkFlowModel(String modelId, String modelKey, String modelName, String bpmnModel) {
        Model model = repositoryService.createModelQuery().modelId(modelId).singleResult();
        ObjectNode modelObjectNode = new ObjectMapper().createObjectNode();
        modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, modelName);
        modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, model.getVersion() + 1);
        model.setMetaInfo(modelObjectNode.toString());
        model.setName(modelName);
        model.setKey(modelKey);
        repositoryService.saveModel(model);
        String json = "";
        if (bpmnModel == null) {
            JsonNode jsonNode = null;
            try {
                jsonNode = new ObjectMapper().readTree(repositoryService.getModelEditorSource(modelId));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            json = updateProperties(jsonNode, modelKey, modelName).toString();
        } else {
            json = updateBpmnModel(bpmnModel, modelKey, modelName).toString();
        }
        try {
            repositoryService.addModelEditorSource(modelId, json.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return ResultJson.ok();
    }

    /**
     * 根据传参，更新bpmnmodel的信息
     *
     * @param bpmnModelJson
     * @param modelKey
     * @param modelName
     * @return
     */
    private ObjectNode updateBpmnModel(String bpmnModelJson, String modelKey, String modelName) {
        try {
            ObjectNode jsonNode = (ObjectNode) new ObjectMapper().readTree(bpmnModelJson);
            return updateProperties(jsonNode, modelKey, modelName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 更新流程模板的属性
     *
     * @param jsonNode
     * @return
     */
    private ObjectNode updateProperties(JsonNode jsonNode, String modelKey, String modelName) {
        ObjectNode properties = (ObjectNode) jsonNode.get("properties");
        properties.put("process_author", UserUtils.getPrincipal().getUsername());
        properties.put("process_namespace", "cn.com.sparknet.workflow");
        properties.put("process_id", modelKey);
        properties.put("name", modelName);
        return (ObjectNode) jsonNode;
    }

    /**
     * 复制流程模板
     */
    @Override
    public ResultJson copyWorkFlowModel(String modelId, String modelKey, String modelName) {
        // 保存模板
        ResultJson resultJson = this.saveWorkFlowModel(modelKey, modelName, null);
        Map<String, Object> map = (Map<String, Object>) resultJson.getData();
        String newModelId = map.get("modelId").toString();
        // 获取模板资源
        ObjectNode jsonNode = null;
        try {
            jsonNode = (ObjectNode) new ObjectMapper().readTree(repositoryService.getModelEditorSource(modelId));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String json = updateProperties(jsonNode, modelKey, modelName).toString();
        try {
            repositoryService.addModelEditorSource(newModelId, json.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        // 添加资源文件
        byte[] picResource = repositoryService.getModelEditorSourceExtra(modelId);
        repositoryService.addModelEditorSourceExtra(newModelId, picResource);
        return ResultJson.ok();
    }

    /**
     * 查询流程模板详细
     */
    @Override
    public ResultJson findWorkFlowModelById(String id) {
        ModelQuery modelQuery = repositoryService.createModelQuery();
        Model model = modelQuery.modelId(id).singleResult();
        Map<String, String> map = new HashMap<>();
        map.put("modelId", model.getId());
        map.put("modelKey", model.getKey());
        map.put("modelName", model.getName());
        return ResultJson.ok(map);
    }

    /**
     * 删除流程模板
     */
    @Override
    public ResultJson deleteWorkFlowModel(String ids) {
        String[] modelIds = ids.split(",");
        for (String modelId : modelIds) {
            repositoryService.deleteModel(modelId);
        }
        return ResultJson.ok();
    }

    /**
     * 部署流程模板
     */
    @Override
    public ResultJson deployWorkFlowModel(String modelId) {
        Model modelData = repositoryService.getModel(modelId);
        JsonNode jsonNode = null;
        try {
            jsonNode = new ObjectMapper().readTree(repositoryService.getModelEditorSource(modelData.getId()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        BpmnModel bpmnModel = BpmnModelUtils.jsonToBpmnModel(jsonNode);
        List<String> errorList = BpmnValidationUtil.isValid(bpmnModel);
        if (errorList.size() > 0) {
            return ResultJson.failed("流程图不符合规范:" + errorList.get(0));
        }
        parseBomnModel(bpmnModel);
        errorList = BpmnValidationUtil.isValid(bpmnModel,
                (ProcessEngineConfigurationImpl) processEngineConfiguration);
        if (errorList.size() > 0) {
            return ResultJson.failed("流程图不符合规范:" + errorList.get(0));
        }

        // 部署流程
        repositoryService.createDeployment().category(modelData.getCategory())
                .addBpmnModel(modelData.getName() + ".bpmn20.xml", bpmnModel).deploy();

        return ResultJson.ok();
    }

    /**
     * 解析排他网关、会签、第一个任务
     *
     * @param bpmnModel
     */
    private void parseBomnModel(BpmnModel bpmnModel) {

        List<Process> processes = bpmnModel.getProcesses();
        for (Process process : processes) {
            List<UserTask> userTasks = process.findFlowElementsOfType(UserTask.class);
            for (UserTask userTask : userTasks) {
                // 检查是否为会签
                if (BpmnModelUtils.isCounterSign(bpmnModel, userTask.getId())) {
                    paserCouterSign(userTask);
                    continue;
                }
            }

            // 解析排他网关
            List<ExclusiveGateway> exclusiveGateways = process.findFlowElementsOfType(ExclusiveGateway.class);
            for (Gateway gateway : exclusiveGateways) {
                List<SequenceFlow> sequenceFlows = gateway.getOutgoingFlows();
                String defaultFlowId = gateway.getDefaultFlow();
                parseSequenceFlow(sequenceFlows, defaultFlowId);
            }
        }
    }

    /**
     * 解析会签信息
     *
     * @param userTask
     */
    private void paserCouterSign(UserTask userTask) {
        MultiInstanceLoopCharacteristics characteristics = userTask.getLoopCharacteristics();
        characteristics.setElementVariable(COUNTERSIGN_USER);
        characteristics.setInputDataItem("${" + COUNTERSIGN_USERS + "}");
        userTask.setAssignee("${" + COUNTERSIGN_USER + "}");
    }

    /**
     * 解析顺序流，在顺序流上添加流程条件
     *
     * @param sequenceFlows
     * @param defaultFlowId
     */
    private void parseSequenceFlow(List<SequenceFlow> sequenceFlows, String defaultFlowId) {
        if (sequenceFlows == null) {
            return;
        }
        StringBuilder builder = new StringBuilder();
        for (SequenceFlow sequenceFlow : sequenceFlows) {
            if (sequenceFlow.getId().equals(defaultFlowId)) {
                continue;
            }
            String name = sequenceFlow.getName();
            builder.append("${");
            builder.append(SEQUENCE_NAME);
            builder.append("==\"");
            builder.append(name);
            builder.append("\"}");
            sequenceFlow.setConditionExpression(builder.toString());
            builder.setLength(0);
        }
    }

    @Override
    public List<Map<String, String>> findBpmnModels(String ids) {
        List<EditorSource> list = establishDao.findModelEditorSource(ids.split(","));
        List<Map<String, String>> jsonModels = new ArrayList<Map<String, String>>();
        StringBuilder builder = new StringBuilder();
        for (EditorSource source : list) {
            Map<String, String> bpmnModelMap = new HashMap<>();
            builder.append(source.getName()).append(".");
            builder.append(source.getKey()).append(".");
            try {
                bpmnModelMap.put(builder.toString(), new String(source.getContent(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
            jsonModels.add(bpmnModelMap);
            builder.setLength(0);
        }
        return jsonModels;
    }

    @Override
    public ListJson findRole(String searchText) {
        return new ListJson(establishDao.findRole(searchText));
    }

    @Override
    public ListJson findDept(String searchText) {
        return new ListJson(establishDao.findDept(searchText));
    }

    @Override
    public ListJson findOrg(String searchText) {
        return new ListJson(establishDao.findOrg(searchText));
    }

    @Override
    public ResultJson saveFormAuth(String modelKey, String taskKey, String roles, String depts, String orgs) {
        List<TaskAuth> list = new ArrayList<TaskAuth>();
        if (StringUtils.isNotBlank(roles)) {
            String[] roleIds = roles.split(",");
            newFormAuth(roleIds, "role", list, modelKey, taskKey);
        }
        if (StringUtils.isNotBlank(depts)) {
            String[] deptIds = depts.split(",");
            newFormAuth(deptIds, "dept", list, modelKey, taskKey);
        }
        if (StringUtils.isNotBlank(orgs)) {
            String[] orgIds = orgs.split(",");
            newFormAuth(orgIds, "org", list, modelKey, taskKey);
        }
        establishDao.deleteFormAuth(modelKey, taskKey);
        for (TaskAuth auth : list) {
            establishDao.insertTaskAuth(auth);
        }
        return ResultJson.ok();
    }

    private void newFormAuth(String[] ids, String type, List<TaskAuth> list, String modelKey, String taskKey) {
        Set<String> set = new HashSet<String>();
        set.addAll(Arrays.asList(ids));
        Iterator<String> iter = set.iterator();
        while (iter.hasNext()) {
            TaskAuth auth = new TaskAuth();
            auth.setCreateDate(new Date());
            auth.setId(IdGenerate.uuid());
            auth.setModelKey(modelKey);
            auth.setObjectId(iter.next());
            auth.setObjectType(type);
            auth.setTaskKey(taskKey);
            list.add(auth);
        }
    }

    @Override
    public ResultJson findTaskAuth(String modelKey, String taskKey) {
        return ResultJson.ok(establishDao.findTaskAuth(modelKey, taskKey));
    }

    @Override
    public ResultJson findFormPath(String modelKey, String taskKey) {
        return ResultJson.ok(establishDao.findFormPath(modelKey, taskKey));
    }

    @Override
    public ResultJson saveOrUpdateFormPath(TaskForm taskForm) {
        taskForm.setFormPath(parsePath(taskForm.getFormPath()));
        if (StringUtils.isBlank(taskForm.getId())) {
            taskForm.setId(StringUtils.isBlank(taskForm.getId()) ? IdGenerate.uuid() : taskForm.getId());
            establishDao.insertForm(taskForm);
        } else {
            establishDao.updateForm(taskForm);
        }
        return ResultJson.ok();
    }

    /**
     * 删除字符串中的回车和换行
     *
     * @param formPath
     * @return
     */
    private String parsePath(String formPath) {
        return formPath.replaceAll("\r", "").replaceAll("\n", "");
    }

}
