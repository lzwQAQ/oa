package com.kuyuner.bg.approval.service;

import com.kuyuner.bg.approval.entity.Car;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;

/**
 * 车辆信息Service层接口
 *
 * @author administrator
 */
public interface CarService {

    /**
     * 分页查询
     *
     * @param pageNum
     * @param pageSize
     * @param car
     * @return
     */
    PageJson findPageList(String pageNum, String pageSize, Car car);

    /**
     * 查询唯一
     *
     * @param id
     * @return
     */
    Car get(String id);

    /**
     * 插入数据
     *
     * @param car
     * @return
     */
    ResultJson saveOrUpdate(Car car);

    /**
     * 删除数据
     *
     * @param ids
     */
    ResultJson deletes(String... ids);

}