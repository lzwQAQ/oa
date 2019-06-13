package com.kuyuner.core.sys.service.impl;

import com.kuyuner.common.idgen.IdGenerate;
import com.kuyuner.common.lang.StringUtils;
import com.kuyuner.common.controller.ListJson;
import com.kuyuner.common.persistence.TreeEntity;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.common.persistence.Page;
import com.kuyuner.core.sys.dao.DeptDao;
import com.kuyuner.core.sys.entity.Dept;
import com.kuyuner.core.sys.service.DeptService;
import com.kuyuner.core.sys.security.UserUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 部门Service层实现
 *
 * @author administrator
 */
@Service
public class DeptServiceImpl implements DeptService {

    @Autowired
    private DeptDao deptDao;

    @Override
    public PageJson findPageList(String pageNum, String pageSize, Dept dept) {
        Page<Dept> page = new Page<>(pageNum, pageSize);
        page.start();
        deptDao.findList(dept);
        page.end();
        return new PageJson(page);
    }

    @Override
    public Dept get(String id) {
        return deptDao.get(new Dept(id));
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultJson saveOrUpdate(Dept dept) {
        if (StringUtils.isBlank(dept.getId())) {
            if (StringUtils.isNotBlank(dept.getParentId())) {
                Dept parent = deptDao.get(dept.getParent());
                dept.setTreeLevel(parent.getTreeLevel() + 1);
                if (parent.isLeaf()) {
                    //将父节点更新为非最后一个节点
                    parent.setLeaf(false);
                    deptDao.update(parent);
                }
            } else {
                dept.setTreeLevel(TreeEntity.ROOT_LEVEL);
            }
            dept.setId(IdGenerate.uuid());
            dept.setTreeLeaf(TreeEntity.LAST_LEAF);
            dept.setCreater(UserUtils.getPrincipal().getId());
            deptDao.insert(dept);
        } else {
            Dept oldDept = deptDao.get(new Dept(dept.getId()));
            if (!StringUtils.equals(dept.getParentId(), oldDept.getParentId())) {
                if (StringUtils.isNotBlank(dept.getParentId())) {
                    Dept parent = deptDao.get(dept.getParent());
                    dept.setTreeLevel(parent.getTreeLevel() + 1);
                } else {
                    dept.setParentId("");
                    dept.setTreeLevel(TreeEntity.ROOT_LEVEL);
                }
                deptDao.update(dept);
                updateOldParentLeaf(oldDept.getParentId());
                updateParentLeaf(dept.getParentId());
                updateChildrenLevel(dept.getId(), dept.getTreeLevel());
            } else {
                deptDao.update(dept);
            }

        }
        return ResultJson.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultJson deletes(String... ids) {
        for (String id : ids) {
            Dept dept = deptDao.get(new Dept(id));
            deptDao.deletes(id);
            deleteAllChildren(id);
            updateOldParentLeaf(dept.getParentId());
        }
        return ResultJson.ok();
    }

    /**
     * 更新子节点的等级
     *
     * @param parentId  子节点所属父节点ID
     * @param treeLevel 父节点层级
     */
    private void updateChildrenLevel(String parentId, Integer treeLevel) {
        List<Dept> list = deptDao.findAllChildren(parentId);
        for (Dept oldDept : list) {
            updateChildrenLevel(oldDept.getId(), treeLevel + 1);
            Dept dept = new Dept(oldDept.getId());
            dept.setTreeLevel(treeLevel + 1);
            deptDao.update(dept);
        }
    }

    /**
     * 现有父节点变为非叶子节点
     *
     * @param parentId 父类ID
     */
    private void updateParentLeaf(String parentId) {
        if (StringUtils.isNotBlank(parentId)) {
            Dept dept = new Dept(parentId);
            dept.setLeaf(false);
            deptDao.update(dept);
        }
    }

    /**
     * 更新父节点的叶子状态
     *
     * @param parentId 父类ID
     */
    private void updateOldParentLeaf(String parentId) {
        if (StringUtils.isNotBlank(parentId)) {
            int count = deptDao.findChildrenCount(parentId);
            Dept dept = new Dept(parentId);
            dept.setLeaf(count == 0);
            deptDao.update(dept);
        }
    }

    /**
     * 删除所有子节点
     *
     * @param parentId 父类ID
     */
    private void deleteAllChildren(String parentId) {
        if (StringUtils.isNotBlank(parentId)) {
            List<Dept> list = deptDao.findAllChildren(parentId);
            for (Dept dept : list) {
                deleteAllChildren(dept.getId());
                deptDao.deletes(dept.getId());
            }
        }
    }

    @Override
    public ListJson findList(Dept dept) {
        return new ListJson(deptDao.findList(dept));
    }

    @Override
    public ListJson findDeptTreeList() {
        return new ListJson(deptDao.findDeptTreeList());
    }
}