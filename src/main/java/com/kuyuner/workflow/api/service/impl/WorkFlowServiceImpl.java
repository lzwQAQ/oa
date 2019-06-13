package com.kuyuner.workflow.api.service.impl;

import com.kuyuner.common.idgen.IdGenerate;
import com.kuyuner.common.mapper.JsonMapper;
import com.kuyuner.core.sys.security.UserUtils;
import com.kuyuner.workflow.api.bean.BusinessKey;
import com.kuyuner.workflow.api.bean.TaskBean;
import com.kuyuner.workflow.api.bean.TaskBusinessKey;
import com.kuyuner.workflow.api.bean.TaskInfo;
import com.kuyuner.workflow.api.dao.WorkflowDao;
import com.kuyuner.workflow.api.service.WorkFlowService;
import com.kuyuner.workflow.manage.bean.TaskLog;
import com.kuyuner.workflow.util.BpmnModelUtils;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 工作流对外服务接口
 *
 * @author tangzj
 */
@Service
public class WorkFlowServiceImpl implements WorkFlowService {

    @Autowired
    private TaskService taskService;

    @Autowired
    private IdentityService identityService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private WorkflowDao workflowDao;

    @Autowired
    private HistoryService historyService;

    @Override
    public TaskInfo submitTask(String task, BusinessKey businessKey) {
        return submitTask(task, businessKey, true);
    }

    @Override
    public TaskInfo submitTask(String task, BusinessKey businessKey, boolean useLastCandidates) {
        TaskBean taskBean = JsonMapper.fromJsonString(task, TaskBean.class);
        String userId = UserUtils.getPrincipal().getId();

        // 完成任务
        if (StringUtils.isNotBlank(taskBean.getTaskId())) {
            // 完成任务businessKey的保存
            String taskId = taskBean.getTaskId();
            Task taskEntity = taskService.createTaskQuery().taskId(taskId).singleResult();
            taskService.claim(taskEntity.getId(), userId);
            saveTaskBusinessKey(businessKey, taskEntity);
            return complete(taskId, taskBean.getSequenceFlowName(), taskBean.getUsers(), useLastCandidates);
        }

        // 开启流程并自动完成第一个任务
        ProcessInstance processInstance = startProcessInstanceById(taskBean.getProcessDefinitionId(), userId,
                IdGenerate.uuid(), taskBean.getStartSequenceFlowName());
        // 完成任务businessKey的保存
        Task taskEntity = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        taskService.setAssignee(taskEntity.getId(), userId);
        // 保存businessKey
        saveTaskBusinessKey(businessKey, taskEntity);
        // 自动完成第一个任务
        return complete(taskEntity.getId(), taskBean.getSequenceFlowName(), taskBean.getUsers(), useLastCandidates);
    }

    /**
     * 保存任务的businessKey信息
     *
     * @param businessKey
     * @param task        当前任务
     */
    private void saveTaskBusinessKey(BusinessKey businessKey, Task task) {
        TaskBusinessKey taskBusinessKey = new TaskBusinessKey();
        taskBusinessKey.setId(IdGenerate.uuid());
        taskBusinessKey.setBusinessId(businessKey.getId());
        taskBusinessKey.setBusinessLogId(businessKey.getLogId());
        taskBusinessKey.setCreateDate(new Date());
        taskBusinessKey.setTaskId(task.getId());
        taskBusinessKey.setProcInstId(task.getProcessInstanceId());
        workflowDao.insertTaskBusinessKey(taskBusinessKey);
    }

