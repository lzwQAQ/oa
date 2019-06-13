package com.kuyuner.bg.work.service;

import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.bg.work.entity.Contract;

/**
 * 合同管理Service层接口
 *
 * @author administrator
 */
public interface ContractService {

    /**
     * 分页查询
     *
     * @param pageNum
     * @param pageSize
     * @param contract
     * @return
     */
    PageJson findPageList(String pageNum, String pageSize, Contract contract);

    /**
     * 查询唯一
     *
     * @param id
     * @return
     */
    Contract get(String id);

    /**
     * 插入数据
     *
     * @param contract
     * @return
     */
    ResultJson saveOrUpdate(Contract contract);

    /**
     * 删除数据
     *
     * @param ids
     */
    ResultJson deletes(String... ids);

}