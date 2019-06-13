package com.kuyuner.workflow.runtime.controller;

import com.kuyuner.common.controller.ListJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.workflow.runtime.service.CreateTaskService;
import com.kuyuner.workflow.runtime.service.PendingTaskService;
import com.kuyuner.workflow.util.BpmnModelUtils;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 创建任务
 *
 * @author tangzj
 */
@Controller
@RequestMapping("${kuyuner.admin-path}/createtask")
public class CreateTaskController {

    @Autowired
    private CreateTaskService createTaskService;

    @Autowired
    private PendingTaskService pendingTaskService;

    @Autowired
    private RepositoryService repositoryService;

    @RequestMapping("createtask")
    public String showCreateTask() {
        return "workflow/createTask";
    }

    /**
     * 查询可创建任务列表
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/findCreatableTasks")
    public ListJson findCreatableTasks() {
        return createTaskService.findCreatableTasks();
    }

    /**
     * 查询任务的form表单
     *
     * @param modelKey
     * @param startSequenceFlowName
     * @return
     */
    @ResponseBody
    @RequestMapping("/findFormPath")
    public ResultJson findFormPath(String modelKey, String startSequenceFlowName) {
        return createTaskService.getStartFormPath(modelKey, startSequenceFlowName);
    }

    /**
     * 查询下一任务节点是否可以选择候选信息
     */
    @ResponseBody
    @RequestMapping("/isSelectNextTaskCandidateInfos")
    public ResultJson isSelectNextTaskCandidateInfos(String processDefinitionId, String sequenceFlowName, String startSequenceFlowName) {
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        UserTask firstUserTask = BpmnModelUtils.getFirstUserTask(bpmnModel, startSequenceFlowName);
        return pendingTaskService.isSelectNextTaskCandidateInfos(processDefinitionId, firstUserTask.getId(), sequenceFlowName, null);
    }


    /**
     * 查询下一任务节点的候选信息
     */
    @ResponseBody
    @RequestMapping("/findNextTaskCandidateInfos")
    public ListJson findNextTaskCandidateInfos(String processDefinitionId, String searchText, String sequenceFlowName, String startSequenceFlowName) {
        return createTaskService.findNextUserTaskCandidateInfos(processDefinitionId, searchText, startSequenceFlowName, sequenceFlowName);
    }
}
