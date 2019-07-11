package com.kuyuner.bg.approval.service.impl;

import com.kuyuner.bg.approval.bean.CarApplyHistoricListView;
import com.kuyuner.bg.approval.bean.CarApplyPendingListView;
import com.kuyuner.bg.approval.dao.CarApplyDao;
import com.kuyuner.bg.approval.entity.Car;
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
import com.kuyuner.common.utils.GfJsonUtil;
import com.kuyuner.core.sys.entity.User;
import com.kuyuner.core.sys.security.UserUtils;
import com.kuyuner.core.sys.service.UserService;
import com.kuyuner.workflow.api.bean.BusinessKey;
import com.kuyuner.workflow.api.bean.TaskBean;
import com.kuyuner.workflow.api.bean.TaskInfo;
import com.kuyuner.workflow.api.service.WorkFlowService;
import com.kuyuner.workflow.runtime.service.PendingTaskService;
import com.kuyuner.workflow.runtime.service.TaskService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 车辆申请Service层实现
 *
 * @author administrator
 */
@Component
public class ProduceCarApplyServiceFaced implements ProduceFaced {

    @Autowired
    private CarApplyDao carApplyDao;
    @Autowired
    private CarApplyService carApplyService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private UserService userService;

    @Override
    public PageJson findPendingList(String pageNum, String pageSize, String senderName, String businessName, String leaveType, String userId) {
        Page<CarApplyPendingListView> page = new Page<>(pageNum, pageSize);
        page.start();
        CarApply carApply = new CarApply();
        carApply.setSenderName(senderName);
        Car car = new Car();
        car.setCarNo(businessName);
        carApply.setCar(car);
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
        Car car = new Car();
        car.setCarNo(businessName);
        carApply.setCar(car);
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
        Car car = new Car();
        car.setCarNo(businessName);
        carApply.setCar(car);;
        carApplyDao.findSendList(carApply,UserUtils.getPrincipal() == null?userId:UserUtils.getPrincipal().getId());
        page.end();
        return new PageJson(page);
    }

    @Override
    public String getCode() {
        return "carApply";
    }

    @Override
    public ResultJson submit(String body, String taskResult, String userId,String goods, String modelKey) {
        if(UserUtils.getPrincipal()==null && StringUtils.isBlank(userId)){
            return ResultJson.failed("请传入用户信息");
        }
        CarApply carApply = GfJsonUtil.parseObject(body,CarApply.class);
        //保存业务数据
        if (StringUtils.isBlank(carApply.getId())) {
            carApply.setId(IdGenerate.uuid());
            carApply.setSenderId(UserUtils.getPrincipal() == null ? userId : UserUtils.getPrincipal().getId());
            carApply.setCreater(UserUtils.getPrincipal().getId());
            carApplyDao.insert(carApply);
        } else {
            carApplyDao.update(carApply);
            carApply = carApplyDao.get(new CarApply(carApply.getId()));
        }


        carApplyService._submitForm(carApply, taskResult,userId);

        return ResultJson.ok();
    }

    @Override
    public ResultJson approvalForm(String id, String approvalResult, String taskResult, String userId) {
        CarApply carApply = new CarApply();
        carApply.setId(id);
        String approvalResultContent = carApplyDao.getApprovalResult(carApply.getId());
        TaskBean taskBean = JsonMapper.fromJsonString(taskResult, TaskBean.class);
        String str = "<span class=\"people_name\">%s</span>的处理意见：</br>%s；<span class=\"audit_result\">%s</span></br>";
        User user = userService.get(userId);
        carApply.setApprovalResult((approvalResultContent == null ? "" : approvalResultContent) + String.format(str, user.getName(), approvalResult, taskBean.getSequenceFlowName()));
        carApplyDao.update(carApply);

        carApply = carApplyDao.get(new CarApply(carApply.getId()));
        carApplyService.submitForm(carApply, taskResult,userId);

        return ResultJson.ok();
    }

    @Override
    public ResultJson detail(String produceType, String businessId, String firstTask, String type, String taskId,
                             String modelKey, String startSequenceFlowName, String userId) {
        Map map = new HashMap();
        ResultJson result = taskService.getFormPath(modelKey,startSequenceFlowName,taskId);
        map.put("work",result.getData());
        if ("historic".equals(type)) {
            map.put("data", carApplyService.getLog(businessId));
        } else {
            if (StringUtils.isNotBlank(businessId)) {
                map.put("data", carApplyService.get(businessId));
            } else {
                User user = userService.get(UserUtils.getPrincipal() == null ? userId : UserUtils.getPrincipal().getId());
                CarApply carApply = new CarApply();
                carApply.setSenderName(user.getName());
                carApply.setSenderDeptName(user.getDept() != null ? user.getDept().getName() : "");
                map.put("data", carApply);
            }
        }
        return ResultJson.ok(map);
    }

}