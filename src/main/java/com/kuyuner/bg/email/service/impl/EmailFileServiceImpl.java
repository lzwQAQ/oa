package com.kuyuner.bg.email.service.impl;

import com.kuyuner.common.controller.ListJson;
import com.kuyuner.common.idgen.IdGenerate;
import com.kuyuner.common.lang.StringUtils;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.common.persistence.Page;
import com.kuyuner.bg.email.dao.EmailFileDao;
import com.kuyuner.bg.email.entity.EmailFile;
import com.kuyuner.bg.email.service.EmailFileService;
import com.kuyuner.core.sys.security.UserUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 邮箱复件Service层实现
 *
 * @author administrator
 */
@Service
public class EmailFileServiceImpl implements EmailFileService {

    @Autowired
    private EmailFileDao emailFileDao;

    @Override
    public PageJson findPageList(String pageNum, String pageSize, EmailFile emailFile) {
        Page<EmailFile> page = new Page<>(pageNum, pageSize);
        page.start();
        emailFileDao.findList(emailFile);
        page.end();
        return new PageJson(page);
    }

    @Override
    public EmailFile get(String id) {
        return emailFileDao.get(new EmailFile(id));
    }

    @Override
    public ResultJson saveOrUpdate(EmailFile emailFile) {
        int count;
        if (StringUtils.isBlank(emailFile.getId())) {
            emailFile.setId(IdGenerate.uuid());
            emailFile.setCreater(UserUtils.getPrincipal().getId());
            count = emailFileDao.insert(emailFile);
        } else {
            count = emailFileDao.update(emailFile);
        }
        return count > 0 ? ResultJson.ok() : ResultJson.failed();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultJson deletes(String... ids) {
        emailFileDao.deletes(ids);
        return ResultJson.ok();
    }

    @Override
    public ListJson findEmailFiles(String emailId) {
        EmailFile emailFile = new EmailFile();
        emailFile.setEmailId(emailId);
        return new ListJson(emailFileDao.findList(emailFile));
    }

}