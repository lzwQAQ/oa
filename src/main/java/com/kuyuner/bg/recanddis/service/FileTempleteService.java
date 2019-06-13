package com.kuyuner.bg.recanddis.service;

import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.bg.recanddis.entity.FileTemplete;

/**
 * 发文模板Service层接口
 *
 * @author administrator
 */
public interface FileTempleteService {

    /**
     * 分页查询
     *
     * @param pageNum
     * @param pageSize
     * @param fileTemplete
     * @return
     */
    PageJson findPageList(String pageNum, String pageSize, FileTemplete fileTemplete);

    /**
     * 查询唯一
     *
     * @param id
     * @return
     */
    FileTemplete get(String id);

    /**
     * 插入数据
     *
     * @param fileTemplete
     * @return
     */
    ResultJson saveOrUpdate(FileTemplete fileTemplete);

    /**
     * 删除数据
     *
     * @param ids
     */
    ResultJson deletes(String... ids);

}