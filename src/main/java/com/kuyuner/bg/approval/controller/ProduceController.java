package com.kuyuner.bg.approval.controller;

import com.kuyuner.bg.approval.entity.Business;
import com.kuyuner.bg.approval.entity.Leave;
import com.kuyuner.bg.approval.entity.PersonnelAdjustment;
import com.kuyuner.bg.approval.service.BusinessService;
import com.kuyuner.bg.approval.service.LeaveService;
import com.kuyuner.bg.approval.service.PersonnelAdjustmentService;
import com.kuyuner.common.controller.BaseController;
import com.kuyuner.common.controller.ListJson;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.common.lang.StringUtils;
import com.kuyuner.core.sys.entity.User;
import com.kuyuner.core.sys.security.UserUtils;
import com.kuyuner.core.sys.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
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
    private BusinessService businessService;
    @Autowired
    private LeaveService leaveService;
    @Autowired
    private PersonnelAdjustmentService personnelAdjustmentService;

    /**
     * 查询1.代办数据 2.申请历史 3.审批历史 4.采购申请 5.用车申请 6.财务申请
     *
     * @param produceType 1.请假 2.业务 3.人事
     * @param pageNum
     * @param pageSize
     * @param dataType 1.代办数据 2.申请历史 3.审批历史
     * @return
     */
    @ResponseBody
    @RequestMapping("/list")
    public PageJson procedureHistory(String leaveType,String senderName, String pageNum, String pageSize,String produceType,String dataType,String userId) {
        if(StringUtils.isBlank(pageNum) || StringUtils.isBlank(pageSize)){
            pageNum = "1";
            pageSize = "10000";
        }
        PageJson pageJson = new PageJson();
        switch (produceType){
            case "1":
                Leave leave = new Leave();
                leave.setType(leaveType);
                leave.setSenderName(senderName);
                switch (dataType){
                    case "1":
                        pageJson = leaveService.findPendingList(pageNum, pageSize, leave,userId);
                        break;
                    case "2":
                        pageJson = leaveService.findSendList(pageNum, pageSize, leave,userId);
                        break;
                    case "3":
                        pageJson = leaveService.findHistoricList(pageNum, pageSize, leave,userId);
                        break;
                    default:
                            break;
                }
               break;
            case "2":
                Business business = new Business();
                switch (dataType){
                    case "1":
                        pageJson = businessService.findPendingList(pageNum, pageSize, business,userId);
                        break;
                    case "2":
                        pageJson = businessService.findSendList(pageNum, pageSize, business,userId);
                        break;
                    case "3":
                        pageJson = businessService.findHistoricList(pageNum, pageSize, business,userId);
                        break;
                    default:
                        break;
                }
                break;
            case "3":
                PersonnelAdjustment personnelAdjustment = new PersonnelAdjustment();
                personnelAdjustment.setSenderName(senderName);
                switch (dataType){
                    case "1":
                        pageJson = personnelAdjustmentService.findPendingList(pageNum, pageSize, personnelAdjustment,userId);
                        break;
                    case "2":
                        pageJson = personnelAdjustmentService.findSendList(pageNum, pageSize, personnelAdjustment,userId);
                        break;
                    case "3":
                        pageJson = personnelAdjustmentService.findHistoricList(pageNum, pageSize, personnelAdjustment,userId);
                        break;
                    default:
                        break;
                }
               break;
            default:

                break;
        }
        return pageJson;
    }

}