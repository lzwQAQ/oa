package com.kuyuner.core.sys.service;

import com.kuyuner.common.controller.ListJson;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.core.sys.entity.Menu;

import java.util.List;

/**
 * 菜单Service层接口
 *
 * @author administrator
 */
public interface MenuService {

    /**
     * 分页查询
     *
     * @param pageNum
     * @param pageSize
     * @param menu
     * @return
     */
    PageJson findPageList(String pageNum, String pageSize, Menu menu);

    /**
     * 查询唯一
     *
     * @param id
     * @return
     */
    Menu get(String id);

    /**
     * 插入数据
     *
     * @param menu
     * @return
     */
    ResultJson saveOrUpdate(Menu menu);

    /**
     * 删除数据
     *
     * @param ids
     */
    ResultJson deletes(String... ids);

    /**
     * 查询集合
     *
     * @param menu
     * @return
     */
    ListJson findList(Menu menu);

    /**
     * 升序查询数据
     *
     * @param userId
     * @return
     */
    List<Menu> findAllListBySort(String userId);

    /**
     * 显示所有的课选择菜单
     *
     * @return
     */
    ListJson findMenuAll();
}