package com.kuyuner.bg.work.service.impl;

import com.kuyuner.bg.work.entity.Plan;
import com.kuyuner.common.controller.ListJson;
import com.kuyuner.common.idgen.IdGenerate;
import com.kuyuner.common.lang.StringUtils;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.common.persistence.Page;
import com.kuyuner.bg.work.dao.ReportDao;
import com.kuyuner.bg.work.entity.Report;
import com.kuyuner.bg.work.service.ReportService;
import com.kuyuner.core.sys.security.UserUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 工作日报Service层实现
 *
 * @author administrator
 */
@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportDao reportDao;

    @Override
    public PageJson findPageList(String pageNum, String pageSize, Report report) {
        Page<Report> page = new Page<>(pageNum, pageSize);
        page.start();
        reportDao.findList(report, UserUtils.getUser().getId());
        page.end();
        return new PageJson(page);
    }

    @Override
    public Report get(String id) {
        return reportDao.get(new Report(id));
    }

    @Override
    public ResultJson saveOrUpdate(Report report) {
        int count;
        if (StringUtils.isBlank(report.getId())) {
            report.setId(IdGenerate.uuid());
            report.setCreater(UserUtils.getPrincipal().getId());
            count = reportDao.insert(report);
        } else {
            count = reportDao.update(report);
        }
        return count > 0 ? ResultJson.ok() : ResultJson.failed();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultJson deletes(String... ids) {
        reportDao.deletes(ids);
        return ResultJson.ok();
    }

    @Override
    public PageJson findSendPageList(String pageNum, String pageSize, Report report) {
        Page<Report> page = new Page<>(pageNum, pageSize);
        report.setCreater(UserUtils.getUser().getId());
        page.start();
        reportDao.findSendList(report);
        page.end();
        return new PageJson(page);
    }

    @Override
    public List<Plan> findPlanList() {
        return reportDao.findPlanList(UserUtils.getUser().getId());
    }

    @Override
    public ListJson findPlanChargePeoples(String planId) {
        return new ListJson(reportDao.findPlanChargePeoples(planId));
    }

}