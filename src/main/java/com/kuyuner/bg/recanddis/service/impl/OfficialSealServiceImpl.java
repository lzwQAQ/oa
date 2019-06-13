package com.kuyuner.bg.recanddis.service.impl;

import com.kuyuner.common.idgen.IdGenerate;
import com.kuyuner.common.lang.StringUtils;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.common.persistence.Page;
import com.kuyuner.bg.recanddis.dao.OfficialSealDao;
import com.kuyuner.bg.recanddis.entity.OfficialSeal;
import com.kuyuner.bg.recanddis.service.OfficialSealService;
import com.kuyuner.core.sys.security.UserUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 发文公章Service层实现
 *
 * @author administrator
 */
@Service
public class OfficialSealServiceImpl implements OfficialSealService {

    @Autowired
    private OfficialSealDao officialSealDao;

    @Override
    public PageJson findPageList(String pageNum, String pageSize, OfficialSeal officialSeal) {
        Page<OfficialSeal> page = new Page<>(pageNum, pageSize);
        page.start();
        officialSealDao.findList(officialSeal);
        page.end();
        return new PageJson(page);
    }

    @Override
    public OfficialSeal get(String id) {
        return officialSealDao.get(new OfficialSeal(id));
    }

    @Override
    public ResultJson saveOrUpdate(OfficialSeal officialSeal) {
        if (StringUtils.isBlank(officialSeal.getId())) {
            officialSeal.setId(IdGenerate.uuid());
            officialSeal.setCreater(UserUtils.getPrincipal().getId());
            officialSealDao.insert(officialSeal);
        } else {
            officialSealDao.update(officialSeal);
        }
        return ResultJson.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultJson deletes(String... ids) {
        officialSealDao.deletes(ids);
        return ResultJson.ok();
    }

}