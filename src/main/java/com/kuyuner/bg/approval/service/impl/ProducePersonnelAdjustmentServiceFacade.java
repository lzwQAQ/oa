package com.kuyuner.bg.approval.service.impl;

import com.kuyuner.bg.approval.bean.AdjustmentHistoricListView;
import com.kuyuner.bg.approval.bean.AdjustmentPendingListView;
import com.kuyuner.bg.approval.dao.PersonnelAdjustmentDao;
import com.kuyuner.bg.approval.entity.PersonnelAdjustment;
import com.kuyuner.bg.approval.service.PersonnelAdjustmentService;
import com.kuyuner.bg.approval.service.ProduceFaced;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.common.idgen.IdGenerate;
import com.kuyuner.common.lang.StringUtils;
import com.kuyuner.common.mapper.JsonMapper;
import com.kuyuner.common.persistence.Page;
import com.kuyuner.common.utils.GfJsonUtil;
import com.kuyuner.core.sys.security.UserUtils;
import com.kuyuner.workflow.api.bean.BusinessKey;
import com.kuyuner.workflow.api.bean.TaskBean;
import com.kuyuner.workflow.api.bean.TaskInfo;
import com.kuyuner.workflow.api.service.WorkFlowService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 人事调度Service层实现
 *
 * @author administrator
 */
@Component
public class ProducePersonnelAdjustmentServiceFacade implements ProduceFaced {

    @Autowired
    private PersonnelAdjustmentDao personnelAdjustmentDao;

    @Autowired
    private PersonnelAdjustmentService personnelAdjustmentService;

    @Override
    public PageJson findPendingList(String pageNum, String pageSize, String senderName,String businessName,String leaveType, String userId) {
        Page<AdjustmentPendingListView> page = new Page<>(pageNum, pageSize);
        page.start();
        PersonnelAdjustment personnelAdjustment = new PersonnelAdjustment();
        personnelAdjustment.setSenderName(senderName);
        personnelAdjustmentDao.findPendingList(personnelAdjustment, UserUtils.getPrincipal()==null?userId:UserUtils.getPrincipal().getId());
        page.end();
        return new PageJson(page);
    }

    @Override
    public PageJson findHistoricList(String pageNum, String pageSize, String senderName,String businessName,String leaveType, String userId) {
        Page<AdjustmentHistoricListView> page = new Page<>(pageNum, pageSize);
        page.start();
        PersonnelAdjustment personnelAdjustment = new PersonnelAdjustment();
        personnelAdjustment.setSenderName(senderName);
        personnelAdjustmentDao.findHistoricList(personnelAdjustment, UserUtils.getPrincipal()==null?userId:UserUtils.getPrincipal().getId());
        page.end();
        return new PageJson(page);
    }

    @Override
    public PageJson findSendList(String pageNum, String pageSize, String senderName,String businessName,String leaveType, String userId) {
        Page<AdjustmentHistoricListView> page = new Page<>(pageNum, pageSize);
        page.start();
        PersonnelAdjustment personnelAdjustment = new PersonnelAdjustment();
        personnelAdjustment.setSenderName(senderName);
        personnelAdjustmentDao.findSendList(personnelAdjustment,UserUtils.getPrincipal()==null?userId:UserUtils.getPrincipal().getId());
        page.end();
        return new PageJson(page);
    }

    @Override
    public String getCode() {
        return "personnel";
    }

    @Override
    public ResultJson submit(String body, String taskResult, String userId,String goods, String modelKey) {
        if(UserUtils.getPrincipal()==null && StringUtils.isBlank(userId)){
            return ResultJson.failed("请传入用户信息");
        }
        PersonnelAdjustment personnelAdjustment = GfJsonUtil.parseObject(body,PersonnelAdjustment.class);
        //保存业务数据
        if (StringUtils.isBlank(personnelAdjustment.getId())) {
            personnelAdjustment.setId(IdGenerate.uuid());
            personnelAdjustment.setSenderId(UserUtils.getPrincipal() == null ? userId : UserUtils.getPrincipal().getId());
            personnelAdjustment.setCreater(UserUtils.getPrincipal() == null ? userId : UserUtils.getPrincipal().getId());
            personnelAdjustmentDao.insert(personnelAdjustment);
        } else {
            personnelAdjustmentDao.update(personnelAdjustment);
            personnelAdjustment = personnelAdjustmentDao.get(new PersonnelAdjustment(personnelAdjustment.getId()));
        }
        personnelAdjustmentService.handleForm(personnelAdjustment, taskResult,userId);
        return ResultJson.ok();
    }

    @Override
    public ResultJson approvalForm(String id, String approvalResult, String taskResult, String userId) {
        PersonnelAdjustment personnelAdjustment = new PersonnelAdjustment();
        personnelAdjustment.setId(id);
        String approvalResultContent = personnelAdjustmentDao.getApprovalResult(personnelAdjustment.getId());
        TaskBean taskBean = JsonMapper.fromJsonString(taskResult, TaskBean.class);
        String str = "<span class=\"people_name\">%s</span>的处理意见：</br>%s；<span class=\"audit_result\">%s</span></br>";
        personnelAdjustment.setApprovalResult((approvalResultContent == null ? "" : approvalResultContent) + String.format(str, UserUtils.getPrincipal().getName(), approvalResult, taskBean.getSequenceFlowName()));
        personnelAdjustmentDao.update(personnelAdjustment);

        personnelAdjustment = personnelAdjustmentDao.get(new PersonnelAdjustment(personnelAdjustment.getId()));
        personnelAdjustmentService.handleForm(personnelAdjustment, taskResult,userId);


        return ResultJson.ok();
    }

    @Override
    public ResultJson detail(String produceType, String businessId, String firstTask, String type, String taskId,
                             String modelKey, String startSequenceFlowName, String userId) {
        return null;
    }

}