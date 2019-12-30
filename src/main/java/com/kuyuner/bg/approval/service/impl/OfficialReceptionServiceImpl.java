package com.kuyuner.bg.approval.service.impl;

import com.kuyuner.bg.approval.bean.OfficialReceptionHistoricListView;
import com.kuyuner.bg.approval.bean.OfficialReceptionPendingListView;
import com.kuyuner.bg.approval.dao.OfficialReceptionDao;
import com.kuyuner.bg.approval.entity.OfficialReception;
import com.kuyuner.bg.approval.service.OfficialReceptionService;
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
 * 公务接待申请Service层实现
 *
 * @author administrator
 */
@Service
public class OfficialReceptionServiceImpl implements OfficialReceptionService {

    @Autowired
    private OfficialReceptionDao officialReceptionDao;
    @Autowired
    private WorkFlowService workFlowService;

    /**
     * 查询唯一
     *
     * @param id
     * @return
     */
    @Override
    public OfficialReception get(String id) {
        return officialReceptionDao.get(new OfficialReception(id));
    }

    /**
     * 删除数据
     *
     * @param ids
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultJson deletes(String... ids) {
        officialReceptionDao.deletes(ids);
        return ResultJson.ok();
    }

    /**
     * 获得历史数据
     *
     * @param businessLogId
     * @return
     */
    @Override
    public OfficialReception getLog(String businessLogId) {
        OfficialReception officialReception = new OfficialReception();
        officialReception.setId(businessLogId);
        return officialReceptionDao.getLog(officialReception);
    }

    /**
     * 查询待办事项
     *
     * @param pageNum
     * @param pageSize
     * @param officialReception
     * @return
     */
    @Override
    public PageJson findPendingList(String pageNum, String pageSize, OfficialReception officialReception) {
        Page<OfficialReceptionPendingListView> page = new Page<>(pageNum, pageSize);
        page.start();
        officialReceptionDao.findPendingList(officialReception, UserUtils.getPrincipal().getId());
        page.end();
        return new PageJson(page);
    }

    /**
     * 查询历史审批
     *
     * @param pageNum
     * @param pageSize
     * @param officialReception
     * @return
     */
    @Override
    public PageJson findHistoricList(String pageNum, String pageSize, OfficialReception officialReception) {
        Page<OfficialReceptionHistoricListView> page = new Page<>(pageNum, pageSize);
        page.start();
        officialReceptionDao.findHistoricList(officialReception, UserUtils.getPrincipal().getId());
        page.end();
        return new PageJson(page);
    }

    /**
     * 查询申请历史
     *
     * @param pageNum
     * @param pageSize
     * @param officialReception
     * @return
     */
    @Override
    public PageJson findSendList(String pageNum, String pageSize, OfficialReception officialReception) {
        Page<OfficialReceptionPendingListView> page = new Page<>(pageNum, pageSize);
        page.start();
        officialReceptionDao.findSendList(officialReception, UserUtils.getPrincipal().getId());
        page.end();
        return new PageJson(page);
    }

    /**
     * 提交申请
     *
     * @param officialReception
     * @param taskResult
     * @param userId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultJson submitForm(OfficialReception officialReception, String taskResult, String userId) {
        //保存业务数据
        if (StringUtils.isBlank(officialReception.getId())) {
            officialReception.setId(IdGenerate.uuid());
            officialReception.setSenderId(UserUtils.getPrincipal().getId());
            officialReception.setCreater(UserUtils.getPrincipal().getId());
            officialReceptionDao.insert(officialReception);
        } else {
            officialReceptionDao.update(officialReception);
            officialReception = officialReceptionDao.get(new OfficialReception(officialReception.getId()));
        }
        _submitForm(officialReception, taskResult, userId);

        return ResultJson.ok();
    }

    /**
     * 审批操作
     *
     * @param officialReception
     * @param approvalResult
     * @param taskResult
     * @param userId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultJson approvalForm(OfficialReception officialReception, String approvalResult, String taskResult, String userId) {
        String approvalResultContent = officialReceptionDao.getApprovalResult(officialReception.getId());
        TaskBean taskBean = JsonMapper.fromJsonString(taskResult, TaskBean.class);
        String str = "<span class=\"people_name\">%s</span>的处理意见：</br>%s；<span class=\"audit_result\">%s</span></br>";
        officialReception.setApprovalResult((approvalResultContent == null ? "" : approvalResultContent) + String.format(str, UserUtils.getPrincipal().getName(), approvalResult, taskBean.getSequenceFlowName()));
        officialReceptionDao.update(officialReception);
        officialReception = officialReceptionDao.get(new OfficialReception(officialReception.getId()));
        submitForm(officialReception, taskResult, userId);

        return ResultJson.ok();
    }

    @Override
    public void _submitForm(OfficialReception officialReception, String taskResult, String userId) {
        //记录日志
        OfficialReception officialReceptionLog = new OfficialReception();
        BeanUtils.copyProperties(officialReception, officialReceptionLog);
        officialReceptionLog.setId(IdGenerate.uuid());
        officialReceptionDao.insertLog(officialReceptionLog);
        //提交流程
        BusinessKey businessKey = new BusinessKey();
        businessKey.setId(officialReception.getId());
        businessKey.setLogId(officialReceptionLog.getId());
        TaskInfo taskInfo = workFlowService.submitTask(taskResult, businessKey, userId);
        //更新业务表的当前环节字段
        OfficialReception officialReception1 = new OfficialReception();
        officialReception1.setId(officialReception.getId());
        officialReception1.setTaskName(taskInfo.getTaskName());
        officialReceptionDao.update(officialReception1);
    }
}
