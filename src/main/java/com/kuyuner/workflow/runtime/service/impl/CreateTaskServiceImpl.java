package com.kuyuner.workflow.runtime.service.impl;

import com.kuyuner.common.controller.ListJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.workflow.api.dao.WorkflowDao;
import com.kuyuner.workflow.runtime.dao.CreateTaskDao;
import com.kuyuner.workflow.runtime.service.CreateTaskService;
import com.kuyuner.workflow.util.BpmnModelUtils;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 创建任务
 *
 * @author tangzj
 */
@Service
public class CreateTaskServiceImpl implements CreateTaskService {

    @Autowired
    private CreateTaskDao createTaskDao;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private WorkflowDao workflowDao;

    @Override
    public ListJson findCreatableTasks() {
        return new ListJson(createTaskDao.findCreatableTasks());
    }

    @Override
    public ResultJson getStartFormPath(String modelKey, String startSequenceFlowName) {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(modelKey).latestVersion().singleResult();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());
        UserTask userTask = BpmnModelUtils.getFirstUserTask(bpmnModel, startSequenceFlowName);
        String formPath = workflowDao.getDefaultFormPath(userTask.getId(), processDefinition.getKey());
        StringBuilder builder = new StringBuilder();
        builder.append(formPath);
        builder.append(formPath.indexOf("?") > 0 ? "&" : "?");
        builder.append("modelKey=" + modelKey);
        try {
            builder.append("&taskName=" + URLEncoder.encode(userTask.getName(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        builder.append("&type=create");

        Map<String, Object> map = new HashMap<>(2);
        map.put("formPath", builder.toString());
        map.put("processDefinitionId", processDefinition.getId());
        return ResultJson.ok(map);
    }

    @Override
    public ListJson findNextUserTaskCandidateInfos(String processDefinitionId, String searchText,
                                                   String startSequenceFlowName, String sequenceFlowName) {
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        UserTask firstUserTask = BpmnModelUtils.getFirstUserTask(bpmnModel, startSequenceFlowName);
        UserTask secondTask = BpmnModelUtils.getNextUserTask(bpmnModel, firstUserTask.getId(), sequenceFlowName).get(0);
        String modelKey = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId)
                .singleResult().getKey();
        return new ListJson(workflowDao.findAllCandidateUserInfos(modelKey, secondTask.getId(), searchText));
    }

}
