package com.kuyuner.bg.approval.service.impl;

import com.kuyuner.bg.approval.bean.AdjustmentHistoricListView;
import com.kuyuner.bg.approval.bean.AdjustmentPendingListView;
import com.kuyuner.bg.approval.dao.PersonnelAdjustmentDao;
import com.kuyuner.bg.approval.entity.PersonnelAdjustment;
import com.kuyuner.bg.approval.service.PersonnelAdjustmentService;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.common.idgen.IdGenerate;
import com.kuyuner.common.lang.StringUtils;
import com.kuyuner.common.mapper.JsonMapper;
import com.kuyuner.common.persistence.Page;
import com.kuyuner.core.sys.security.UserUtils;
import com.kuyuner.workflow.api.bean.BusinessKey;
import com.kuyuner.workflow.api.bean.TaskBean;
import com.kuyuner.workflow.api.bean.TaskInfo;
import com.kuyuner.workflow.api.service.WorkFlowService;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 人事调度Service层实现
 *
 * @author administrator
 */
@Service
public class PersonnelAdjustmentServiceImpl implements PersonnelAdjustmentService {

    @Autowired
    private PersonnelAdjustmentDao personnelAdjustmentDao;

    @Autowired
    private WorkFlowService workFlowService;

    @Override
    public PersonnelAdjustment get(String id) {
        return personnelAdjustmentDao.get(new PersonnelAdjustment(id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultJson deletes(String... ids) {
        personnelAdjustmentDao.deletes(ids);
        return ResultJson.ok();
    }

    @Override
    public PersonnelAdjustment getLog(String logId) {
        PersonnelAdjustment personnelAdjustmentLog = new PersonnelAdjustment();
        personnelAdjustmentLog.setId(logId);
        return personnelAdjustmentDao.getLog(personnelAdjustmentLog);
    }

    @Override
    public PageJson findPendingList(String pageNum, String pageSize, PersonnelAdjustment personnelAdjustment) {
        Page<AdjustmentPendingListView> page = new Page<>(pageNum, pageSize);
        page.start();
        personnelAdjustmentDao.findPendingList(personnelAdjustment, UserUtils.getPrincipal().getId());
        page.end();
        return new PageJson(page);
    }

    @Override
    public PageJson findHistoricList(String pageNum, String pageSize, PersonnelAdjustment personnelAdjustment) {
        Page<AdjustmentHistoricListView> page = new Page<>(pageNum, pageSize);
        page.start();
        personnelAdjustmentDao.findHistoricList(personnelAdjustment, UserUtils.getPrincipal().getId());
        page.end();
        return new PageJson(page);
    }

    @Override
    public PageJson findSendList(String pageNum, String pageSize, PersonnelAdjustment personnelAdjustment) {
        Page<AdjustmentHistoricListView> page = new Page<>(pageNum, pageSize);
        page.start();
        personnelAdjustmentDao.findSendList(personnelAdjustment, UserUtils.getPrincipal().getId());
        page.end();
        return new PageJson(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultJson submitForm(PersonnelAdjustment personnelAdjustment, String taskResult) {
        //保存业务数据
        if (StringUtils.isBlank(personnelAdjustment.getId())) {
            personnelAdjustment.setId(IdGenerate.uuid());
            personnelAdjustment.setSenderId(UserUtils.getPrincipal().getId());
            personnelAdjustment.setCreater(UserUtils.getPrincipal().getId());
            personnelAdjustmentDao.insert(personnelAdjustment);
        } else {
            personnelAdjustmentDao.update(personnelAdjustment);
            personnelAdjustment = personnelAdjustmentDao.get(new PersonnelAdjustment(personnelAdjustment.getId()));
        }

        handleForm(personnelAdjustment, taskResult);

        return ResultJson.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultJson approvalForm(String id, String approvalResult, String taskResult) {
        PersonnelAdjustment personnelAdjustment = new PersonnelAdjustment();
        personnelAdjustment.setId(id);
        String approvalResultContent = personnelAdjustmentDao.getApprovalResult(personnelAdjustment.getId());
        TaskBean taskBean = JsonMapper.fromJsonString(taskResult, TaskBean.class);
        String str = "<span class=\"people_name\">%s</span>的处理意见：</br>%s；<span class=\"audit_result\">%s</span></br>";
        personnelAdjustment.setApprovalResult((approvalResultContent == null ? "" : approvalResultContent) + String.format(str, UserUtils.getPrincipal().getName(), approvalResult, taskBean.getSequenceFlowName()));
        personnelAdjustmentDao.update(personnelAdjustment);

        personnelAdjustment = personnelAdjustmentDao.get(new PersonnelAdjustment(personnelAdjustment.getId()));
        handleForm(personnelAdjustment, taskResult);


        return ResultJson.ok();
    }

    /**
     * 提交任务
     *
     * @param personnelAdjustment
     * @param taskResult
     */
    private void handleForm(PersonnelAdjustment personnelAdjustment, String taskResult) {
        //记录日志
        PersonnelAdjustment adjustmentLog = new PersonnelAdjustment();
        BeanUtils.copyProperties(personnelAdjustment, adjustmentLog);
        adjustmentLog.setId(IdGenerate.uuid());
        personnelAdjustmentDao.insertLog(adjustmentLog);

        //提交流程
        BusinessKey businessKey = new BusinessKey();
        businessKey.setId(personnelAdjustment.getId());
        businessKey.setLogId(adjustmentLog.getId());
        TaskInfo taskInfo = workFlowService.submitTask(taskResult, businessKey);

        //更新业务表的当前环节字段
        PersonnelAdjustment adjustment = new PersonnelAdjustment();
        adjustment.setId(personnelAdjustment.getId());
        adjustment.setTaskName(taskInfo.getTaskName());
        personnelAdjustmentDao.update(adjustment);
    }
}