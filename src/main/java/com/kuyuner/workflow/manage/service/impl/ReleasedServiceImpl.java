package com.kuyuner.workflow.manage.service.impl;

import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.persistence.Page;
import com.kuyuner.workflow.manage.dao.ReleasedDao;
import com.kuyuner.workflow.manage.service.ReleasedService;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 流程管理-已发布流程
 *
 * @author tangzj
 */
@Service
public class ReleasedServiceImpl implements ReleasedService {

    @Resource
    private ReleasedDao releasedDao;

    @Override
    public PageJson findReleasedWorkFlow(String pageNum, String pageSize, String modelKey, String modelName) {
        Page page = new Page(pageNum, pageSize);
        page.start();
        releasedDao.findReleasedWorkFlow(modelKey, modelName);
        page.end();
        return new PageJson(page);
    }
}
