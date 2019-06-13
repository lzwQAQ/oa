package com.kuyuner.common.persistence;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 通用增删改查
 *
 * @author Administrator
 */
public interface CrudDao<T extends BaseEntity> {
    /**
     * 插入数据
     *
     * @param t
     * @return
     */
    int insert(T t);

    /**
     * 更新数据
     *
     * @param t
     * @return
     */
    int update(T t);

    /**
     * 查询集合
     *
     * @param t
     * @return
     */
    List<T> findList(T t);

    /**
     * 删除数据
     *
     * @param ids
     * @return
     */
    int deletes(@Param("ids") String... ids);

    /**
     * 查询单条数据
     *
     * @param t
     * @return
     */
    T get(T t);
}
