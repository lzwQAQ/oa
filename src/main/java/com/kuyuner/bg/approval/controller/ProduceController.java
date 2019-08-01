package com.kuyuner.bg.approval.controller;

import com.kuyuner.bg.approval.lib.ProduceFacadeFactory;
import com.kuyuner.bg.approval.service.ProduceFaced;
import com.kuyuner.common.controller.BaseController;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.common.lang.StringUtils;
import com.kuyuner.common.mapper.JsonMapper;
import com.kuyuner.common.reflect.ReflectUtils;
import com.kuyuner.common.utils.GfJsonUtil;
import com.kuyuner.core.sys.security.UserUtils;
import com.kuyuner.core.sys.service.UserService;
import com.kuyuner.workflow.api.bean.TaskBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 业务申请Service层接口
 *
 * @author administrator
 */
@Controller
@RequestMapping("${kuyuner.admin-path}/procedure")
public class ProduceController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(ReflectUtils.class);
    @Autowired
    private ProduceFacadeFactory produceFacadeFactory;
    @Autowired
    private UserService userService;

    /**
     * 查询1.请假申请 2.业务申请 3.人事调度 4.采购申请 5.用车申请 6.财务申请
     *
     * @param produceType 1.请假申请 2.业务申请 3.人事调度 4.采购申请 5.用车申请 6.财务申请 7.自定义申请
     * @param pageNum
     * @param pageSize
     * @param dataType 1.代办数据 2.申请历史 3.审批历史
     * @return
     */
    @ResponseBody
    @RequestMapping("/list")
    public PageJson procedureHistory(String leaveType,String senderName,String businessName, String pageNum, String pageSize,String produceType,String dataType,String userId) {
        if(StringUtils.isBlank(pageNum) || StringUtils.isBlank(pageSize)){
            pageNum = "1";
            pageSize = "10000";
        }
        String produce = getFacedCode(produceType);
        ProduceFaced biz = produceFacadeFactory.getBizFacad(produce);
        PageJson pageJson = new PageJson();
        switch (dataType){
            case "1":
                return biz.findPendingList(pageNum, pageSize, senderName,businessName,leaveType,userId);
            case "2":
                return biz.findSendList(pageNum, pageSize, senderName,businessName,leaveType,userId);
            case "3":
                return biz.findHistoricList(pageNum, pageSize, senderName,businessName,leaveType,userId);
            default:
                return pageJson;
        }
    }

    /**
     * 查询1.请假申请 2.业务申请 3.人事调度 4.采购申请 5.用车申请 6.财务申请
     *
     * @param produceType 1.请假申请 2.业务申请 3.人事调度 4.采购申请 5.用车申请 6.财务申请 7.自定义申请
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping("/submit")
    public ResultJson procedureSubmit(String body, String taskResult, String pageNum, String pageSize, String produceType, String userId,String goods, String modelKey) {
        logger.info("流程提交参数...userId={},produceType={},body={},modelKey={},taskResult={}",userId,produceType,body,modelKey,taskResult);
        String produce = getFacedCode(produceType);
        logger.info("流程produce:{}",produce);
        TaskBean taskBean = JsonMapper.fromJsonString(taskResult, TaskBean.class);
        taskBean.setSequenceFlowName(SequenceFlowNameUtil.getSequenceFlowName(userId,null,userService));
        ProduceFaced biz = produceFacadeFactory.getBizFacad(produce);
        return biz.submit(body, GfJsonUtil.toJSONString(taskBean),userId,goods,modelKey);
    }


    /**
     * 审批
     *
     * @param approvalResult
     * @param taskResult
     * @return
     */
    @ResponseBody
    @RequestMapping("approval")
    public ResultJson approval(String produceType,String businessId, String approvalResult, String taskResult,String userId) {
        String produce = getFacedCode(produceType);
        ProduceFaced biz = produceFacadeFactory.getBizFacad(produce);
        approvalResult = StringUtils.isBlank(approvalResult) ? "无" : approvalResult;
        return biz.approvalForm(businessId, approvalResult, taskResult,userId);
    }

    /**
     * 详情
     * 必传: produceType、type（historic为历史 pendding:审核）、userId
     * 在首次发起流程进入页面时调用：传参（userId，produceType，type，modelKey，startSequenceFlowName）  modelKey必传 任务类型（qingjia、renshidiaodu、yewushenqing、caigou、caiwushenqing、shouwen、yongche、fawen、aaaa）startSequenceFlowName可以不传
     * 在审批进入页面的时候调用：传参（userId，produceType，type，businessId（列表中的ID），taskId）
     * 采购申请特殊处理 满足上面的情况下，在发起和审批过程中需要传参firstTask在第一次发起申请的时候穿"true"，在代办的时候传“false”
     * @return
     */
    @ResponseBody
    @RequestMapping("detail")
    public ResultJson detail(String produceType,String businessId, String firstTask, String type,String taskId,
                             String modelKey, String startSequenceFlowName,String userId) {

        String produce = getFacedCode(produceType);
        ProduceFaced biz = produceFacadeFactory.getBizFacad(produce);
        return biz.detail(produceType,businessId,firstTask,type,taskId,modelKey,startSequenceFlowName,userId);
    }

    private String getFacedCode(String produceType){
        String produce = "";
        switch (produceType){
            case "1":
                produce = "leave";
                break;
            case "2":
                produce = "business";
                break;
            case "3":
                produce = "personnel";
                break;
            case "4":
                produce = "purchase";
                break;
            case "5":
                produce = "carApply";
                break;
            case "6":
                produce = "finance";
                break;
            case "7":
                produce = "simple";
                break;
            default:
                break;
        }
        return produce;
    }
}