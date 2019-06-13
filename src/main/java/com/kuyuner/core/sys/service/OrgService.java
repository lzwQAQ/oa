package com.kuyuner.core.sys.service;

import com.kuyuner.common.controller.ListJson;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.core.sys.entity.Org;

/**
 * @author Administrator
 */
public interface OrgService {

    /**
     * 查询唯一
     *
     * @param org
     * @return
     */
    Org get(Org org);

    /**
     * 插入数据
     *
     * @param org
     * @return
     */
    ResultJson saveOrUpdate(Org org);

    /**
     * 删除数据
     *
     * @param ids
     */
    void deletes(String... ids);

    /**
     * 分页查询
     *
     * @param pageNum
     * @param pageSize
     * @param org
     * @return
     */
    PageJson findPageList(String pageNum, String pageSize, Org org);

    /**
     * 查询集合
     *
     * @param org
     * @return
     */
    ListJson findList(Org org);

}