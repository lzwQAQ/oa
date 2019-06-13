package com.kuyuner.core.sys.service;

import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.core.sys.entity.DictData;
import com.kuyuner.core.sys.entity.DictType;

import java.util.List;

/**
 * 字典数据Service层接口
 *
 * @author administrator
 */
public interface DictDataService {

    /**
     * 分页查询
     *
     * @param pageNum
     * @param pageSize
     * @param dictData
     * @return
     */
    PageJson findPageList(String pageNum, String pageSize, DictData dictData);

    /**
     * 查询唯一
     *
     * @param id
     * @return
     */
    DictData get(String id);

    /**
     * 插入数据
     *
     * @param dictData
     * @return
     */
    ResultJson saveOrUpdate(DictData dictData);

    /**
     * 删除数据
     *
     * @param ids
     */
    ResultJson deletes(String... ids);

    /**
     * 查询所有字典类型
     *
     * @return
     */
    List<DictType> findAllTypes();
}