package com.kuyuner.bg.approval.service.impl;

import com.kuyuner.bg.approval.dao.CarDao;
import com.kuyuner.bg.approval.entity.Car;
import com.kuyuner.bg.approval.service.CarService;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.common.idgen.IdGenerate;
import com.kuyuner.common.lang.StringUtils;
import com.kuyuner.common.persistence.Page;
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
public class CarServiceImpl implements CarService {

    @Autowired
    private CarDao carDao;

    @Override
    public PageJson findPageList(String pageNum, String pageSize, Car car) {
        Page<Car> page = new Page<>(pageNum, pageSize);
        page.start();
        carDao.findList(car);
        page.end();
        return new PageJson(page);
    }

    @Override
    public Car get(String id) {
        return carDao.get(new Car(id));
    }

    @Override
    public ResultJson saveOrUpdate(Car car) {
        int count;
        if (StringUtils.isBlank(car.getId())) {
            car.setId(IdGenerate.uuid());
            car.setCreater(UserUtils.getPrincipal().getId());
            count = carDao.insert(car);
        } else {
            count = carDao.update(car);
        }
        return count > 0 ? ResultJson.ok() : ResultJson.failed();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultJson deletes(String... ids) {
        carDao.deletes(ids);
        return ResultJson.ok();
    }

}