package com.kuyuner.bg.work.service.impl;

import com.kuyuner.common.controller.ListJson;
import com.kuyuner.common.idgen.IdGenerate;
import com.kuyuner.common.lang.StringUtils;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.common.persistence.Page;
import com.kuyuner.bg.work.dao.PlanDao;
import com.kuyuner.bg.work.entity.Plan;
import com.kuyuner.bg.work.service.PlanService;
import com.kuyuner.core.sys.entity.User;
import com.kuyuner.core.sys.security.UserUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 任务计划Service层实现
 *
 * @author administrator
 */
@Service
public class PlanServiceImpl implements PlanService {

    @Autowired
    private PlanDao planDao;

    @Override
    public PageJson findPageList(String pageNum, String pageSize, Plan plan) {
        Page<Plan> page = new Page<>(pageNum, pageSize);
        page.start();
        planDao.findPersonalList(plan, UserUtils.getUser().getId());
        page.end();
        return new PageJson(page);
    }

    @Override
    public Plan get(String id) {
        return planDao.get(new Plan(id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultJson saveOrUpdate(Plan plan, String joinPeople, String chargePeoples) {
        if (StringUtils.isBlank(plan.getId())) {
            plan.setId(IdGenerate.uuid());
            plan.setCreater(UserUtils.getPrincipal().getId());
            planDao.insert(plan);
        } else {
            planDao.update(plan);
        }
        updatePeoples(plan.getId(), joinPeople, chargePeoples);
        return ResultJson.ok();
    }

    private void updatePeoples(String id, String joinPeople, String chargePeoples) {
        planDao.deleteByPlan(id);
        planDao.insertPeoples(id, chargePeoples.split(","), "1");
        planDao.insertPeoples(id, joinPeople.split(","), "2");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultJson deletes(String... ids) {
        planDao.deletes(ids);
        return ResultJson.ok();
    }

    @Override
    public PageJson findSendPageList(String pageNum, String pageSize, Plan plan) {
        plan.setCreater(UserUtils.getUser().getId());
        Page<Plan> page = new Page<>(pageNum, pageSize);
        page.start();
        planDao.findList(plan);
        page.end();
        return new PageJson(page);
    }

    @Override
    public List<User> findAllUsers() {
        return planDao.findAllUsers();
    }

    @Override
    public ListJson findChargePeoples(String planId) {
        return new ListJson(planDao.findPeoples(planId, "1"));
    }

    @Override
    public ListJson findJoinPeoples(String planId) {
        return new ListJson(planDao.findPeoples(planId, "2"));
    }

    @Override
    public List<Map<String, Object>> findPeopleList(String id) {
        return planDao.findPeoples(id, null);
    }

}