package com.kuyuner.bg.approval.service.impl;

import com.kuyuner.bg.approval.dao.AccountsDao;
import com.kuyuner.bg.approval.entity.Accounts;
import com.kuyuner.bg.approval.service.AccountsService;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.common.idgen.IdGenerate;
import com.kuyuner.common.persistence.Page;
import com.kuyuner.core.sys.dao.UserDao;
import com.kuyuner.core.sys.entity.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

/**
 * @author admin
 */
@Service
public class AccountsServiceImpl implements AccountsService {

    @Autowired
    private AccountsDao accountsDao;

    @Resource
    private UserDao userDao;

    @Override
    public Accounts get(String id) {
        return accountsDao.get(new Accounts(id));
    }

    @Override
    public PageJson findRecordList(String pageNum, String pageSize, Accounts accounts) {
        Page<Accounts> page = new Page<>(pageNum, pageSize);
        page.start();
        accountsDao.findList(accounts);
        page.end();
        return new PageJson(page);
    }

    @Override
    public List<Accounts> findAllRecordList(Accounts accounts) {
        return accountsDao.findList(accounts);
    }

    @Override
    public ResultJson save(Accounts accounts) {
        accounts.setId(IdGenerate.uuid());
        accounts.setCreateDate(new Date());
        accountsDao.insert(accounts);
        return ResultJson.ok();
    }

    @Override
    public List<User> findUsers() {
        return userDao.findList(new User());
    }
}
