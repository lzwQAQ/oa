package com.kuyuner.core.sys.service;

import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.core.sys.entity.DictType;

/**
 * 字典类型Service层接口
 *
 * @author administrator
 */
public interface DictTypeService {

    /**
     * 分页查询
     *
     * @param pageNum
     * @param pageSize
     * @param dictType
     * @return
     */
    PageJson findPageList(String pageNum, String pageSize, DictType dictType);

    /**
     * 查询唯一
     *
     * @param id
     * @return
     */
    DictType get(String id);

    /**
     * 插入数据
     *
     * @param dictType
     * @return
     */
    ResultJson saveOrUpdate(DictType dictType);

    /**
     * 删除数据
     *
     * @param ids
     */
    ResultJson deletes(String... ids);

}