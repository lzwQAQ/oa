package com.kuyuner.core.sys.service;

import com.kuyuner.common.controller.ListJson;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.core.sys.entity.Area;

/**
 * 区划Service层接口
 *
 * @author administrator
 */
public interface AreaService {

    /**
     * 分页查询
     *
     * @param pageNum
     * @param pageSize
     * @param area
     * @return
     */
    PageJson findPageList(String pageNum, String pageSize, Area area);

    /**
     * 查询唯一
     *
     * @param id
     * @return
     */
    Area get(String id);

    /**
     * 插入数据
     *
     * @param area
     * @return
     */
    ResultJson saveOrUpdate(Area area);

    /**
     * 删除数据
     *
     * @param ids
     */
    ResultJson deletes(String... ids);

    /**
     * 查询集合
     *
     * @param area
     * @return
     */
    ListJson findList(Area area);
}