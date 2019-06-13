package com.kuyuner.bg.recanddis.service.impl;

import com.kuyuner.bg.recanddis.dao.FileTempleteDao;
import com.kuyuner.bg.recanddis.entity.FileTemplete;
import com.kuyuner.bg.recanddis.service.FileTempleteService;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.common.idgen.IdGenerate;
import com.kuyuner.common.lang.StringUtils;
import com.kuyuner.common.persistence.Page;
import com.kuyuner.core.sys.security.UserUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 发文模板Service层实现
 *
 * @author administrator
 */
@Service
public class FileTempleteServiceImpl implements FileTempleteService {

    @Autowired
    private FileTempleteDao fileTempleteDao;

    @Override
    public PageJson findPageList(String pageNum, String pageSize, FileTemplete fileTemplete) {
        Page<FileTemplete> page = new Page<>(pageNum, pageSize);
        page.start();
        fileTempleteDao.findList(fileTemplete);
        page.end();
        return new PageJson(page);
    }

    @Override
    public FileTemplete get(String id) {
        return fileTempleteDao.get(new FileTemplete(id));
    }

    @Override
    public ResultJson saveOrUpdate(FileTemplete fileTemplete) {
        int count;
        if (StringUtils.isBlank(fileTemplete.getId())) {
            fileTemplete.setId(IdGenerate.uuid());
            fileTemplete.setCreater(UserUtils.getPrincipal().getId());
            count = fileTempleteDao.insert(fileTemplete);
        } else {
            count = fileTempleteDao.update(fileTemplete);
        }
        return count > 0 ? ResultJson.ok() : ResultJson.failed();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultJson deletes(String... ids) {
        fileTempleteDao.deletes(ids);
        return ResultJson.ok();
    }

}