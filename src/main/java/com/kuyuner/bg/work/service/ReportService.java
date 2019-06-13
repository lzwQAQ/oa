package com.kuyuner.bg.work.service;

import com.kuyuner.bg.work.entity.Plan;
import com.kuyuner.common.controller.ListJson;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.bg.work.entity.Report;

import java.util.List;

/**
 * 工作日报Service层接口
 *
 * @author administrator
 */
public interface ReportService {

    /**
     * 分页查询
     *
     * @param pageNum
     * @param pageSize
     * @param report
     * @return
     */
    PageJson findPageList(String pageNum, String pageSize, Report report);

    /**
     * 查询唯一
     *
     * @param id
     * @return
     */
    Report get(String id);

    /**
     * 插入数据
     *
     * @param report
     * @return
     */
    ResultJson saveOrUpdate(Report report);

    /**
     * 删除数据
     *
     * @param ids
     */
    ResultJson deletes(String... ids);

    /**
     * 查询编写的日报
     *
     * @param pageNum
     * @param pageSize
     * @param report
     * @return
     */
    PageJson findSendPageList(String pageNum, String pageSize, Report report);

    /**
     * 查询参与的所有任务
     *
     * @return
     */
    List<Plan> findPlanList();

    /**
     * 查询责任人
     *
     * @param planId
     * @return
     */
    ListJson findPlanChargePeoples(String planId);
}