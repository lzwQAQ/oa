package com.kuyuner.core.sys.service;

import com.kuyuner.core.sys.entity.User;

/**
 * 登陆Service
 *
 * @author Administrator
 */
public interface LoginService {
    /**
     * 登陆
     *
     * @param username
     * @return
     */
    User login(String username);
}
