package com.kuyuner.workflow.manage.service;

import com.kuyuner.common.controller.PageJson;

/**
 * 流程管理-已发布流程
 *
 * @author chenxy
 */
public interface ReleasedService {

    /**
     * 查询已发布流程
     *
     * @param pageNum
     * @param pageSize
     * @param modelKey
     * @param modelName
     * @return
     */
    PageJson findReleasedWorkFlow(String pageNum, String pageSize, String modelKey, String modelName);

}
