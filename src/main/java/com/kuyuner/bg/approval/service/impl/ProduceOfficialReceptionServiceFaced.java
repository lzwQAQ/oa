package com.kuyuner.bg.approval.service.impl;

import com.kuyuner.bg.approval.bean.OfficialReceptionHistoricListView;
import com.kuyuner.bg.approval.bean.OfficialReceptionPendingListView;
import com.kuyuner.bg.approval.dao.OfficialReceptionDao;
import com.kuyuner.bg.approval.entity.OfficialReception;
import com.kuyuner.bg.approval.service.OfficialReceptionService;
import com.kuyuner.bg.approval.service.ProduceFaced;
import com.kuyuner.bg.msg.util.HtmlRegexpUtil;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.common.idgen.IdGenerate;
import com.kuyuner.common.lang.StringUtils;
import com.kuyuner.common.mapper.JsonMapper;
import com.kuyuner.common.persistence.Page;
import com.kuyuner.common.utils.GfJsonUtil;
import com.kuyuner.core.sys.entity.User;
import com.kuyuner.core.sys.security.UserUtils;
import com.kuyuner.core.sys.service.UserService;
import com.kuyuner.workflow.api.bean.TaskBean;
import com.kuyuner.workflow.runtime.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 公务接待申请Service层实现
 *
 * @author administrator
 */
@Component
public class ProduceOfficialReceptionServiceFaced implements ProduceFaced {

    @Autowired
    private OfficialReceptionDao officialReceptionDao;
    @Autowired
    private OfficialReceptionService officialReceptionService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private UserService userService;

    @Override
    public PageJson findPendingList(String pageNum, String pageSize, String senderName, String businessName, String leaveType, String userId) {
        Page<OfficialReceptionPendingListView> page = new Page<>(pageNum, pageSize);
        page.start();
        OfficialReception officialReception = new OfficialReception();
        officialReception.setSenderName(senderName);
        officialReceptionDao.findPendingList(officialReception, UserUtils.getPrincipal() == null ? userId : UserUtils.getPrincipal().getId());
        page.end();
        return new PageJson(page);
    }

    @Override
    public PageJson findHistoricList(String pageNum, String pageSize, String senderName, String businessName, String leaveType, String userId) {
        Page<OfficialReceptionHistoricListView> page = new Page<>(pageNum, pageSize);
        page.start();
        OfficialReception officialReception = new OfficialReception();
        officialReception.setSenderName(senderName);
        officialReceptionDao.findHistoricList(officialReception, UserUtils.getPrincipal() == null ? userId : UserUtils.getPrincipal().getId());
        page.end();
        return new PageJson(page);
    }

    @Override
    public PageJson findSendList(String pageNum, String pageSize, String senderName, String businessName, String leaveType, String userId) {
        Page<OfficialReceptionPendingListView> page = new Page<>(pageNum, pageSize);
        page.start();
        OfficialReception officialReception = new OfficialReception();
        officialReception.setSenderName(senderName);
        officialReceptionDao.findSendList(officialReception, UserUtils.getPrincipal() == null ? userId : UserUtils.getPrincipal().getId());
        page.end();
        return new PageJson(page);
    }

    @Override
    public String getCode() {
        return "officialReception";
    }

    @Override
    public ResultJson submit(String body, String taskResult, String userId, String goods, String modelKey) {
        if (UserUtils.getPrincipal() == null && StringUtils.isBlank(userId)) {
            return ResultJson.failed("请传入用户信息");
        }
        OfficialReception officialReception = GfJsonUtil.parseObject(body, OfficialReception.class);
        //保存业务数据
        if (StringUtils.isBlank(officialReception.getId())) {
            officialReception.setId(IdGenerate.uuid());
            officialReception.setSenderId(UserUtils.getPrincipal() == null ? userId : UserUtils.getPrincipal().getId());
            officialReception.setCreater(UserUtils.getPrincipal() == null ? userId : UserUtils.getPrincipal().getId());
            officialReceptionDao.insert(officialReception);
        } else {
            officialReceptionDao.update(officialReception);
            officialReception = officialReceptionDao.get(new OfficialReception(officialReception.getId()));
        }
        officialReceptionService._submitForm(officialReception, taskResult, userId);

        return ResultJson.ok();
    }

    @Override
    public ResultJson approvalForm(String id, String approvalResult, String taskResult, String userId) {
        OfficialReception officialReception = new OfficialReception();
        officialReception.setId(id);
        String approvalResultContent = officialReceptionDao.getApprovalResult(officialReception.getId());
        TaskBean taskBean = JsonMapper.fromJsonString(taskResult, TaskBean.class);
        String str = "<span class=\"people_name\">%s</span>的处理意见：</br>%s；<span class=\"audit_result\">%s</span></br>";
        User user = userService.get(userId);
        officialReception.setApprovalResult((approvalResultContent == null ? "" : approvalResultContent) + String.format(str, user.getName(), approvalResult, taskBean.getSequenceFlowName()));
        officialReceptionDao.update(officialReception);
        officialReception = officialReceptionDao.get(new OfficialReception(officialReception.getId()));
        officialReceptionService.submitForm(officialReception, taskResult, userId);

        return ResultJson.ok();
    }

    @Override
    public ResultJson detail(String produceType, String businessId, String firstTask, String type, String taskId,
                             String modelKey, String startSequenceFlowName, String userId) {
        Map map = new HashMap();
        ResultJson result = taskService.getFormPath(modelKey, startSequenceFlowName, taskId);
        map.put("work", result.getData());
        if (StringUtils.isBlank(businessId) || "null".equals(businessId)) {
            return ResultJson.ok(map);
        }
        if ("historic".equals(type)) {
            map.put("data", officialReceptionService.getLog(businessId));
        } else {
            if (StringUtils.isNotBlank(businessId)) {
                OfficialReception officialReception = officialReceptionService.get(businessId);
                officialReception.setApprovalResult(HtmlRegexpUtil.filterHtml(officialReception.getApprovalResult()));
                map.put("data", officialReception);
            } else {
                User user = userService.get(UserUtils.getPrincipal() == null ? userId : UserUtils.getPrincipal().getId());
                OfficialReception officialReception = new OfficialReception();
                officialReception.setSenderName(user.getName());
                officialReception.setSenderDeptName(user.getDept() != null ? user.getDept().getName() : "");
                map.put("data", officialReception);
            }
        }
        return ResultJson.ok(map);
    }

}