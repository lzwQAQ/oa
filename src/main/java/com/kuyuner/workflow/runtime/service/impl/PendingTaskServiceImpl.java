package com.kuyuner.workflow.runtime.service.impl;

import com.kuyuner.common.controller.ListJson;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.common.persistence.Page;
import com.kuyuner.core.sys.security.UserUtils;
import com.kuyuner.workflow.api.bean.BusinessKey;
import com.kuyuner.workflow.api.dao.WorkflowDao;
import com.kuyuner.workflow.runtime.dao.PendingTaskDao;
import com.kuyuner.workflow.runtime.service.PendingTaskService;
import com.kuyuner.workflow.util.BpmnModelUtils;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * 运行中流程
 *
 * @author tangzj
 */
@Service
public class PendingTaskServiceImpl implements PendingTaskService {

    @Autowired
    private PendingTaskDao pendingTaskDao;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private WorkflowDao workflowDao;

    @Autowired
    private HistoryService historyService;

    @Override
    public PageJson findTask(String pageNum, String pageSize, String processDefinitionName, String taskName, String modelKey) {
        Page page = new Page(pageNum, pageSize);
        page.start();
        pendingTaskDao.findTask(UserUtils.getPrincipal().getId(), processDefinitionName, taskName, modelKey);
        page.end();
        return new PageJson(page);
    }

    @Override
    public ResultJson getDefaultFormPath(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(task.getProcessDefinitionId()).singleResult();
        String formPath = workflowDao.getDefaultFormPath(task.getTaskDefinitionKey(), processDefinition.getKey());
        BusinessKey businessKey = pendingTaskDao.getTaskBusinessKey(task.getProcessInstanceId());
        StringBuilder builder = new StringBuilder();
        builder.append(formPath);
        builder.append(formPath.indexOf("?") > 0 ? "&" : "?");
        builder.append("businessId=" + businessKey.getId());
        builder.append("&taskId=" + task.getId());
        builder.append("&modelKey=" + processDefinition.getKey());
        try {
            builder.append("&taskName=" + URLEncoder.encode(task.getName(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        builder.append("&type=pending");

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("formPath", builder.toString());
        map.put("taskName", task.getName());
        map.put("taskKey", task.getTaskDefinitionKey());
        map.put("modelKey", processDefinition.getKey());
        map.put("processDefinitionName", processDefinition.getName());
        map.put("processDefinitionId", processDefinition.getId());
        return ResultJson.ok(map);
    }

    @Override
    public ResultJson isSelectNextTaskCandidateInfos(String processDefinitionId, String taskKey,
                                                     String sequenceFlowName, String processInstanceId) {

        //检查是否为会签
        if (StringUtils.isNotBlank(processInstanceId)) {
            // 查询当前是否是最后一个任务，在会签环节下，会有多个任务，只有最后一个任务的时候才会让用户选择下一环节处理人
            long count = taskService.createTaskQuery().processInstanceId(processInstanceId).count();
            if (count != 1) {
                // 不是最后一个任务，直接返回false
                return ResultJson.ok("false");
            }
        }

        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        // 检查顺序流是否正确，不正确，直接返回错误原因
        boolean check = BpmnModelUtils.checkSequenceFlowName(bpmnModel, taskKey, sequenceFlowName);
        if (!check) {
            return ResultJson.ok("sequenceFlowName error");
        }

        // 是否还有下一环节,如果没有下一环节，直接返回false；并行网关由于会有多个环节，无法设置下一环节处理人，也给前台返回false
        List<UserTask> nextUserTasks = BpmnModelUtils.getNextUserTask(bpmnModel, taskKey, sequenceFlowName);
        if (nextUserTasks == null || nextUserTasks.size() > 1) {
            return ResultJson.ok("false");
        }

        // 下一环节是否经历过，如果经历过，直接返回false
        if (StringUtils.isNotBlank(processInstanceId)) {
            boolean f = isExperienced(nextUserTasks.get(0), processInstanceId);
            if (f) {
                return ResultJson.ok("false");
            }
        }

        // 检查是否设置了候选人信息
        String str = bpmnModel.getMainProcess().getId();
        String s = nextUserTasks.get(0).getId();
        List<Map<String, Object>> list = workflowDao.findAllCandidateUserInfos(str, s
                , null);
        if (list.size() == 0) {
            return ResultJson.ok("no person error");
        }
        return ResultJson.ok("true");
    }

    /**
     * 下一环节是否经历过
     *
     * @param nextUserTask
     * @param processInstanceId
     * @return true：经历过了；false：没有
     */
    private boolean isExperienced(UserTask nextUserTask, String processInstanceId) {
        long count = historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId)
                .taskDefinitionKey(nextUserTask.getId()).count();
        return count > 0;
    }

    @Override
    public ListJson findNextUserTaskCandidateInfos(String taskId, String sequenceFlowName, String searchText) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
        List<UserTask> userTasks = BpmnModelUtils.getNextUserTask(bpmnModel, task.getTaskDefinitionKey(), sequenceFlowName);
        ListJson listJson = new ListJson();
        if (userTasks == null || userTasks.size() > 1) {
            return new ListJson(Collections.emptyList());
        }
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(task.getProcessDefinitionId()).singleResult();
        String modelKey = processDefinition.getKey();
        String taskKey = userTasks.get(0).getId();

        // 查询可以选择的人
        List<Map<String, Object>> list = workflowDao.findAllCandidateUserInfos(modelKey, taskKey, searchText);

        return new ListJson(list);
    }

}
