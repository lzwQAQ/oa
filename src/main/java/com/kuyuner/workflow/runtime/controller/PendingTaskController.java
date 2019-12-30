package com.kuyuner.workflow.runtime.controller;

import com.kuyuner.bg.approval.controller.SequenceFlowNameUtil;
import com.kuyuner.common.controller.ListJson;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.core.sys.security.UserUtils;
import com.kuyuner.core.sys.service.UserService;
import com.kuyuner.workflow.runtime.service.PendingTaskService;
import com.kuyuner.workflow.util.BpmnModelUtils;

import io.swagger.annotations.Api;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 运行中流程
 *
 * @author tangzj
 */
@Api(value = "运行中的流程")
@Controller
@RequestMapping("${kuyuner.admin-path}/pendingtask")
public class PendingTaskController {

    @Autowired
    private UserService userService;
    @Autowired
    private PendingTaskService pendingTaskService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private RepositoryService repositoryService;

    @RequestMapping("showCreateCandidateInfoWindow")
    public String showCreateCandidateInfoWindow(){
        return "workflow/candidateInfo";
    }

    @RequestMapping("handle")
    public String handleTask(){
        return "workflow/handleTask";
    }

    /**
     * 查询待办任务
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/findTask")
    public PageJson findTask(String pageNum, String pageSize, String processDefinitionName, String taskName, String modelKey) {
        if(StringUtils.isBlank(pageNum) || StringUtils.isBlank(pageSize)){
            pageNum = "1";
            pageSize = "10000";
        }
        return pendingTaskService.findTask(pageNum, pageSize, processDefinitionName, taskName, modelKey);
    }

    /**
     * 查询下一任务节点是否可以选择候选信息
     */
    @ResponseBody
    @RequestMapping("/isSelectNextTaskCandidateInfos")
    public ResultJson isSelectNextTaskCandidateInfos(String taskId, String sequenceFlowName,String userId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String uid = UserUtils.getPrincipal() == null ? userId : UserUtils.getPrincipal().getId();
        if(StringUtils.isNotBlank(uid) && StringUtils.isBlank(sequenceFlowName)){
            sequenceFlowName = SequenceFlowNameUtil.getSequenceFlowName(userId,sequenceFlowName,userService);
        }
        return pendingTaskService.isSelectNextTaskCandidateInfos(task.getProcessDefinitionId(), task.getTaskDefinitionKey(),
                sequenceFlowName, task.getProcessInstanceId(),userId);
    }

    /**
     * 查询下一任务节点的候选信息
     */
    @ResponseBody
    @RequestMapping("/findNextTaskCandidateInfos")
    public ListJson findNextTaskCandidateInfos(String taskId, String sequenceFlowName, String searchText) {
        return pendingTaskService.findNextUserTaskCandidateInfos(taskId, sequenceFlowName, searchText);
    }

    /**
     * 查询任务的form表单
     *
     * @param taskId
     * @return
     */
    @ResponseBody
    @RequestMapping("/findFormPath")
    public ResultJson findFormPath(String taskId) {
        return pendingTaskService.getDefaultFormPath(taskId);
    }

    /**
     * 获得下一环节可选的顺序流名称
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/findNextSequenceFlowNames")
    public ResultJson findNextSequenceFlowNames(String taskId, String modelKey, String startSequenceFlowName) {

        String taskKey;
        BpmnModel bpmnModel;
        if (StringUtils.isNotBlank(taskId)) {
            Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
            bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
            taskKey = task.getTaskDefinitionKey();
        } else {
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(modelKey).latestVersion().singleResult();
            bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());
            UserTask firstUserTask = BpmnModelUtils.getFirstUserTask(bpmnModel, startSequenceFlowName);
            taskKey = firstUserTask.getId();
        }
        return ResultJson.ok(BpmnModelUtils.findNextSequenceFlowNames(bpmnModel, taskKey));
    }

}
