package com.kuyuner.bg.approval.service.impl;

import com.kuyuner.bg.approval.bean.CarApplyHistoricListView;
import com.kuyuner.bg.approval.bean.CarApplyPendingListView;
import com.kuyuner.bg.approval.dao.CarApplyDao;
import com.kuyuner.bg.approval.entity.CarApply;
import com.kuyuner.bg.approval.entity.Driver;
import com.kuyuner.bg.approval.service.CarApplyService;
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
public class CarApplyServiceImpl implements CarApplyService {

    @Autowired
    private CarApplyDao carApplyDao;

    @Autowired
    private WorkFlowService workFlowService;

    @Override
    public CarApply get(String id) {
        return carApplyDao.get(new CarApply(id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultJson deletes(String... ids) {
        carApplyDao.deletes(ids);
        return ResultJson.ok();
    }

    @Override
    public CarApply getLog(String businessLogId) {
        CarApply carApplyLog = new CarApply();
        carApplyLog.setId(businessLogId);
        return carApplyDao.getLog(carApplyLog);
    }

    @Override
    public PageJson findPendingList(String pageNum, String pageSize, CarApply carApply) {
        Page<CarApplyPendingListView> page = new Page<>(pageNum, pageSize);
        page.start();
        carApplyDao.findPendingList(carApply, UserUtils.getPrincipal().getId());
        page.end();
        return new PageJson(page);
    }

    @Override
    public PageJson findHistoricList(String pageNum, String pageSize, CarApply carApply) {
        Page<CarApplyHistoricListView> page = new Page<>(pageNum, pageSize);
        page.start();
        carApplyDao.findHistoricList(carApply, UserUtils.getPrincipal().getId());
        page.end();
        return new PageJson(page);
    }

    @Override
    public PageJson findSendList(String pageNum, String pageSize, CarApply carApply) {
        Page<CarApplyPendingListView> page = new Page<>(pageNum, pageSize);
        page.start();
        carApplyDao.findSendList(carApply, UserUtils.getPrincipal().getId());
        page.end();
        return new PageJson(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultJson submitForm(CarApply carApply, String taskResult,String userId) {
        //保存业务数据
        if (StringUtils.isBlank(carApply.getId())) {
            carApply.setId(IdGenerate.uuid());
            carApply.setSenderId(UserUtils.getPrincipal().getId());
            carApply.setCreater(UserUtils.getPrincipal().getId());
            carApplyDao.insert(carApply);
        } else {
            carApplyDao.update(carApply);
            carApply = carApplyDao.get(new CarApply(carApply.getId()));
        }


        _submitForm(carApply, taskResult,userId);

        return ResultJson.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultJson approvalForm(String id, String approvalResult, String taskResult,String userId) {
        CarApply carApply = new CarApply();
        carApply.setId(id);
        String approvalResultContent = carApplyDao.getApprovalResult(carApply.getId());
        TaskBean taskBean = JsonMapper.fromJsonString(taskResult, TaskBean.class);
        String str = "<span class=\"people_name\">%s</span>的处理意见：</br>%s；<span class=\"audit_result\">%s</span></br>";
        carApply.setApprovalResult((approvalResultContent == null ? "" : approvalResultContent) + String.format(str, UserUtils.getPrincipal().getName(), approvalResult, taskBean.getSequenceFlowName()));
        carApplyDao.update(carApply);

        carApply = carApplyDao.get(new CarApply(carApply.getId()));
        submitForm(carApply, taskResult,userId);

        return ResultJson.ok();
    }

    @Override
    public ListJson findDrivers() {
        List<Driver> list = carApplyDao.findDrivers();
        Driver driver = new Driver();
        User user = new User();
        BeanUtils.copyProperties(UserUtils.getUser(), user);
        user.setName("自驾");
        driver.setUser(user);
        list.add(0, driver);
        return new ListJson(list);
    }

    @Override
    public ListJson findCars() {
        return new ListJson(carApplyDao.findCars());
    }

    /**
     * 提交任务
     *
     * @param carApply
     * @param taskResult
     */
    @Override
    public void _submitForm(CarApply carApply, String taskResult, String userId) {
        //记录日志
        CarApply carApplyLog = new CarApply();
        BeanUtils.copyProperties(carApply, carApplyLog);
        carApplyLog.setId(IdGenerate.uuid());
        carApplyDao.insertLog(carApplyLog);

        //提交流程
        BusinessKey businessKey = new BusinessKey();
        businessKey.setId(carApply.getId());
        businessKey.setLogId(carApplyLog.getId());
        TaskInfo taskInfo = workFlowService.submitTask(taskResult, businessKey,userId);

        //更新业务表的当前环节字段
        CarApply carApply2 = new CarApply();
        carApply2.setId(carApply.getId());
        carApply2.setTaskName(taskInfo.getTaskName());
        carApplyDao.update(carApply2);
    }
}