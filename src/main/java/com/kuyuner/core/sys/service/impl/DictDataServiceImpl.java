package com.kuyuner.core.sys.service.impl;

import com.kuyuner.common.idgen.IdGenerate;
import com.kuyuner.common.lang.StringUtils;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.core.common.dict.DictHolder;
import com.kuyuner.common.persistence.Page;
import com.kuyuner.core.sys.dao.DictDataDao;
import com.kuyuner.core.sys.dao.DictTypeDao;
import com.kuyuner.core.sys.entity.DictData;
import com.kuyuner.core.sys.entity.DictType;
import com.kuyuner.core.sys.service.DictDataService;
import com.kuyuner.core.sys.security.UserUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 字典数据Service层实现
 *
 * @author administrator
 */
@Service
public class DictDataServiceImpl implements DictDataService {

    @Autowired
    private DictDataDao dictDataDao;

    @Autowired
    private DictTypeDao dictTypeDao;

    @Autowired
    private DictHolder dictHolder;

    @Override
    public PageJson findPageList(String pageNum, String pageSize, DictData dictData) {
        Page<DictData> page = new Page<>(pageNum, pageSize);
        page.start();
        dictDataDao.findList(dictData);
        page.end();
        return new PageJson(page);
    }

    @Override
    public DictData get(String id) {
        return dictDataDao.get(new DictData(id));
    }

    @Override
    public ResultJson saveOrUpdate(DictData dictData) {
        int count;
        if (StringUtils.isBlank(dictData.getId())) {
            dictData.setId(IdGenerate.uuid());
            dictData.setCreater(UserUtils.getPrincipal().getId());
            count = dictDataDao.insert(dictData);
        } else {
            String oldType = dictDataDao.get(dictData).getDictType();
            count = dictDataDao.update(dictData);
            dictHolder.removeType(oldType);
            dictHolder.removeType(dictData.getDictType());
        }
        return count > 0 ? ResultJson.ok() : ResultJson.failed();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultJson deletes(String... ids) {
        for (String id : ids) {
            String dictType = dictDataDao.get(new DictData(id)).getDictType();
            dictDataDao.deletes(id);
            dictHolder.removeType(dictType);
        }
        return ResultJson.ok();
    }

    @Override
    public List<DictType> findAllTypes() {
        return dictTypeDao.findList(new DictType());
    }

    @Override
    public List<DictData> getDictByType(String dictType) {
        return dictDataDao.getDictByType(dictType);
    }

}