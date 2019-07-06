package com.kuyuner.bg.email.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.kuyuner.bg.email.entity.EmailReceive;
import com.kuyuner.bg.email.entity.EmailSend;
import com.kuyuner.bg.email.service.EmailReceiveService;
import com.kuyuner.bg.email.service.EmailSendService;
import com.kuyuner.bg.msg.util.HtmlRegexpUtil;
import com.kuyuner.common.controller.*;
import com.kuyuner.common.io.FileInfo;
import com.kuyuner.common.lang.StringUtils;
import com.kuyuner.common.mapper.JsonMapper;
import com.kuyuner.core.sys.entity.User;
import com.kuyuner.core.sys.security.UserUtils;
import com.kuyuner.core.sys.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 发件箱Service层接口
 *
 * @author administrator
 */
@Controller
@RequestMapping("${kuyuner.admin-path}/emailsend")
public class EmailSendController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private EmailSendService emailSendService;

    @Autowired
    private EmailReceiveService emailReceiveService;

    @Autowired
    private UserService userService;

    /**
     * 显示列表页面
     *
     * @return
     */
    @RequestMapping("send")
    public String showEmailSendList() {
        return "email/emailSendList";
    }

    /**
     * 显示列表页面
     *
     * @return
     */
    @RequestMapping("draft")
    public String showEmailDraftList() {
        return "email/emailDraftList";
    }

    /**
     * 显示发送邮件页面
     *
     * @param id
     * @param modelMap
     * @return
     */
    @RequestMapping({"form/{id}", "form"})
    public String showEmailSendForm(@PathVariable(name = "id", required = false) String id, String type,
                                    String emailType, ModelMap modelMap) {
        if (StringUtils.isNotBlank(id)) {
            EmailSend emailSend = new EmailSend();
            if ("receive".equals(emailType)) {
                EmailReceive emailReceive = emailReceiveService.getEmail(id);
                BeanUtils.copyProperties(emailReceive, emailSend);
            } else {
                emailSend = emailSendService.getEmail(id);
            }
            if (StringUtils.isNotBlank(emailType)) {
                emailSend.setId(null);
                emailSend.setTitle(("forward".equals(type) ? "转发：" : "回复：") + emailSend.getTitle());
                StringBuilder sender = new StringBuilder();
                StringBuilder receivers = new StringBuilder();
                StringBuilder copys = new StringBuilder();
                makeUser(sender, emailSend.getSenderName(), emailSend.getSenderAccount());
                makeUser(receivers, emailSend.getReceiverName(), emailSend.getReceiverAccount());
                makeUser(copys, emailSend.getCopySenderName(), emailSend.getCopySenderAccount());
                modelMap.addAttribute("sender", sender.toString());
                modelMap.addAttribute("receivers", receivers.toString());
                modelMap.addAttribute("copys", copys.toString());
            }
            modelMap.addAttribute("emailSend", emailSend);
            modelMap.addAttribute("fromId", id);
        } else {
            modelMap.addAttribute("emailSend", new EmailSend());
            modelMap.addAttribute("fromId", null);
        }
        return "email/emailForm";
    }

    /**
     * 查询发件箱数据
     *
     * @param emailSend
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping("list")
    public PageJson list(EmailSend emailSend, String pageNum, String pageSize,String userId) {
        emailSend.setDraft("0");
        if(StringUtils.isBlank(pageNum) || StringUtils.isBlank(pageSize)){
            pageNum = "1";
            pageSize = "10000";
        }
        return emailSendService.findPageList(pageNum, pageSize, emailSend,userId);
    }

    /**
     * 查询草稿信息
     *
     * @param emailSend
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping("draft/list")
    public PageJson findDraftList(EmailSend emailSend, String pageNum, String pageSize,String userId) {
        emailSend.setDraft("1");
        if(StringUtils.isBlank(pageNum) || StringUtils.isBlank(pageSize)){
            pageNum = "1";
            pageSize = "10000";
        }
        return emailSendService.findPageList(pageNum, pageSize, emailSend,userId);
    }

    /**
     * 新增或修改数据
     *receivers
     * @return
     */
    @ResponseBody
    @RequestMapping("save")
    public ResultJson save(String userId,String id, String[] receivers, String[] copys, String[] secrets, String title,
                           String editorValue, String toMessage, String draft, String schedule,
                           String type, String level, String files, String scheduleTime) throws IOException {
        logger.info("日志打印发送邮件参数:receivers:{},copys:{},secrets{}",receivers,copys,secrets);
        EmailSend emailSend = new EmailSend();
        emailSend.setId(id);
        emailSend.setTitle(title);
        emailSend.setContent(editorValue);
        emailSend.setDraft("1".equals(draft) ? "1" : "0");
        emailSend.setToMessage("1".equals(toMessage) ? "1" : "0");
        emailSend.setLevel(level);
        emailSend.setIsSchedule("1".equals(schedule) ? "1" : "0");
        emailSend.setType(type);

        List<String> receiverName = new ArrayList<>();
        List<String> receiverAccount = new ArrayList<>();
        List<String> copyName = new ArrayList<>();
        List<String> copyAccount = new ArrayList<>();
        List<String> secretName = new ArrayList<>();
        List<String> secretAccount = new ArrayList<>();
        splitArray(receivers, receiverName, receiverAccount);
        splitArray(copys, copyName, copyAccount);
        splitArray(secrets, secretName, secretAccount);
        emailSend.setReceiverName(StringUtils.join(receiverName, ";"));
        emailSend.setReceiverAccount(StringUtils.join(receiverAccount, ";"));
        emailSend.setCopySenderName(StringUtils.join(copyName, ";"));
        emailSend.setCopySenderAccount(StringUtils.join(copyAccount, ";"));
        emailSend.setSecretSenderName(StringUtils.join(secretName, ";"));
        emailSend.setSecretSenderAccount(StringUtils.join(secretAccount, ";"));

        User user = userService.get(UserUtils.getPrincipal() == null ? userId : UserUtils.getPrincipal().getId());
        emailSend.setSenderName(user.getName());
        emailSend.setSenderAccount(user.getEmail());

        List<FileInfo> fileInfos = new ArrayList<>();
        if (StringUtils.isNotBlank(files)) {
            fileInfos = JsonMapper.getInstance().readValue(files, new TypeReference<List<FileInfo>>() {});
        }
        emailSend.setContainFile(fileInfos.size() == 0 ? "0" : "1");

        return emailSendService.saveOrUpdate(emailSend, fileInfos, scheduleTime,userId);
    }

    /**
     * 收藏或取消收藏
     *
     * @param id
     * @param star
     * @return
     */
    @ResponseBody
    @RequestMapping("star/{id}")
    public ResultJson star(@PathVariable("id") String id, String star) {
        EmailSend emailSend = new EmailSend();

        emailSend.setId(id);
        emailSend.setStar("1".equals(star) ? "1" : "0");
        emailSendService.update(emailSend);
        return ResultJson.ok("1".equals(emailSend.getStar()) ? "收藏成功！" : "取消成功！");
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
        return emailSendService.deletes(ids.split(","));
    }

    /**
     * 更新删除数据
     *
     * @param ids
     * @return
     */
    @ResponseBody
    @RequestMapping("update/deletes")
    public ResultJson updateDelete(String ids) {
        return emailSendService.updateDeletes(ids.split(","));
    }

    /**
     * 查询收件人
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("receivers")
    public ListJson findReceivers() {
        return emailSendService.findReceivers();
    }

    /**
     * 查询已经选择的收件人等信息
     *
     * @param emailId
     * @return
     */
    @ResponseBody
    @RequestMapping("selectedusers")
    public ResultJson findSelectedUsers(String emailId) {
        return emailSendService.findSelectedUsers(emailId);
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

    private void splitArray(String[] arra, List<String> name, List<String> account) {
        if (arra == null) {
            return;
        }
        for (String receiver : arra) {
            name.add(receiver.split(";")[0]);
            account.add(receiver.split(";")[1]);
        }
    }

    //#######################################草稿箱####################################
    /**
     * 显示发送邮件页面
     *
     * @param id
     * @return
     */
    @RequestMapping("draft/detail")
    @ResponseBody
    public ResultJson draftDetail(String id, String sendType, String emailType) {
        Map map = new HashMap();
        if (StringUtils.isNotBlank(id)) {
            EmailSend emailSend = new EmailSend();
            if ("receive".equals(emailType)) {
                EmailReceive emailReceive = emailReceiveService.getEmail(id);
                BeanUtils.copyProperties(emailReceive, emailSend);
            } else {
                emailSend = emailSendService.getEmail(id);
            }
            if (StringUtils.isNotBlank(emailType)) {
                emailSend.setId(null);
                emailSend.setTitle(("forward".equals(sendType) ? "转发：" : "回复：") + emailSend.getTitle());
                StringBuilder sender = new StringBuilder();
                StringBuilder receivers = new StringBuilder();
                StringBuilder copys = new StringBuilder();
                makeUser(sender, emailSend.getSenderName(), emailSend.getSenderAccount());
                makeUser(receivers, emailSend.getReceiverName(), emailSend.getReceiverAccount());
                makeUser(copys, emailSend.getCopySenderName(), emailSend.getCopySenderAccount());
                map.put("sender", sender.toString());
                map.put("receivers", receivers.toString());
                map.put("copys", copys.toString());
            }
            emailSend.setContent(HtmlRegexpUtil.filterHtml(emailSend.getContent()));
            map.put("emailSend", emailSend);
            map.put("fromId", id);
        } else {
            map.put("emailSend", new EmailSend());
            map.put("fromId", null);
        }
        return ResultJson.ok(map);
    }


    /**
     * 新增或修改数据
     *receivers
     * @return
     */
    @ResponseBody
    @RequestMapping("saveForApp")
    public ResultJson saveForApp(String userId,String id, String receivers, String copys, String secrets, String title,
                           String editorValue, String toMessage, String draft, String schedule,
                           String type, String level, String files, String scheduleTime) throws IOException {
        logger.info("日志打印发送邮件参数:receivers:{},copys:{},secrets:{}",receivers,copys,secrets);
        if(StringUtils.isBlank(receivers)){
            return ResultJson.failed("请录入收件人信息");
        }
        String[] recArr = receivers.split(",");
        String[] copArr = new String[0];
        if (StringUtils.isNotBlank(copys)) {
            copArr = copys.split(",");
        }
        String[] secArr = new String[0];
        if (StringUtils.isNotBlank(secrets)) {
            secArr = secrets.split(",");
        }

        EmailSend emailSend = new EmailSend();
        emailSend.setId(id);
        emailSend.setTitle(title);
        emailSend.setContent(editorValue);
        emailSend.setDraft("1".equals(draft) ? "1" : "0");
        emailSend.setToMessage("1".equals(toMessage) ? "1" : "0");
        emailSend.setLevel(level);
        emailSend.setIsSchedule("1".equals(schedule) ? "1" : "0");
        emailSend.setType(type);

        List<String> receiverName = new ArrayList<>();
        List<String> receiverAccount = new ArrayList<>();
        List<String> copyName = new ArrayList<>();
        List<String> copyAccount = new ArrayList<>();
        List<String> secretName = new ArrayList<>();
        List<String> secretAccount = new ArrayList<>();
        splitArray(recArr, receiverName, receiverAccount);
        splitArray(copArr, copyName, copyAccount);
        splitArray(secArr, secretName, secretAccount);
        emailSend.setReceiverName(StringUtils.join(receiverName, ";"));
        emailSend.setReceiverAccount(StringUtils.join(receiverAccount, ";"));
        emailSend.setCopySenderName(StringUtils.join(copyName, ";"));
        emailSend.setCopySenderAccount(StringUtils.join(copyAccount, ";"));
        emailSend.setSecretSenderName(StringUtils.join(secretName, ";"));
        emailSend.setSecretSenderAccount(StringUtils.join(secretAccount, ";"));

        User user = userService.get(UserUtils.getPrincipal() == null ? userId : UserUtils.getPrincipal().getId());
        emailSend.setSenderName(user.getName());
        emailSend.setSenderAccount(user.getEmail());

        List<FileInfo> fileInfos = new ArrayList<>();
        if (StringUtils.isNotBlank(files)) {
            fileInfos = JsonMapper.getInstance().readValue(files, new TypeReference<List<FileInfo>>() {});
        }
        emailSend.setContainFile(fileInfos.size() == 0 ? "0" : "1");

        return emailSendService.saveOrUpdate(emailSend, fileInfos, scheduleTime,userId);
    }
}