package com.kuyuner.bg.work.service;

import com.kuyuner.common.controller.ListJson;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.bg.work.entity.Plan;
import com.kuyuner.core.sys.entity.User;

import java.util.List;
import java.util.Map;

/**
 * 任务计划Service层接口
 *
 * @author administrator
 */
public interface PlanService {

    /**
     * 分页查询
     *
     * @param pageNum
     * @param pageSize
     * @param plan
     * @return
     */
    PageJson findPageList(String pageNum, String pageSize, Plan plan,String userId);

    /**
     * 查询唯一
     *
     * @param id
     * @return
     */
    Plan get(String id);

    /**
     * 插入数据
     *
     * @param plan
     * @param joinPeople
     * @param chargePeoples
     * @return
     */
    ResultJson saveOrUpdate(Plan plan, String joinPeople, String chargePeoples,String userId);

    /**
     * 删除数据
     *
     * @param ids
     */
    ResultJson deletes(String... ids);

    /**
     * 查询发布的任务列表
     *
     * @param pageNum
     * @param pageSize
     * @param plan
     * @return
     */
    PageJson findSendPageList(String pageNum, String pageSize, Plan plan,String userId);

    /**
     * 查询所有的用户
     *
     * @return
     */
    List<User> findAllUsers();

    /**
     * 查询责任人
     *
     * @param planId
     * @return
     */
    ListJson findChargePeoples(String planId);

    /**
     * 查询参与人员
     *
     * @param planId
     * @return
     */
    ListJson findJoinPeoples(String planId);

    /**
     * 查询所有的任务相关人员
     *
     * @param id
     * @return
     */
    List<Map<String, Object>> findPeopleList(String id);

    PageJson applist(String pageNum, String pageSize, Plan plan,String userId);
}