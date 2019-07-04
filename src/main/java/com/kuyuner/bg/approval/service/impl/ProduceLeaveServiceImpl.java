package com.kuyuner.bg.approval.service.impl;

import com.kuyuner.bg.approval.bean.LeaveHistoricListView;
import com.kuyuner.bg.approval.bean.LeavePendingListView;
import com.kuyuner.bg.approval.dao.LeaveDao;
import com.kuyuner.bg.approval.dao.LeaveRecordDao;
import com.kuyuner.bg.approval.entity.Leave;
import com.kuyuner.bg.approval.entity.LeaveRecord;
import com.kuyuner.bg.approval.service.LeaveService;
import com.kuyuner.bg.approval.service.ProduceService;
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
 * 请假Service层实现
 *
 * @author administrator
 */
@Service
public class ProduceLeaveServiceImpl implements ProduceService {

    @Autowired
    private LeaveDao leaveDao;

    @Override
    public PageJson findPendingList(String pageNum, String pageSize, Leave leave,String userId) {
        Page<LeavePendingListView> page = new Page<>(pageNum, pageSize);
        page.start();
        leaveDao.findPendingList(leave,  UserUtils.getPrincipal()==null?userId:UserUtils.getPrincipal().getId());
        page.end();
        return new PageJson(page);
    }

    @Override
    public PageJson findHistoricList(String pageNum, String pageSize, Leave leave,String userId) {
        Page<LeaveHistoricListView> page = new Page<>(pageNum, pageSize);
        page.start();
        leaveDao.findHistoricList(leave,  UserUtils.getPrincipal()==null?userId:UserUtils.getPrincipal().getId());
        page.end();
        return new PageJson(page);
    }

    @Override
    public PageJson findSendList(String pageNum, String pageSize, Leave leave,String userId) {
        Page<LeavePendingListView> page = new Page<>(pageNum, pageSize);
        page.start();
        leaveDao.findSendList(leave,  UserUtils.getPrincipal()==null?userId:UserUtils.getPrincipal().getId());
        page.end();
        return new PageJson(page);
    }

    @Override
    public String getCode() {
        return null;
    }

}