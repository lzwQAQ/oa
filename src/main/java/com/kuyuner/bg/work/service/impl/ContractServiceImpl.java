package com.kuyuner.bg.work.service.impl;

import com.kuyuner.common.idgen.IdGenerate;
import com.kuyuner.common.lang.StringUtils;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.common.persistence.Page;
import com.kuyuner.bg.work.dao.ContractDao;
import com.kuyuner.bg.work.entity.Contract;
import com.kuyuner.bg.work.service.ContractService;
import com.kuyuner.core.sys.security.UserUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 合同管理Service层实现
 *
 * @author administrator
 */
@Service
public class ContractServiceImpl implements ContractService {

    @Autowired
    private ContractDao contractDao;

    @Override
    public PageJson findPageList(String pageNum, String pageSize, Contract contract) {
        Page<Contract> page = new Page<>(pageNum, pageSize);
        page.start();
        contractDao.findList(contract);
        page.end();
        return new PageJson(page);
    }

    @Override
    public Contract get(String id) {
        return contractDao.get(new Contract(id));
    }

    @Override
    public ResultJson saveOrUpdate(Contract contract) {
        int count;
        if (StringUtils.isBlank(contract.getId())) {
            contract.setId(IdGenerate.uuid());
            contract.setCreater(UserUtils.getPrincipal().getId());
            count = contractDao.insert(contract);
        } else {
            count = contractDao.update(contract);
        }
        return count > 0 ? ResultJson.ok() : ResultJson.failed();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultJson deletes(String... ids) {
        contractDao.deletes(ids);
        return ResultJson.ok();
    }

}