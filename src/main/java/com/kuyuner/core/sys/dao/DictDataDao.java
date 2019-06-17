package com.kuyuner.core.sys.dao;

import com.kuyuner.common.persistence.CrudDao;
import com.kuyuner.common.persistence.annotation.MyBatisDao;
import com.kuyuner.core.sys.entity.DictData;

import com.kuyuner.core.sys.entity.DictType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 字典数据Dao层接口
 *
 * @author administrator
 */
@MyBatisDao
public interface DictDataDao extends CrudDao<DictData> {

    /**
     * 根据所属类型ID，修改字段类型
     *
     * @param newDictType
     * @param oldDictType
     * @return
     */
    int updateDictType(@Param("newDictType") String newDictType, @Param("oldDictType") String oldDictType);

    List<DictData> getDictByType(String dictType);
}