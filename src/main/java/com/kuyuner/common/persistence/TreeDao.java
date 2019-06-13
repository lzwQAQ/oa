package com.kuyuner.common.persistence;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 通用增删改查
 *
 * @author Administrator
 */
public interface TreeDao<T extends BaseEntity> extends CrudDao<T> {
    /**
     * 查询所有直属子节点
     *
     * @param parentId
     * @return
     */
    List<T> findAllChildren(@Param("parentId") String parentId);

    /**
     * 查询所有直属子节点的数量
     *
     * @param parentId
     * @return
     */
    int findChildrenCount(@Param("parentId") String parentId);
}
