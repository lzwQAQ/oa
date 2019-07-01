package com.kuyuner.bg.email.controller;

import com.kuyuner.bg.email.entity.*;
import com.kuyuner.bg.email.service.EmailFileService;
import com.kuyuner.bg.email.service.EmailReceiveService;
import com.kuyuner.bg.email.service.EmailSendService;
import com.kuyuner.bg.email.service.EmailService;
import com.kuyuner.common.controller.BaseController;
import com.kuyuner.common.controller.ListJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.common.lang.StringUtils;
import com.kuyuner.common.network.WebUtil;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author administrator
 */
@Controller
@RequestMapping("${kuyuner.admin-path}/email")
public class EmailController extends BaseController {

    @Value("${kuyuner.file-base-path}")
    private String fileBasePath;

    @Autowired
    private EmailReceiveService emailReceiveService;

    @Autowired
    private EmailSendService emailSendService;

    @Autowired
    private EmailFileService emailFileService;

    @Autowired
    private EmailService emailService;

    /**
     * 查看邮件详情
     *
     * @param id
     * @param type
     * @param modelMap
     * @return
     */
    @RequestMapping("/view/{type}/{id}")
    public String showEmailDetails(@PathVariable("id") String id, @PathVariable("type") String type, ModelMap modelMap) {
        Email email = null;
        String isSchedule = "0";
        String isSecret = "0";
        Date scheduleTime = null;
        if ("send".equals(type)) {
            email = emailSendService.getEmail(id);
            isSchedule = ((EmailSend) email).getIsSchedule();
            if ("1".equals(isSchedule)) {
                scheduleTime = emailSendService.getScheduleTime(email.getId());
            }
        } else if ("receive".equals(type)) {
            email = emailReceiveService.getEmail(id);
            isSecret = ((EmailReceive) email).getIsSecret();
        }
        StringBuilder receivers = new StringBuilder();
        StringBuilder copys = new StringBuilder();
        makeUser(receivers, email.getReceiverName(), email.getReceiverAccount());
        makeUser(copys, email.getCopySenderName(), email.getCopySenderAccount());

        modelMap.addAttribute("email", email);
        modelMap.addAttribute("isSchedule", isSchedule);
        modelMap.addAttribute("scheduleTime", scheduleTime);
        modelMap.addAttribute("receivers", receivers.toString());
        modelMap.addAttribute("copys", copys.toString());
        modelMap.addAttribute("emailType", type);
        modelMap.addAttribute("isSecret", isSecret);

        return "email/emailDetail";
    }

    /**
     * 展示邮件的html内容
     *
     * @param id
     * @param type
     * @param modelMap
     * @return
     */
    @RequestMapping("/view/html/{type}/{id}")
    public String showEmailHtml(@PathVariable("id") String id, @PathVariable("type") String type,
                                ModelMap modelMap) {
        String content;
        if ("send".equals(type)) {
            content = emailSendService.getEmailContent(id);
        } else {
            content = emailReceiveService.getEmailContent(id);
        }
        modelMap.addAttribute("content", content);
        return "email/emailDetailContent";
    }

    /**
     * 查看收藏邮件
     *
     * @return
     */
    @RequestMapping("favorite/favorite")
    public String showFavoriteHtml() {
        return "email/emailFavoriteList";
    }

    /**
     * 查看回收站
     *
     * @return
     */
    @RequestMapping("recyclebin/recyclebin")
    public String showRecyclebinHtml() {
        return "email/emailRecyclebinList";
    }

    /**
     * 查询邮件的所有附件
     *
     * @param emailId
     * @return
     */
    @ResponseBody
    @RequestMapping("/file/list")
    public ListJson findEmailFiles(String emailId) {
        return emailFileService.findEmailFiles(emailId);
    }

