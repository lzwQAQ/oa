package com.kuyuner.bg.approval.service.impl;

import com.kuyuner.bg.approval.dao.DriverDao;
import com.kuyuner.bg.approval.entity.Driver;
import com.kuyuner.bg.approval.service.DriverService;
import com.kuyuner.common.controller.ListJson;
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

/**
 * 车辆信息Service层实现
 *
 * @author administrator
 */
@Service
public class DriverServiceImpl implements DriverService {

    @Autowired
    private DriverDao driverDao;

    @Autowired
    private UserDao userDao;

    @Override
    public PageJson findPageList(String pageNum, String pageSize, Driver driver) {
        Page<Driver> page = new Page<>(pageNum, pageSize);
        page.start();
        driverDao.findList(driver);
        page.end();
        return new PageJson(page);
    }

    @Override
    public Driver get(String id) {
        return driverDao.get(new Driver(id));
    }

    @Override
    public ResultJson saveOrUpdate(Driver driver) {
        int count;
        if (StringUtils.isBlank(driver.getId())) {
            driver.setId(IdGenerate.uuid());
            driver.setCreater(UserUtils.getPrincipal().getId());
            count = driverDao.insert(driver);
        } else {
            count = driverDao.update(driver);
        }
        return count > 0 ? ResultJson.ok() : ResultJson.failed();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultJson deletes(String... ids) {
        driverDao.deletes(ids);
        return ResultJson.ok();
    }

    @Override
    public ListJson findAllUsers() {
        return new ListJson(userDao.findList(new User()));
    }

}