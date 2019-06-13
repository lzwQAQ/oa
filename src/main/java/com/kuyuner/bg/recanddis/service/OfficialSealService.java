package com.kuyuner.bg.recanddis.service;

import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.bg.recanddis.entity.OfficialSeal;

/**
 * 发文公章Service层接口
 *
 * @author administrator
 */
public interface OfficialSealService {

    /**
     * 分页查询
     *
     * @param pageNum
     * @param pageSize
     * @param officialSeal
     * @return
     */
    PageJson findPageList(String pageNum, String pageSize, OfficialSeal officialSeal);

    /**
     * 查询唯一
     *
     * @param id
     * @return
     */
    OfficialSeal get(String id);

    /**
     * 插入数据
     *
     * @param officialSeal
     * @return
     */
    ResultJson saveOrUpdate(OfficialSeal officialSeal);

    /**
     * 删除数据
     *
     * @param ids
     */
    ResultJson deletes(String... ids);

}