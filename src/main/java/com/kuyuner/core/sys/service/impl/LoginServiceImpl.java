package com.kuyuner.core.sys.service.impl;

import com.kuyuner.core.sys.dao.LoginDao;
import com.kuyuner.core.sys.entity.User;
import com.kuyuner.core.sys.service.LoginService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 登陆服务实现接口
 *
 * @author Administrator
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private LoginDao loginDao;

    @Override
    public User login(String username) {
        return loginDao.login(username);
    }
}
