package com.kuyuner.bg.email.service.impl;

import com.kuyuner.bg.email.dao.EmailFileDao;
import com.kuyuner.bg.email.dao.EmailReceiveDao;
import com.kuyuner.bg.email.entity.EmailReceive;
import com.kuyuner.bg.email.service.EmailReceiveService;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.common.idgen.IdGenerate;
import com.kuyuner.common.lang.StringUtils;
import com.kuyuner.common.persistence.Page;
import com.kuyuner.core.sys.dao.UserDao;
import com.kuyuner.core.sys.entity.User;
import com.kuyuner.core.sys.security.UserUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import javax.annotation.Resource;

/**
 * 收件箱Service层实现
 *
 * @author administrator
 */
@Service
public class EmailReceiveServiceImpl implements EmailReceiveService {

    @Autowired
    private EmailReceiveDao emailReceiveDao;

    @Autowired
    private EmailFileDao emailFileDao;

    @Resource
    private UserDao userDao;

    @Override
    public PageJson findPageList(String pageNum, String pageSize, EmailReceive emailReceive) {
        emailReceive.setCreater(UserUtils.getPrincipal().getId());
        Page<EmailReceive> page = new Page<>(pageNum, pageSize);
        page.start();
        emailReceiveDao.findList(emailReceive);
        page.end();
        return new PageJson(page);
    }

    @Override
    public EmailReceive get(String id) {
        return emailReceiveDao.get(new EmailReceive(id));
    }

    @Override
    public EmailReceive getEmail(String id) {
        EmailReceive emailReceive = new EmailReceive(id);
        emailReceive.setRead("1");
        emailReceiveDao.update(emailReceive);
        return emailReceiveDao.getEmail(id);
    }

    @Override
    public ResultJson saveOrUpdate(EmailReceive emailReceive) {
        int count;
        if (StringUtils.isBlank(emailReceive.getId())) {
            emailReceive.setId(IdGenerate.uuid());
            emailReceive.setCreater(UserUtils.getPrincipal().getId());
            count = emailReceiveDao.insert(emailReceive);
        } else {
            count = emailReceiveDao.update(emailReceive);
        }
        return count > 0 ? ResultJson.ok() : ResultJson.failed();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultJson deletes(String... ids) {
        emailReceiveDao.deletes(ids);
        emailFileDao.deleteFiles(ids);
        return ResultJson.ok();
    }

    @Override
    public String getEmailContent(String id) {
        return emailReceiveDao.getEmailContent(id);
    }

    @Override
    public ResultJson update(EmailReceive emailReceive) {
        emailReceiveDao.update(emailReceive);
        return ResultJson.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultJson updateDeletes(String[] ids) {
        emailReceiveDao.updateDeletes(ids);
        return ResultJson.ok();
    }

    @Override
    public ResultJson findSelectedUsers(String emailId) {
        Map<String, List<String>> selectedUsers = new HashMap<>(2);
        EmailReceive emailReceive = emailReceiveDao.findSelectedUsers(emailId);
        List<String> receivers = new ArrayList<>();
        List<String> copys = new ArrayList<>();
        makeSelectedUsers(receivers, emailReceive.getReceiverName().split(";"), emailReceive.getReceiverAccount().split(";"));
        makeSelectedUsers(copys, emailReceive.getCopySenderName().split(";"), emailReceive.getCopySenderAccount().split(";"));
        selectedUsers.put("receivers", receivers);
        selectedUsers.put("copys", copys);
        return ResultJson.ok(selectedUsers);
    }

    @Override
    public ResultJson findReplyusers(String emailId, String type) {
        Map<String, List<String>> selectedUsers = new HashMap<>(2);
        List<String> receivers = new ArrayList<>();
        List<String> copys = new ArrayList<>();
        EmailReceive emailReceive = emailReceiveDao.findSelectedUsers(emailId);
        User user = userDao.get(new User(UserUtils.getPrincipal().getId()));
        String exclude = user.getName() + ";" + user.getEmail();
        if ("reply".equals(type)) {
            makeSelectedUsers(receivers, emailReceive.getSenderName().split(";"), emailReceive.getSenderAccount().split(";"));
        } else {
            makeSelectedUsers(receivers, emailReceive.getReceiverName().split(";"), emailReceive.getReceiverAccount().split(";"));
            receivers.remove(exclude);
            makeSelectedUsers(receivers, emailReceive.getSenderName().split(";"), emailReceive.getSenderAccount().split(";"));
            makeSelectedUsers(copys, emailReceive.getCopySenderName().split(";"), emailReceive.getCopySenderAccount().split(";"));
        }
        copys.remove(exclude);
        selectedUsers.put("receivers", receivers);
        selectedUsers.put("copys", copys);
        return ResultJson.ok(selectedUsers);
    }

    @Override
    public ResultJson getLatestEmail() {
        return ResultJson.ok(emailReceiveDao.getLatestEmail(UserUtils.getPrincipal().getId()));
    }

    private void makeSelectedUsers(List<String> list, String[] names, String[] accounts) {
        EmailSendServiceImpl.buildSelectedUsers(list, names, accounts);
    }
}