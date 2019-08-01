package com.kuyuner.workflow.runtime.controller;

import com.kuyuner.bg.approval.controller.SequenceFlowNameUtil;
import com.kuyuner.common.controller.ListJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.common.lang.StringUtils;
import com.kuyuner.core.sys.security.UserUtils;
import com.kuyuner.core.sys.service.UserService;
import com.kuyuner.workflow.runtime.service.CreateTaskService;
import com.kuyuner.workflow.runtime.service.PendingTaskService;
import com.kuyuner.workflow.util.BpmnModelUtils;

import io.swagger.annotations.*;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 创建任务
 *
 * @author tangzj
 */
@Api(value = "创建任务")
@Controller
@RequestMapping("${kuyuner.admin-path}/createtask")
public class CreateTaskController {

    @Autowired
    private CreateTaskService createTaskService;

    @Autowired
    private PendingTaskService pendingTaskService;

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private UserService userService;

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
    @ApiOperation(value = "查询任务的form表单")
    public ResultJson findFormPath(@ApiParam(value = "任务类型（qingjia、renshidiaodu、yewushenqing、caigou、caiwushenqing、shouwen、yongche、fawen）",required = true) String modelKey,
                                   @ApiParam(value = "",required = false) String startSequenceFlowName) {
        return createTaskService.getStartFormPath(modelKey, startSequenceFlowName);
    }

    /**
     * 查询下一任务节点是否可以选择候选信息
     */
    @ResponseBody
    @RequestMapping("/isSelectNextTaskCandidateInfos")
    @ApiOperation(value = "查询下一任务节点是否可以选择候选信息")
    public ResultJson isSelectNextTaskCandidateInfos(@ApiParam(value = "流程ID",required = true) String processDefinitionId,
                                                     @ApiParam(value = "",required = false) String sequenceFlowName,
                                                     @ApiParam(value = "",required = false) String startSequenceFlowName,String userId) {
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        UserTask firstUserTask = BpmnModelUtils.getFirstUserTask(bpmnModel, startSequenceFlowName);
        String uid = UserUtils.getPrincipal() == null ? userId : UserUtils.getPrincipal().getId();
        if(StringUtils.isNotBlank(uid)){
            sequenceFlowName = SequenceFlowNameUtil.getSequenceFlowName(userId,sequenceFlowName,userService);
        }
        return pendingTaskService.isSelectNextTaskCandidateInfos(processDefinitionId, firstUserTask.getId(), sequenceFlowName, null,userId);
    }


    /**
     * 查询下一任务节点的候选信息
     */
    @ApiOperation(value = "查询下一任务节点的候选信息")
    @ResponseBody
    @RequestMapping("/findNextTaskCandidateInfos")
//    @ApiResponse()
    public ListJson findNextTaskCandidateInfos(@ApiParam(value = "流程ID",required = true) String processDefinitionId, String searchText, String sequenceFlowName, String startSequenceFlowName,String userId) {

//        sequenceFlowName = StringUtils.isBlank(sequenceFlowName) || "null".equals(sequenceFlowName) ? "同意":sequenceFlowName;
        sequenceFlowName = SequenceFlowNameUtil.getSequenceFlowName(userId,sequenceFlowName,userService);
        return createTaskService.findNextUserTaskCandidateInfos(processDefinitionId, searchText, startSequenceFlowName, sequenceFlowName);
    }

}