    /**
     * 下载邮件附件
     *
     * @param request
     * @param response
     * @param fileId
     * @throws IOException
     */
    @RequestMapping("file/download/{fileId}")
    public void findEmailFiles(HttpServletRequest request, HttpServletResponse response,
                               @PathVariable("fileId") String fileId) throws IOException {
        EmailFile file = emailFileService.get(fileId);
        WebUtil.setResponseFileName(request, response, file.getFileName());
        String filePath = file.getFilePath().replace("files/", "");
        IOUtils.copy(new FileInputStream(new File(fileBasePath + filePath)), response.getOutputStream());
    }

    /**
     * 查询需要回复的人员
     *
     * @param emailId
     * @return
     */
    @ResponseBody
    @RequestMapping("send/replyusers")
    public ResultJson findSendReplyusers(String emailId, String type) {
        return emailSendService.findReplyusers(emailId, type);
    }

    /**
     * 查询需要回复的人员
     *
     * @param emailId
     * @return
     */
    @ResponseBody
    @RequestMapping("receive/replyusers")
    public ResultJson findReceiveReplyusers(String emailId, String type) {
        return emailReceiveService.findReplyusers(emailId, type);
    }

    /**
     * 查询收藏的邮件
     *
     * @param email
     * @return
     */
    @ResponseBody
    @RequestMapping("favorite/list")
    public ListJson findStarEmailList(Email email,String userId) {
        return emailService.findStarEmailList(email, userId);
    }

    /**
     * 查询回收站
     *
     * @param email
     * @return
     */
    @ResponseBody
    @RequestMapping("recyclebin/list")
    public ListJson findRecyclebinList(Email email,String userId) {
        return emailService.findRecyclebinList(email,userId);
    }

    /**
     * 彻底删除数据
     *
     * @param ids
     * @return
     */
    @ResponseBody
    @RequestMapping("deletes")
    public ResultJson deletes(String ids) {
        return emailService.deletes(ids.split(","));
    }

    /**
     * 假删
     *
     * @param ids
     * @return
     */
    @ResponseBody
    @RequestMapping("update/deletes")
    public ResultJson updateDeletes(String ids) {
        return emailService.updateDeletes(ids.split(","));
    }

    /**
     * 恢复邮件
     *
     * @param ids
     * @return
     */
    @ResponseBody
    @RequestMapping("recoverys")
    public ResultJson recoverys(String ids) {
        return emailService.recoverys(ids.split(","));
    }

    /**
     * 拼凑收件人和抄送人
     *
     * @param builder
     * @param name
     * @param account
     */
    private void makeUser(StringBuilder builder, String name, String account) {
        if (StringUtils.isBlank(name)) {
            return;
        }

        String[] names = name.split(";");
        String[] accounts = account.split(";");
        for (int i = 0; i < names.length; i++) {
            builder.append(names[i] + "<" + accounts[i] + ">");
            builder.append(", ");
        }
        builder.setLength(builder.length() - 2);
    }


    //#################################H5#############################################################################
    /**
     * 查看邮件详情
     *
     * @param id
     * @param type
     * @return
     */
    @RequestMapping("/emailDetails")
    @ResponseBody
    public ResultJson emailDetails(String id,String type) {
        Map map = new HashMap();
        Email email = null;
        String isSchedule = "0";
        String isSecret = "0";
        Date scheduleTime = null;
        if ("send".equals(type)) {
            email = emailSendService.getEmail(id);
            isSchedule = ((EmailSend) email).getIsSchedule();
            if ("1".equals(isSchedule)) {
                scheduleTime = emailSendService.getScheduleTime(email.getId());
            }
        } else if ("receive".equals(type)) {
            email = emailReceiveService.getEmail(id);
            isSecret = ((EmailReceive) email).getIsSecret();
        }
        StringBuilder receivers = new StringBuilder();
        StringBuilder copys = new StringBuilder();
        makeUser(receivers, email.getReceiverName(), email.getReceiverAccount());
        makeUser(copys, email.getCopySenderName(), email.getCopySenderAccount());

        map.put("email", email);
        map.put("isSchedule", isSchedule);
        map.put("scheduleTime", scheduleTime);
        map.put("receivers", receivers.toString());
        map.put("copys", copys.toString());
        map.put("emailType", type);
        map.put("isSecret", isSecret);

        return ResultJson.ok(map);
    }
}