    /**
     * 根据流程定义ID，开启流程实例
     *
     * @param processDefinitionId
     * @param userId
     * @param businessKey
     * @param sequenceFlowName
     * @return
     */
    private ProcessInstance startProcessInstanceById(String processDefinitionId, String userId, String businessKey,
                                                     String sequenceFlowName) {
        String name = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId)
                .singleResult().getName();
        try {
            identityService.setAuthenticatedUserId(userId);
            Map<String, Object> variables = new HashMap<String, Object>();
            variables.put(SEQUENCE_NAME, sequenceFlowName);
            ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinitionId, businessKey,
                    variables);
            runtimeService.setProcessInstanceName(processInstance.getId(), name);
            return processInstance;
        } finally {
            identityService.setAuthenticatedUserId(null);
        }
    }

    /**
     * 完成任务
     *
     * @param taskId
     * @param sequenceFlowName
     * @param users
     * @param useLastCandidates
     */
    private TaskInfo complete(String taskId, String sequenceFlowName, List<String> users, boolean useLastCandidates) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

        // 查询下一步用户任务
        String processDefinitionId = task.getProcessDefinitionId();
        String taskKey = task.getTaskDefinitionKey();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        List<UserTask> nextUserTasks = BpmnModelUtils.getNextUserTask(bpmnModel, taskKey, sequenceFlowName);
        String processInstanceId = task.getProcessInstanceId();
        String modelKey = bpmnModel.getMainProcess().getId();

        Map<String, Object> resultValues = new HashMap<String, Object>();
        // 检查是否需要设置候选信息
        boolean need = isNeedToSetCandidate(bpmnModel, nextUserTasks, task);
        // 要设置，但是经历过该环节，就使用上次经历的那几个人。并行网关下的环节无法指定环节处理人，所以不需要查询上一次处理人是哪几个
        // 这里不支持并行网关下面有会签，因为这个会增加逻辑复杂度，同时目前的项目中并没有出现这样的情况，如果以后有了，再增加
        if (need && nextUserTasks.size() == 1) {
            List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                    .processInstanceId(processInstanceId).taskDefinitionKey(nextUserTasks.get(0).getId()).list();
            if (useLastCandidates && list.size() > 0) {
                if (users == null) {
                    users = new ArrayList<String>();
                }
                users.clear();
                setLastTimeUsers(list, users);
            }

            // 设置会签人员
            setCounterSignUsers(nextUserTasks.get(0), modelKey, users, resultValues);
        }

        // 设置流程方向
        if (StringUtils.isNotBlank(sequenceFlowName)) {
            resultValues.put(SEQUENCE_NAME, sequenceFlowName);
            // 记录一下当前任务走的哪个方向
            taskService.setVariableLocal(taskId, SEQUENCE_NAME, sequenceFlowName);
        }

        // 保存日志
        saveTaskLog(taskId, task, sequenceFlowName, nextUserTasks);
        // 完成任务
        taskService.complete(taskId, resultValues);

        // 如果不是会签，设置下一步候选人或代理人。nextUserTasks个数大于1，肯定不是
        if (need && (nextUserTasks.size() > 1 || nextUserTasks.get(0).getLoopCharacteristics() == null)) {
            if (nextUserTasks.size() == 1) {
                Task nextTask = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
                setCandidateUsersOrAssignee(nextUserTasks.get(0), modelKey, users, resultValues, nextTask.getId());
            } else {
                List<Task> nextTasks = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
                for (Task t : nextTasks) {
                    setCandidateUsersOrAssignee(nextUserTasks.get(0), modelKey, null, resultValues, t.getId());
                }
            }
        }
        List<Task> list = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
        String taskName = list.size() > 0 ? list.get(0).getName() : "结束";
        TaskInfo taskInfo = new TaskInfo();
        taskInfo.setTaskName(taskName);
        return taskInfo;
    }

    /**
     * 设置之前处理过该环节的人
     *
     * @param list
     * @param users
     */
    private void setLastTimeUsers(List<HistoricTaskInstance> list, List<String> users) {
        // 在会签的情况下，去除重复的用户代理人信息
        Set<String> set = new HashSet<String>();
        for (HistoricTaskInstance instance : list) {
            set.add(instance.getAssignee());
        }
        users.addAll(set);
    }

    private void saveTaskLog(String taskId, Task task, String sequenceFlowName, List<UserTask> nextUserTasks) {
        if (nextUserTasks == null) {
            saveTaskLog(taskId, sequenceFlowName, task, null);
        } else {
            for (UserTask userTask : nextUserTasks) {
                saveTaskLog(taskId, sequenceFlowName, task, userTask);
            }
        }

    }

    private void saveTaskLog(String taskId, String sequenceFlowName, Task task, UserTask nextUserTask) {
        TaskLog taskLog = new TaskLog();
        taskLog.setId(IdGenerate.uuid());
        taskLog.setTaskId(taskId);
        taskLog.setTaskName(task.getName());
        taskLog.setAssignee(task.getAssignee());
        taskLog.setApprovalOpinion(StringUtils.isBlank(sequenceFlowName) ? NEXT_STEP : sequenceFlowName);
        taskLog.setApprovalDate(new Date());
        taskLog.setEventName("提交至" + (nextUserTask != null ? nextUserTask.getName() : "结束") + "环节");
        taskLog.setProInstId(task.getProcessInstanceId());
        workflowDao.insertTaskLog(taskLog);

    }

    /**
     * 判断是否需要设置下一步的候选人信息
     *
     * @param bpmnModel
     * @param nextUserTasks
     * @param task
     * @return
     */
    private boolean isNeedToSetCandidate(BpmnModel bpmnModel, List<UserTask> nextUserTasks, Task task) {
        // 是否还有下一环节
        if (nextUserTasks == null) {
            return false;
        }
        // 当前环节是否是会签或并行任务中的环节，则查询当前任务是不是最后一个，如果是最后一个，那么需要设置候选人
        UserTask userTask = (UserTask) BpmnModelUtils.getFlowElement(bpmnModel, task.getTaskDefinitionKey());
        if (userTask.getLoopCharacteristics() != null || nextUserTasks.size() > 1) {
            long count = taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId()).count();
            if (count == 1) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查会签并设置会签流程变量
     *
     * @param userTask
     * @param modelKey
     * @param users        自定义会签人员
     * @param resultValues 流程变量
     * @return true：设置会签人员成功，false：该任务不是会签
     */
    private void setCounterSignUsers(UserTask userTask, String modelKey, List<String> users, Map<String, Object> resultValues) {
        if (userTask == null || userTask.getLoopCharacteristics() == null) {
            return;
        }

        // 设置会签人员
        if (users == null || users.size() == 0) {
            users = workflowDao.findAllCandidateUsers(modelKey, userTask.getId());
        }
        resultValues.put(COUNTERSIGN_USERS, users);
    }

    /**
     * 设置候选人或代理人
     *
     * @param nextUserTask
     * @param modelKey
     * @param users
     * @param resultValues
     * @param taskId
     */
    private void setCandidateUsersOrAssignee(UserTask nextUserTask, String modelKey, List<String> users,
                                             Map<String, Object> resultValues, String taskId) {
        if (users == null || users.size() == 0) {
            users = workflowDao.findAllCandidateUsers(modelKey, nextUserTask.getId());
        }
        if (users.size() == 1) {
            taskService.setAssignee(taskId, users.get(0));
            return;
        }

        workflowDao.addCandidateUsers(taskId, users);
    }

}