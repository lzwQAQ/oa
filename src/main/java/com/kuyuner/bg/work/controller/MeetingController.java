package com.kuyuner.bg.work.controller;

import com.kuyuner.common.lang.StringUtils;
import com.kuyuner.common.controller.BaseController;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.bg.work.entity.Meeting;
import com.kuyuner.bg.work.service.MeetingService;
import com.kuyuner.core.sys.security.UserUtils;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * 会议通知Controller层
 *
 * @author administrator
 */
@Controller
@RequestMapping("${kuyuner.admin-path}/meeting")
public class MeetingController extends BaseController {

    @Autowired
    private MeetingService meetingService;

    /**
     * 显示列表页面
     *
     * @return
     */
    @RequestMapping("meeting")
    public String showMeetingList() {
        return "work/meetingList";
    }

    /**
     * 发起会议
     *
     * @return
     */
    @RequestMapping("meetingsend")
    public String meetingSend() {
        return "work/meetingSend";
    }

    /**
     * 显示表单页面
     *
     * @param modelMap
     * @return
     */
    @RequestMapping("form")
    public String showMeetingForm(ModelMap modelMap) {
        Meeting meeting = new Meeting();
        meeting.setTrainer(UserUtils.getUser());
        modelMap.addAttribute("meeting", meeting);
        return "work/meetingForm";
    }

    /**
     * 显示详情页面
     *
     * @param modelMap
     * @return
     */
    @RequestMapping("form/{id}")
    public String showMeetingForm(@PathVariable(name = "id", required = false) String id, ModelMap modelMap) {
        Meeting meeting = meetingService.get(id);
        modelMap.addAttribute("meeting", meeting);
        modelMap.addAttribute("joinPeoples", meetingService.findJoinPeoples(meeting.getJoinPeople().split(",")));
        return "work/meetingDetail";
    }

    /**
     * 查询分页集合JSON数据
     *
     * @param meeting
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping("list")
    public PageJson list(Meeting meeting, String pageNum, String pageSize,String userId) {
        if(StringUtils.isBlank(pageNum) || StringUtils.isBlank(pageSize)){
            pageNum="1";
            pageSize="10000";
        }
        return meetingService.findPageList(pageNum, pageSize, meeting,userId);
    }

    /**
     * 发起培训列表
     *
     * @param meeting
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping("sendlist")
    public PageJson sendList(Meeting meeting, String pageNum, String pageSize,String userId) {
        if(StringUtils.isBlank(pageNum) || StringUtils.isBlank(pageSize)){
            pageNum="1";
            pageSize="10000";
        }
        return meetingService.findSendPageList(pageNum, pageSize, meeting,userId);
    }

    /**
     * 新增或修改数据
     *
     * @param meeting
     * @return
     */
    @ResponseBody
    @RequestMapping("save")
    public ResultJson save(Meeting meeting,String userId) {
        return meetingService.saveOrUpdate(meeting,userId);
    }

    /**
     * 删除数据
     *
     * @param ids
     * @return
     */
    @ResponseBody
    @RequestMapping("deletes")
    public ResultJson delete(String ids) {
        return meetingService.deletes(ids.split(","));
    }



    /**
     * 显示详情页面
     *
     * @return
     */
    @RequestMapping("detail")
    @ResponseBody
    public ResultJson showMeetingForm(String id) {
        Meeting meeting = meetingService.get(id);
        Map map = new HashMap();
        map.put("meeting", meeting);
        map.put("joinPeoples", meetingService.findJoinPeoples(meeting.getJoinPeople().split(",")));
        return ResultJson.ok(map);
    }
}