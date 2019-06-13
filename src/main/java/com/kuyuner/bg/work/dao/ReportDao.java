package com.kuyuner.bg.work.dao;

import com.kuyuner.bg.work.entity.Plan;
import com.kuyuner.common.persistence.CrudDao;
import com.kuyuner.common.persistence.annotation.MyBatisDao;
import com.kuyuner.bg.work.entity.Report;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 工作日报Dao层接口
 *
 * @author administrator
 */
@MyBatisDao
public interface ReportDao extends CrudDao<Report> {

    /**
     * 批阅日报查询
     *
     * @param report
     * @param userId
     * @return
     */
    List<Report> findList(@Param("report") Report report, @Param("userId") String userId);

    /**
     * 查询自己参与的工作计划
     *
     * @param userId
     * @return
     */
    List<Plan> findPlanList(@Param("userId") String userId);

    /**
     * 已发送日报查询
     *
     * @param report
     * @return
     */
    List<Report> findSendList(Report report);

    /**
     * 查询责任人
     *
     * @param planId
     * @return
     */
    List<String> findPlanChargePeoples(@Param("planId") String planId);
}