package com.kuyuner.core.sys.service.impl;

import com.kuyuner.common.idgen.IdGenerate;
import com.kuyuner.common.lang.StringUtils;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.core.common.dict.DictHolder;
import com.kuyuner.common.persistence.Page;
import com.kuyuner.core.sys.dao.DictDataDao;
import com.kuyuner.core.sys.dao.DictTypeDao;
import com.kuyuner.core.sys.entity.DictType;
import com.kuyuner.core.sys.service.DictTypeService;
import com.kuyuner.core.sys.security.UserUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 字典类型Service层实现
 *
 * @author administrator
 */
@Service
public class DictTypeServiceImpl implements DictTypeService {

    @Autowired
    private DictTypeDao dictTypeDao;

    @Autowired
    private DictDataDao dictDataDao;

    @Autowired
    private DictHolder dictHolder;

    @Override
    public PageJson findPageList(String pageNum, String pageSize, DictType dictType) {
        Page<DictType> page = new Page<>(pageNum, pageSize);
        page.start();
        dictTypeDao.findList(dictType);
        page.end();
        return new PageJson(page);
    }

    @Override
    public DictType get(String id) {
        return dictTypeDao.get(new DictType(id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultJson saveOrUpdate(DictType dictType) {
        int count;
        if (StringUtils.isBlank(dictType.getId())) {
            dictType.setId(IdGenerate.uuid());
            dictType.setCreater(UserUtils.getPrincipal().getId());
            count = dictTypeDao.insert(dictType);
        } else {
            String oldType = dictTypeDao.get(dictType).getType();
            dictDataDao.updateDictType(dictType.getType(), oldType);
            count = dictTypeDao.update(dictType);
            dictHolder.removeType(oldType);
        }
        return count > 0 ? ResultJson.ok() : ResultJson.failed();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultJson deletes(String... ids) {
        for (String id : ids) {
            String dictType = dictTypeDao.get(new DictType(id)).getType();
            dictTypeDao.deletes(id);
            dictHolder.removeType(dictType);
        }
        return ResultJson.ok();
    }

}