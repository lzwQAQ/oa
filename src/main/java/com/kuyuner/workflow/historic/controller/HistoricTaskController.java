package com.kuyuner.workflow.historic.controller;

import com.kuyuner.common.controller.ListJson;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.common.lang.StringUtils;
import com.kuyuner.core.sys.security.UserUtils;
import com.kuyuner.workflow.engine.image.HighLightFlowElement;
import com.kuyuner.workflow.historic.service.HistoricTaskService;
import com.kuyuner.workflow.util.BpmnModelUtils;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

/**
 * 历史任务
 *
 * @author tangzj
 */
@Controller
@RequestMapping("${kuyuner.admin-path}/historictask")
public class HistoricTaskController {

    @Autowired
    private HistoricTaskService historyTaskService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private ProcessEngineConfiguration processEngineConfiguration;
    @Autowired
    private RepositoryService repositoryService;

    @RequestMapping("details")
    public String handleTask(){
        return "workflow/historicTask";
    }

    /**
     * 查询历史任务
     *
     * @param processDefinitionName
     * @param modelKey
     * @param taskType 任务类型，表示是审批的还是申请的任务
     * @return
     */
    @ResponseBody
    @RequestMapping("/findHistoricTask")
    public PageJson findHistoricTask(String pageNum, String pageSize, String processDefinitionName,
                                     String personName, String modelKey, String taskType,String userId) {
        if(StringUtils.isBlank(pageNum) || StringUtils.isBlank(pageSize)){
            pageNum = "1";
            pageSize = "10000";
        }
        return historyTaskService.findHistoricTask(UserUtils.getPrincipal()==null?userId:UserUtils.getPrincipal().getId(),
                processDefinitionName, personName, pageNum, pageSize, modelKey, taskType);
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
        return historyTaskService.getDefaultFormPath(taskId);
    }

    /**
     * 展示流程运行的历史轨迹图
     *
     * @param response
     * @param taskId
     * @param ifSpecial 当前任务是否区别显示
     * @throws IOException
     */
    @RequestMapping("/historicTraskImage")
    public void historicTraskImage(HttpServletResponse response, String taskId, String ifSpecial, String finished) throws IOException {

        HistoricTaskInstance task = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
        String processDefinitionId = task.getProcessDefinitionId();
        String processInstanceId = task.getProcessInstanceId();

        HistoricActivityInstanceQuery query = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId).orderByHistoricActivityInstanceEndTime().asc()
                .orderByHistoricActivityInstanceStartTime().asc();
        if ("true".equalsIgnoreCase(finished)) {
            query.finished();
            ifSpecial = "false";
        }
        List<HistoricActivityInstance> historicActivityInstances = query.list();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        List<HighLightFlowElement> highLightedActivities = new ArrayList<HighLightFlowElement>();
        List<HighLightFlowElement> highLightedFlows = new ArrayList<HighLightFlowElement>();
        for (int i = 0, l = historicActivityInstances.size(); i < l; i++) {
            HistoricActivityInstance activityInstance = historicActivityInstances.get(i);
            if ("true".equals(ifSpecial) && task.getTaskDefinitionKey().equals(activityInstance.getActivityId())) {
                highLightedActivities.add(new HighLightFlowElement(activityInstance.getActivityId(), Color.GREEN));
                continue;
            }
            if ("true".equalsIgnoreCase(finished) && i + 1 == l) {
                if (!"exclusiveGateway".equals(activityInstance.getActivityType())) {
                    // 当显示所有已完成任务，如果最后一个节点是网关，那么这个节点排除，否则就不排除
                    highLightedActivities.add(new HighLightFlowElement(activityInstance.getActivityId()));
                }
            } else {
                highLightedActivities.add(new HighLightFlowElement(activityInstance.getActivityId()));
            }
        }

        // 找出相邻流程节点的线路
        for (int i = 0, l = highLightedActivities.size(); i < l; i++) {
            if ((i + 1) == l) {
                // 最后一个流程节点不处理，直接跳出循环
                break;
            }

            String nodeOne = highLightedActivities.get(i).getId();
            String nodeTwo = highLightedActivities.get(i + 1).getId();
            FlowNode flowNodeOne = (FlowNode) BpmnModelUtils.getFlowElement(bpmnModel, nodeOne);
            FlowNode flowNodeTwo = (FlowNode) BpmnModelUtils.getFlowElement(bpmnModel, nodeTwo);
            List<SequenceFlow> sequenceFlows = flowNodeOne.getOutgoingFlows();
            for (SequenceFlow sequenceFlow : sequenceFlows) {
                if (sequenceFlow.getTargetRef().equals(flowNodeTwo.getId())) {
                    highLightedFlows.add(new HighLightFlowElement(sequenceFlow.getId()));
                    break;
                }
            }
        }

        byte[] bytes = BpmnModelUtils.bpmnToPngWithHighLight(bpmnModel, highLightedActivities, highLightedFlows,
                processEngineConfiguration);

        OutputStream outputStream = response.getOutputStream();
        outputStream.write(bytes);
        outputStream.flush();
        outputStream.close();
    }

    /**
     * 查询历史轨迹信息
     *
     * @param taskId
     * @return
     */
    @ResponseBody
    @RequestMapping("/historicTrackInfo")
    public ListJson historicTrackInfo(String taskId) {
        HistoricTaskInstance task = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
        return historyTaskService.findHistoricTrackInfo(task.getProcessInstanceId());
    }

    /**
     * 查询历史环节
     *
     * @param processInstanceId
     * @return
     */
    @ResponseBody
    @RequestMapping("/historicTaskTrackInfo")
    public ResultJson historicTaskTrackInfo(String processInstanceId) {
        return historyTaskService.findHistoricTaskInfo(processInstanceId);
    }

}
