package com.kuyuner.workflow.historic.service.impl;

import com.kuyuner.common.controller.ListJson;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.common.persistence.Page;
import com.kuyuner.workflow.api.bean.BusinessKey;
import com.kuyuner.workflow.api.dao.WorkflowDao;
import com.kuyuner.workflow.historic.dao.HistoricTaskDao;
import com.kuyuner.workflow.historic.service.HistoricTaskService;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 历史任务
 *
 * @author tangzj
 */
@Service
public class HistoricTaskServiceImpl implements HistoricTaskService {

    @Autowired
    private HistoricTaskDao historicTaskDao;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private WorkflowDao workflowDao;

    @Autowired
    private RuntimeService runtimeService;

    @Override
    public PageJson findHistoricTask(String userId, String processDefinitionName, String personName, String pageNum, String pageSize, String modelKey, String taskType) {
        Page page = new Page(pageNum, pageSize);
        page.start();
        historicTaskDao.findHistoricTask(userId, processDefinitionName, personName, modelKey, taskType);
        page.end();
        return new PageJson(page);
    }

    @Override
    public ResultJson getDefaultFormPath(String taskId) {
        HistoricTaskInstance task = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
        String taskKey = task.getTaskDefinitionKey();
        String processInstanceId = task.getProcessInstanceId();
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();
        String modelKey = historicProcessInstance.getProcessDefinitionKey();
        String formPath = workflowDao.getDefaultFormPath(taskKey, modelKey);
        BusinessKey businessKey = historicTaskDao.getHistoricTaskBusinessKey(taskId);
        StringBuilder builder = new StringBuilder();
        builder.append(formPath);
        builder.append(formPath.indexOf("?") > 0 ? "&" : "?");
        builder.append("businessId=" + businessKey.getId());
        builder.append("&businessLogId=" + businessKey.getLogId());
        builder.append("&modelKey=" + modelKey);
        builder.append("&taskId=");
        try {
            builder.append("&taskName=" + URLEncoder.encode(task.getName(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        builder.append("&type=historic");

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("formPath", builder.toString());
        map.put("processDefinitionName", historicProcessInstance.getName());
        return ResultJson.ok(map);
    }

    @Override
    public ListJson findHistoricTrackInfo(String processInstanceId) {
        List<Map<String, Object>> list = historicTaskDao.findHistoricTrackInfo(processInstanceId);
        parseTime(list);
        return new ListJson(list);
    }

    @Override
    public ResultJson findHistoricTaskInfo(String processInstanceId) {
        List<Map<String, Object>> list = historicTaskDao.findHistoricTrackInfo(processInstanceId);
        parseTime(list);
        Map<String, Object> historicProcessInstance = historicTaskDao.getHistoricProcessInstanceInfo(processInstanceId);

        boolean isEnd = historicProcessInstance.get("END_TIME") != null;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("list", list);
        map.put("isEnd", isEnd);
        map.put("processDefinitionId", historicProcessInstance.get("PROC_DEF_ID"));
        return ResultJson.ok(map);
    }

    /**
     * 解析里面的时间
     *
     * @param list
     */
    private void parseTime(List<Map<String, Object>> list) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Map<String, Object> map : list) {
            map.put("approvalDate", dateFormat.format(map.get("approvalDate")));
        }
    }
}
