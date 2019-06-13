package com.kuyuner.bg.email.service.impl;

import com.kuyuner.bg.email.bean.EmailView;
import com.kuyuner.bg.email.dao.EmailDao;
import com.kuyuner.bg.email.dao.EmailReceiveDao;
import com.kuyuner.bg.email.dao.EmailSendDao;
import com.kuyuner.bg.email.entity.Email;
import com.kuyuner.bg.email.service.EmailService;
import com.kuyuner.common.controller.ListJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.core.sys.security.UserUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author administrator
 */
@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private EmailDao emailDao;

    @Autowired
    private EmailSendDao emailSendDao;

    @Autowired
    private EmailReceiveDao emailReceiveDao;

    @Override
    public ListJson findStarEmailList(Email email) {
        email.setCreater(UserUtils.getPrincipal().getId());
        List<EmailView> list = emailDao.findStarEmailList(email);
        return new ListJson(list);
    }

    @Override
    public ListJson findRecyclebinList(Email email) {
        email.setCreater(UserUtils.getPrincipal().getId());
        List<EmailView> list = emailDao.findRecyclebinList(email);
        return new ListJson(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultJson deletes(String[] ids) {
        emailSendDao.deletes(ids);
        emailReceiveDao.deletes(ids);
        return ResultJson.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultJson updateDeletes(String[] ids) {
        emailSendDao.updateDeletes(ids);
        emailReceiveDao.updateDeletes(ids);
        return ResultJson.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultJson recoverys(String[] ids) {
        emailSendDao.recoverys(ids);
        emailReceiveDao.recoverys(ids);
        return ResultJson.ok();
    }
}
