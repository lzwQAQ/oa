package com.kuyuner.bg.msg.controller;

import com.kuyuner.bg.msg.dto.GroupDTO;
import com.kuyuner.bg.msg.entity.GroupUser;
import com.kuyuner.bg.msg.entity.Message;
import com.kuyuner.bg.msg.entity.MessageSend;
import com.kuyuner.bg.msg.service.MessageSendService;
import com.kuyuner.bg.msg.service.MessageService;
import com.kuyuner.common.controller.BaseController;
import com.kuyuner.common.controller.ListJson;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.common.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * 会议通知Controller层
 *
 * @author administrator
 */
@Controller
@RequestMapping("${kuyuner.admin-path}/message")
public class MessageController extends BaseController {
    @Autowired
    private MessageService messageService;
    @Autowired
    private MessageSendService messageSendService;

    /**
     * 短信列表页
     * @param type
     * @param modelMap
     * @return
     */
    @RequestMapping({"list"})
    public String smsList(@PathVariable(name = "type", required = false) String type, ModelMap modelMap) {
        modelMap.addAttribute("type", type);
        return "sms/smsList";
    }

    @RequestMapping({"form/{id}", "form"})
    public String smsForm(@PathVariable(name = "id", required = false) String id, ModelMap modelMap) {
        Message message = messageService.getById(id);
        modelMap.addAttribute("id", id);
        modelMap.addAttribute("message", message);
        return "sms/smsForm";
    }

    /**
     * 短信群组
     */
    @RequestMapping({"group"})
    public String group(@PathVariable(name = "groupId", required = false) String groupId, ModelMap modelMap) {
        return "sms/groupList";
    }

    /**
     * 短信详情
     */
    @RequestMapping({"groupUser/{id}"})
    public String getGroupUserById(@PathVariable(name = "id", required = false) String id, ModelMap modelMap) {
        GroupUser groupUser = messageService.getGroupUserById(id);
        modelMap.addAttribute("id", id);
        modelMap.addAttribute("groupUser", groupUser);
        return "sms/group";
    }

    /**
     * 短信群组编辑
     */
    @RequestMapping({"group/form/{id}", "group/form"})
    public String groupForm(@PathVariable(name = "id", required = false) String id, ModelMap modelMap) {
        GroupUser userGroup = new GroupUser();
        if(StringUtils.isNotBlank(id)){
            userGroup = messageService.getGroupUserById(id);
        }
        modelMap.addAttribute("userGroup", userGroup);
        return "sms/groupForm";
    }

    /**
     * 短信组所有数据
     */
    @ResponseBody
    @RequestMapping("groupAll")
    public ListJson groupAll() {
        List<GroupUser> userGroup = messageService.getGroupUserAll();
        return new ListJson(userGroup);
    }

    /**
     * 新增或修改数据
     */
    @ResponseBody
    @RequestMapping("saveOrUpdate")
    public ResultJson save(HttpServletRequest request, Message message) throws IOException {
        return messageService.saveOrUpdate(message);
    }

    /**
     * 短信列表
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping("/smsList")
    public PageJson sendList(Message message, String pageNum, String pageSize) {
        return messageService.findMessageList(pageNum, pageSize, message);
    }

    /**
     * 短信发送状况列表
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping("/sendRecordList")
    public PageJson sendRecordList(MessageSend messageSend, String pageNum, String pageSize) {
        return messageSendService.findList(pageNum, pageSize, messageSend);
    }

    /**
     * 获取部门树形集合信息
     * @return
     */
    @ResponseBody
    @RequestMapping("/getAllDeptPerson")
    public ListJson getAllDeptPerson(Message message, String pageNum, String pageSize) {
        return messageService.getAllDeptPerson();
    }

    /**
     * 获取树形用户分组
     * @return
     */
    @ResponseBody
    @RequestMapping("/userGroupList")
    public ListJson userGroupList(GroupUser group) {
        return new ListJson(messageService.userGroupList(group.getNodeid()));
    }

    /**
     * 保存或修改用户组
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/saveOrUpdateGroup",method = POST)
    public ResultJson saveOrUpdateGroup(HttpServletRequest request,@RequestBody GroupDTO groupDTO) {
        return messageService.saveOrUpdateGroup(groupDTO);
//        return null;
    }

    /**
     * 新增组下用户
     * @return
     */
    /*@ResponseBody
    @RequestMapping("/saveGroupUser")
    public ResultJson saveGroupUser(HttpServletRequest request, GroupUser groupUser) {
        return messageService.saveGroupUser(groupUser);
    }*/

    /**
     * 删除组下用户
     * @return
     */
    /*@ResponseBody
    @RequestMapping("/removeGroupUser")
    public ResultJson removeGroupUser(HttpServletRequest request, GroupUser groupUser) {
        return messageService.removeGroupUser(groupUser);
    }*/

    /**
     * 删除用户组
     * @return
     */
    @ResponseBody
    @RequestMapping("/deleteGroup")
    public ResultJson deleteGroup(HttpServletRequest request, String ids) {
        return messageService.deleteGroup(ids);
    }
}