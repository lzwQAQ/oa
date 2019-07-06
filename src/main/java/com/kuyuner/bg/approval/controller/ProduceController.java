package com.kuyuner.bg.approval.controller;

import com.kuyuner.bg.approval.lib.ProduceFacadeFactory;
import com.kuyuner.bg.approval.service.ProduceFaced;
import com.kuyuner.common.controller.BaseController;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 业务申请Service层接口
 *
 * @author administrator
 */
@Controller
@RequestMapping("${kuyuner.admin-path}/procedure")
public class ProduceController extends BaseController {

    @Autowired
    private ProduceFacadeFactory produceFacadeFactory;

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