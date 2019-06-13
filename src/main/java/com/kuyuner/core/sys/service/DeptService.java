package com.kuyuner.core.sys.service;

import com.kuyuner.common.controller.ListJson;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.core.sys.entity.Dept;

/**
 * 部门Service层接口
 *
 * @author administrator
 */
public interface DeptService {

    /**
     * 分页查询
     *
     * @param pageNum
     * @param pageSize
     * @param dept
     * @return
     */
    PageJson findPageList(String pageNum, String pageSize, Dept dept);

    /**
     * 查询唯一
     *
     * @param id
     * @return
     */
    Dept get(String id);

    /**
     * 插入数据
     *
     * @param dept
     * @return
     */
    ResultJson saveOrUpdate(Dept dept);

    /**
     * 删除数据
     *
     * @param ids
     */
    ResultJson deletes(String... ids);

    /**
     * 查询集合
     *
     * @param dept
     * @return
     */
    ListJson findList(Dept dept);

    /**
     * 查询部门数据
     *
     * @return
     */
    ListJson findDeptTreeList();
}