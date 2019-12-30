package com.kuyuner.bg.approval.service;

import com.kuyuner.bg.approval.entity.Driver;
import com.kuyuner.common.controller.ListJson;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;

/**
 * 车辆信息Service层接口
 *
 * @author administrator
 */
public interface DriverService {

    /**
     * 分页查询
     *
     * @param pageNum
     * @param pageSize
     * @param driver
     * @return
     */
    PageJson findPageList(String pageNum, String pageSize, Driver driver);

    /**
     * 查询唯一
     *
     * @param id
     * @return
     */
    Driver get(String id);

    /**
     * 插入数据
     *
     * @param driver
     * @return
     */
    ResultJson saveOrUpdate(Driver driver);

    /**
     * 删除数据
     *
     * @param ids
     */
    ResultJson deletes(String... ids);

    /**
     * 查询所有的用户
     *
     * @return
     */
    ListJson findAllUsers();

    /**
     * 查询姓名是否重复
     * @param id
     * @param name
     * @return
     */
    boolean checkName(String id, String name);

    /**
     * 查询手机号码是否重复
     * @param id
     * @param phone
     * @return
     */
    boolean checkPhone(String id, String phone);
}