package com.kuyuner.core.sys.dao;

import com.kuyuner.common.persistence.TreeDao;
import com.kuyuner.common.persistence.annotation.MyBatisDao;
import com.kuyuner.core.sys.entity.Menu;

import java.util.List;
import java.util.Map;

/**
 * 菜单Dao层接口
 *
 * @author administrator
 */
@MyBatisDao
public interface MenuDao extends TreeDao<Menu> {

    /**
     * 升序查询数据
     *
     * @param userId
     * @return
     */
    List<Menu> findAllListBySort(String userId);

    /**
     * 查询所有的可选择菜单
     *
     * @return
     */
    List<Map<String, Object>> findMenuAll();
}