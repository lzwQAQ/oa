package com.kuyuner.common.persistence;

import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * @author Administrator
 */
public abstract class TreeEntity<T extends TreeEntity> extends BaseEntity {

    /**
     * 根等级
     */
    public static final int ROOT_LEVEL = 1;

    /**
     * 最后一个
     */
    public static final int LAST_LEAF = 0;

    /**
     * 父节点
     */
    public static final int PARENT_LEAF = 1;

    /**
     * 父级编号
     */
    @JsonBackReference
    protected T parent;

    protected Integer treeLevel;

    /**
     * 是否最后一个（0：是；1：否）
     */
    protected Integer treeLeaf;

    /**
     * 获得父类对象
     *
     * @return
     */
    public abstract T getParent();

    /**
     * 设置父类对象
     *
     * @param parent
     */
    public abstract void setParent(T parent);

    public String getParentId() {
        return parent != null ? parent.getId() : null;
    }

    public void setParentId(String parentId) {
        if (parent == null) {
            try {
                parent = (T) getClass().newInstance();
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
            }
        }
        parent.setId(parentId);
    }

    public Integer getTreeLevel() {
        return treeLevel;
    }

    public void setTreeLevel(Integer treeLevel) {
        this.treeLevel = treeLevel;
    }

    public Integer getTreeLeaf() {
        return treeLeaf;
    }

    public void setTreeLeaf(Integer treeLeaf) {
        this.treeLeaf = treeLeaf;
    }

    public boolean isLeaf() {
        if (treeLeaf == null) {
            return true;
        }
        return treeLeaf == LAST_LEAF;
    }

    public void setLeaf(boolean leaf) {
        treeLeaf = leaf ? LAST_LEAF : PARENT_LEAF;
    }

}
