package com.kuyuner.core.common.dict;

import com.kuyuner.common.cache.EhCacheUtil;
import com.kuyuner.common.lang.StringUtils;
import com.kuyuner.core.sys.dao.DictDataDao;
import com.kuyuner.core.sys.entity.DictData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 字典辅助工具
 *
 * @author administrator
 */
@Component
public class DictHolder {

    @Autowired
    private DictDataDao dictDataDao;

    /**
     * 根据类型查询字典数据
     *
     * @param dictType
     * @return
     */
    public List<DictData> getValues(String dictType) {
        List<DictData> list = (List<DictData>) EhCacheUtil.get(dictType);
        if (list == null) {
            DictData dictData = new DictData();
            dictData.setDictType(dictType);
            list = dictDataDao.findList(dictData);
            EhCacheUtil.put(dictType, list);
        }
        return list;
    }

    /**
     * 根据类型查询字典数据
     *
     * @param dictCode
     * @param dictType
     * @return
     */
    public String getValue(String dictCode, String dictType) {
        if (StringUtils.isBlank(dictCode) || StringUtils.isBlank(dictType)) {
            return "";
        }
        List<DictData> list = getValues(dictType);
        String value = "";
        for (DictData data : list) {
            if (dictCode.equals(data.getDictCode())) {
                value = data.getDictValue();
            }
        }
        return value;
    }

    /**
     * 根据类型删除字典数据
     *
     * @param dictType
     */
    public void removeType(String dictType) {
        EhCacheUtil.remove(dictType);
    }
}
