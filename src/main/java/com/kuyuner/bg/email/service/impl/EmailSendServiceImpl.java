package com.kuyuner.bg.email.service.impl;

import com.kuyuner.bg.email.dao.EmailFileDao;
import com.kuyuner.bg.email.dao.EmailSendDao;
import com.kuyuner.bg.email.entity.EmailFile;
import com.kuyuner.bg.email.entity.EmailReceive;
import com.kuyuner.bg.email.entity.EmailSend;
import com.kuyuner.bg.email.job.SendEmailJob;
import com.kuyuner.bg.email.service.EmailSendService;
import com.kuyuner.bg.job.TaskManager;
import com.kuyuner.bg.job.entity.ScheduleTask;
import com.kuyuner.common.controller.ListJson;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.common.exception.ServiceException;
import com.kuyuner.common.idgen.IdGenerate;
import com.kuyuner.common.io.FileInfo;
import com.kuyuner.common.lang.StringUtils;
import com.kuyuner.common.persistence.Page;
import com.kuyuner.core.sys.dao.UserDao;
import com.kuyuner.core.sys.entity.User;
import com.kuyuner.core.sys.security.UserUtils;

import org.quartz.CronExpression;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;

/**
 * 发件箱Service层实现
 *
 * @author administrator
 */
@Service
public class EmailSendServiceImpl implements EmailSendService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private EmailSendDao emailSendDao;

    @Autowired
    private EmailFileDao emailFileDao;

    @Resource
    private UserDao userDao;

    @Autowired
    private TaskManager taskManager;

    @Override
    public PageJson findPageList(String pageNum, String pageSize, EmailSend emailSend) {
        emailSend.setCreater(UserUtils.getPrincipal().getId());
        Page<EmailSend> page = new Page<>(pageNum, pageSize);
        page.start();
        emailSendDao.findList(emailSend);
        page.end();
        return new PageJson(page);
    }

    @Override
    public EmailSend get(String id) {
        return emailSendDao.get(new EmailSend(id));
    }

    @Override
    public EmailSend getEmail(String id) {
        return emailSendDao.getEmail(id);
    }

    @Override
    @Transactional
    public ResultJson saveOrUpdate(EmailSend emailSend, List<FileInfo> fileInfos, String scheduleTime) {
        if (StringUtils.isBlank(emailSend.getId())) {
            emailSend.setId(IdGenerate.uuid());
            emailSend.setCreater(UserUtils.getPrincipal().getId());
            emailSendDao.insert(emailSend);
        } else {
            //更新发送时间
            emailSend.setCreateDate(new Date());
            emailSendDao.update(emailSend);
        }
        emailSendDao.deleteFiles(emailSend.getId());
        if (fileInfos.size() > 0) {
            emailSendDao.saveSendEmailFiles(emailSend, fileInfos);
        }
        if ("1".equals(emailSend.getDraft())) {
            if ("1".equals(emailSend.getIsSchedule())) {
                addTask(emailSend.getId(), scheduleTime);
            }
            //draft等于1表示保存草稿
            return ResultJson.ok();
        }

        sendEmail(emailSend, fileInfos);

        return ResultJson.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultJson deletes(String... ids) {
        emailSendDao.deletes(ids);
        emailFileDao.deleteFiles(ids);
        String[] scheduleIds = emailSendDao.findSchedules(ids);
        emailSendDao.deleteSchedules(ids);
        try {
            taskManager.deleteSchedules(scheduleIds);
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
        }
        return ResultJson.ok();
    }

    @Override
    public ListJson findReceivers() {
        List<Map<String, Object>> list = emailSendDao.findReceivers();
        return new ListJson(list);
    }

    @Override
    public String getEmailContent(String id) {
        return emailSendDao.getEmailContent(id);
    }

    @Override
    public ResultJson update(EmailSend emailSend) {
        emailSendDao.update(emailSend);
        return ResultJson.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultJson updateDeletes(String[] ids) {
        emailSendDao.updateDeletes(ids);
        return ResultJson.ok();
    }

    @Override
    public ResultJson findSelectedUsers(String emailId) {
        Map<String, List<String>> selectedUsers = new HashMap<>(3);
        EmailSend emailSend = emailSendDao.findSelectedUsers(emailId);
        List<String> receivers = new ArrayList<>();
        List<String> copys = new ArrayList<>();
        List<String> secrets = new ArrayList<>();
        makeSelectedUsers(receivers, emailSend.getReceiverName().split(";"), emailSend.getReceiverAccount().split(";"));
        makeSelectedUsers(copys, emailSend.getCopySenderName().split(";"), emailSend.getCopySenderAccount().split(";"));
        makeSelectedUsers(secrets, emailSend.getSecretSenderName().split(";"), emailSend.getSecretSenderAccount().split(";"));
        selectedUsers.put("receivers", receivers);
        selectedUsers.put("copys", copys);
        selectedUsers.put("secrets", secrets);
        return ResultJson.ok(selectedUsers);
    }

    @Override
    public ResultJson findReplyusers(String emailId, String type) {
        Map<String, List<String>> selectedUsers = new HashMap<>(2);
        List<String> receivers = new ArrayList<>();
        List<String> copys = new ArrayList<>();
        EmailSend emailSend = emailSendDao.findSelectedUsers(emailId);
        makeSelectedUsers(receivers, emailSend.getReceiverName().split(";"), emailSend.getReceiverAccount().split(";"));
        makeSelectedUsers(copys, emailSend.getCopySenderName().split(";"), emailSend.getCopySenderAccount().split(";"));
        User user = userDao.get(new User(UserUtils.getPrincipal().getId()));
        String exclude = user.getName() + ";" + user.getEmail();
        receivers.remove(exclude);
        copys.remove(exclude);
        selectedUsers.put("receivers", receivers);
        selectedUsers.put("copys", copys);
        return ResultJson.ok(selectedUsers);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendScheduleEmail(JobKey jobKey, String emailId) {
        EmailSend emailSend = emailSendDao.get(new EmailSend(emailId));
        EmailFile emailFile = new EmailFile();
        emailFile.setEmailId(emailId);
        List<EmailFile> list = emailFileDao.findList(emailFile);
        List<FileInfo> fileInfos = new ArrayList<>(list.size());
        for (EmailFile file : list) {
            FileInfo fileInfo = new FileInfo();
            fileInfo.setName(file.getFileName());
            fileInfo.setSize(file.getFileSize());
            fileInfo.setUrl(file.getFilePath());
            fileInfos.add(fileInfo);
        }
        EmailSend email = new EmailSend();
        email.setId(emailId);
        email.setDraft("0");
        email.setIsSchedule("0");
        email.setCreateDate(new Date());
        emailSendDao.update(email);
        sendEmail(emailSend, fileInfos);
        taskManager.deleteInStorage(jobKey);
    }

    @Override
    public Date getScheduleTime(String id) {
        String cron = emailSendDao.getScheduleTime(id);
        CronExpression cronExpression = null;
        try {
            cronExpression = new CronExpression(cron);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return cronExpression.getTimeAfter(new Date());
    }

    private void makeSelectedUsers(List<String> list, String[] names, String[] accounts) {
        buildSelectedUsers(list, names, accounts);
    }

    static void buildSelectedUsers(List<String> list, String[] names, String[] accounts) {
        for (int i = 0; i < names.length; i++) {
            if (StringUtils.isNotBlank(names[i])) {
                String str = names[i] + ";" + accounts[i];
                if (!list.contains(str)) {
                    list.add(str);
                }
            }
        }
    }

    private void sendEmail(EmailSend emailSend, List<FileInfo> fileInfos) {
        List<String> list = new ArrayList<>();
        String[] secretAccounts = emailSend.getSecretSenderAccount().split(";");
        list.addAll(Arrays.asList(emailSend.getReceiverAccount().split(";")));
        list.addAll(Arrays.asList(emailSend.getCopySenderAccount().split(";")));
        list.addAll(Arrays.asList(secretAccounts));
        List<User> users = emailSendDao.findUsersByAccounts(list);

        List<EmailReceive> emailReceives = new ArrayList<>();
        List<EmailFile> emailFiles = new ArrayList<>();
        for (User user : users) {
            EmailReceive emailReceive = new EmailReceive();
            BeanUtils.copyProperties(emailSend, emailReceive);
            emailReceive.setId(IdGenerate.uuid());
            emailReceive.setCreater(user.getId());
            emailReceive.setIsSecret(containsSecretAccounts(user.getEmail(), secretAccounts) ? "1" : "0");
            emailReceives.add(emailReceive);

            for (FileInfo fileInfo : fileInfos) {
                EmailFile emailFile = new EmailFile();
                emailFile.setId(IdGenerate.uuid());
                emailFile.setFileName(fileInfo.getName());
                emailFile.setFilePath(fileInfo.getUrl());
                emailFile.setFileSize(fileInfo.getSize());
                emailFile.setEmailId(emailReceive.getId());
                emailFile.setCreater(user.getId());
                emailFiles.add(emailFile);
            }

        }
        emailSendDao.saveReceiverEmail(emailReceives);
        if (emailFiles.size() > 0) {
            emailSendDao.saveReceiveEmailFiles(emailFiles);
        }
    }

    /**
     * 添加定时任务
     *
     * @param emailId
     * @param scheduleTime
     */
    private void addTask(String emailId, String scheduleTime) {
        ScheduleTask scheduleTask = new ScheduleTask();
        scheduleTask.setId(IdGenerate.uuid());
        scheduleTask.setClassName(SendEmailJob.class.getName());
        scheduleTask.setMethodName("send");
        scheduleTask.setTaskType("send_email");
        scheduleTask.setData(emailId);
        String cron = dateToCron(scheduleTime);
        scheduleTask.setCron(cron);
        CronExpression cronExpression;
        try {
            cronExpression = new CronExpression(cron);
        } catch (ParseException e) {
            throw new ServiceException("定时任务时间设置有误，请重新设置！");
        }
        if (cronExpression.getTimeAfter(new Date()) == null) {
            throw new ServiceException("定时任务时间小于当前时间，请重新设置！");
        }
        taskManager.addTasks(scheduleTask);
    }

    /**
     * 将时间点转换成cron表达式
     *
     * @param dateStr
     * @return
     */
    private String dateToCron(String dateStr) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date;
        try {
            date = dateFormat.parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Calendar calendar = new java.util.GregorianCalendar(TimeZone.getDefault());
        calendar.setTime(date);
        return String.format("%d %d %d %d %d ? %d", calendar.get(Calendar.SECOND), calendar.get(Calendar.MINUTE), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));
    }

    private boolean containsSecretAccounts(String str, String... strs) {
        for (String s : strs) {
            if (StringUtils.isNotBlank(s)) {
                if (s.equals(str)) {
                    return true;
                }
            }
        }
        return false;
    }

}