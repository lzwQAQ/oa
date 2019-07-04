package com.kuyuner.bg.approval.service.impl;

import com.kuyuner.bg.approval.bean.CarApplyHistoricListView;
import com.kuyuner.bg.approval.bean.CarApplyPendingListView;
import com.kuyuner.bg.approval.dao.CarApplyDao;
import com.kuyuner.bg.approval.entity.CarApply;
import com.kuyuner.bg.approval.entity.Driver;
import com.kuyuner.bg.approval.service.CarApplyService;
import com.kuyuner.bg.approval.service.ProduceFaced;
import com.kuyuner.common.controller.ListJson;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.common.idgen.IdGenerate;
import com.kuyuner.common.lang.StringUtils;
import com.kuyuner.common.mapper.JsonMapper;
import com.kuyuner.common.persistence.Page;
import com.kuyuner.core.sys.entity.User;
import com.kuyuner.core.sys.security.UserUtils;
import com.kuyuner.workflow.api.bean.BusinessKey;
import com.kuyuner.workflow.api.bean.TaskBean;
import com.kuyuner.workflow.api.bean.TaskInfo;
import com.kuyuner.workflow.api.service.WorkFlowService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 车辆申请Service层实现
 *
 * @author administrator
 */
@Service
public class ProduceCarApplyServiceFaced implements ProduceFaced {

    @Autowired
    private CarApplyDao carApplyDao;

    @Override
    public PageJson findPendingList(String pageNum, String pageSize, String senderName, String businessName, String leaveType, String userId) {
        Page<CarApplyPendingListView> page = new Page<>(pageNum, pageSize);
        page.start();
        CarApply carApply = new CarApply();
        carApply.setSenderName(senderName);
        carApply.getCar().setCarNo(businessName);
        carApplyDao.findPendingList(carApply, UserUtils.getPrincipal() == null?userId:UserUtils.getPrincipal().getId());
        page.end();
        return new PageJson(page);
    }

    @Override
    public PageJson findHistoricList(String pageNum, String pageSize, String senderName, String businessName, String leaveType, String userId) {
        Page<CarApplyHistoricListView> page = new Page<>(pageNum, pageSize);
        page.start();
        CarApply carApply = new CarApply();
        carApply.setSenderName(senderName);
        carApply.getCar().setCarNo(businessName);
        carApplyDao.findHistoricList(carApply, UserUtils.getPrincipal() == null?userId:UserUtils.getPrincipal().getId());
        page.end();
        return new PageJson(page);
    }

    @Override
    public PageJson findSendList(String pageNum, String pageSize, String senderName, String businessName, String leaveType, String userId) {
        Page<CarApplyPendingListView> page = new Page<>(pageNum, pageSize);
        page.start();
        CarApply carApply = new CarApply();
        carApply.setSenderName(senderName);
        carApply.getCar().setCarNo(businessName);
        carApplyDao.findSendList(carApply,UserUtils.getPrincipal() == null?userId:UserUtils.getPrincipal().getId());
        page.end();
        return new PageJson(page);
    }

    @Override
    public String getCode() {
        return "carApply";
    }

}