package com.kuyuner.core.sys.service.impl;

import com.kuyuner.common.controller.ListJson;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.common.idgen.IdGenerate;
import com.kuyuner.common.lang.StringUtils;
import com.kuyuner.common.persistence.Page;
import com.kuyuner.common.security.PasswordUtils;
import com.kuyuner.core.sys.dao.UserDao;
import com.kuyuner.core.sys.entity.User;
import com.kuyuner.core.sys.security.UserUtils;
import com.kuyuner.core.sys.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户Service层实现
 *
 * @author administrator
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public PageJson findPageList(String pageNum, String pageSize, User user) {
        Page<User> page = new Page<>(pageNum, pageSize);
        page.start();
        userDao.findList(user);
        page.end();
        return new PageJson(page);
    }

    @Override
    public User get(String id) {
        return userDao.get(new User(id));
    }

    @Override
    public ResultJson saveOrUpdate(User user) {
        int count;
        if (StringUtils.isBlank(user.getId())) {
            user.setId(IdGenerate.uuid());
            user.setCreater(UserUtils.getPrincipal().getId());
            user.setPassword(PasswordUtils.entryptPassword("123456"));
            count = userDao.insert(user);
        } else {
            count = userDao.update(user);
        }
        return count > 0 ? ResultJson.ok() : ResultJson.failed();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultJson deletes(String... ids) {
        userDao.deletes(ids);
        return ResultJson.ok();
    }

    @Override
    public ResultJson changeMyPassword(String oldPassword, String password) {
        User user = new User(UserUtils.getPrincipal().getId());
        user = userDao.get(user);
        boolean validate = PasswordUtils.validatePassword(oldPassword, user.getPassword());
        if (validate) {
            user.setPassword(PasswordUtils.entryptPassword(password));
            userDao.update(user);
            return ResultJson.ok();
        }
        return ResultJson.failed("原密码错误！");
    }

    @Override
    public ListJson findUserRoles(String userId, String roleName) {
        return new ListJson(userDao.findUserRoles(userId, roleName));
    }

    @Override
    public ListJson findUnSelectedRoles(String userId, String roleName) {
        return new ListJson(userDao.findUnSelectedRoles(userId, roleName));
    }

    @Override
    public ResultJson saveRoles(String userId, String[] roles) {
        userDao.saveRoles(userId, roles);
        return ResultJson.ok();
    }

    @Override
    public ResultJson deleteRoles(String[] ids) {
        userDao.deleteRoles(ids);
        return ResultJson.ok();
    }

}