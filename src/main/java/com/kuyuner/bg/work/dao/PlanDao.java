package com.kuyuner.bg.work.dao;

import com.kuyuner.common.persistence.CrudDao;
import com.kuyuner.common.persistence.annotation.MyBatisDao;
import com.kuyuner.bg.work.entity.Plan;
import com.kuyuner.core.sys.entity.User;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 任务计划Dao层接口
 *
 * @author administrator
 */
@MyBatisDao
public interface PlanDao extends CrudDao<Plan> {

    /**
     * 查询所有的用户
     *
     * @return
     */
    List<User> findAllUsers();

    /**
     * 根据计划删除人
     *
     * @param planId
     * @return
     */
    int deleteByPlan(@Param("planId") String planId);

    /**
     * 插入参与人员
     *
     * @param planId
     * @param peoples
     * @param type
     * @return
     */
    int insertPeoples(@Param("planId") String planId, @Param("peoples") String[] peoples, @Param("type") String type);

    /**
     * 查询个人任务
     *
     * @param plan
     * @return
     */
    List<Plan> findPersonalList(@Param("plan") Plan plan, @Param("userId") String userId);

    /**
     * 查询相关人员
     *
     * @param planId
     * @param type
     * @return
     */
    List<Map<String, Object>> findPeoples(@Param("planId") String planId, @Param("type") String type);

    List<Plan> applist(@Param("plan") Plan plan);
}