package com.kuyuner.core.sys.service.impl;

import com.kuyuner.common.idgen.IdGenerate;
import com.kuyuner.common.lang.StringUtils;
import com.kuyuner.common.controller.ListJson;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.common.persistence.Page;
import com.kuyuner.common.persistence.TreeEntity;
import com.kuyuner.core.sys.dao.OrgDao;
import com.kuyuner.core.sys.entity.Org;
import com.kuyuner.core.sys.service.OrgService;
import com.kuyuner.core.sys.security.UserUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Administrator
 */
@Service
public class OrgServiceImpl implements OrgService {

    @Autowired
    private OrgDao orgDao;

    @Override
    public PageJson findPageList(String pageNum, String pageSize, Org org) {
        Page page = new Page(pageNum, pageSize);
        page.start();
        orgDao.findList(org);
        page.end();
        return new PageJson(page);
    }

    @Override
    public Org get(Org org) {
        return orgDao.get(org);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletes(String... ids) {
        for (String id : ids) {
            Org org = orgDao.get(new Org(id));
            orgDao.deletes(id);
            deleteAllChildren(id);
            updateOldParentLeaf(org.getParentId());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultJson saveOrUpdate(Org org) {
        if (StringUtils.isBlank(org.getId())) {
            if (StringUtils.isNotBlank(org.getParentId())) {
                Org parent = orgDao.get(org.getParent());
                org.setTreeLevel(parent.getTreeLevel() + 1);
                if (parent.isLeaf()) {
                    //将父节点更新为非最后一个节点
                    parent.setLeaf(false);
                    orgDao.update(parent);
                }
            } else {
                org.setTreeLevel(TreeEntity.ROOT_LEVEL);
            }
            org.setId(IdGenerate.uuid());
            org.setTreeLeaf(TreeEntity.LAST_LEAF);
            org.setCreater(UserUtils.getPrincipal().getId());
            orgDao.insert(org);
        } else {
            Org oldOrg = orgDao.get(new Org(org.getId()));
            if (!StringUtils.equals(org.getParentId(), oldOrg.getParentId())) {
                if (StringUtils.isNotBlank(org.getParentId())) {
                    Org parent = orgDao.get(org.getParent());
                    org.setTreeLevel(parent.getTreeLevel() + 1);
                } else {
                    org.setParentId("");
                    org.setTreeLevel(TreeEntity.ROOT_LEVEL);
                }
                orgDao.update(org);
                updateOldParentLeaf(oldOrg.getParentId());
                updateParentLeaf(org.getParentId());
                updateChildrenLevel(org.getId(), org.getTreeLevel());
            } else {
                orgDao.update(org);
            }

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
        List<Org> list = orgDao.findAllChildren(parentId);
        for (Org oldOrg : list) {
            updateChildrenLevel(oldOrg.getId(), treeLevel + 1);
            Org org = new Org(oldOrg.getId());
            org.setTreeLevel(treeLevel + 1);
            orgDao.update(org);
        }
    }

    /**
     * 现有父节点变为非叶子节点
     *
     * @param parentId
     */
    private void updateParentLeaf(String parentId) {
        if (StringUtils.isNotBlank(parentId)) {
            Org org = new Org(parentId);
            org.setLeaf(false);
            orgDao.update(org);
        }
    }

    /**
     * 更新父节点的叶子状态
     *
     * @param parentId
     */
    private void updateOldParentLeaf(String parentId) {
        if (StringUtils.isNotBlank(parentId)) {
            int count = orgDao.findChildrenCount(parentId);
            Org org = new Org(parentId);
            org.setLeaf(count == 0);
            orgDao.update(org);
        }
    }

    /**
     * 删除所有子节点
     *
     * @param parentId
     */
    private void deleteAllChildren(String parentId) {
        if (StringUtils.isNotBlank(parentId)) {
            List<Org> list = orgDao.findAllChildren(parentId);
            for (Org org : list) {
                deleteAllChildren(org.getId());
                orgDao.deletes(org.getId());
            }
        }
    }

    @Override
    public ListJson findList(Org org) {
        return new ListJson(orgDao.findList(org));
    }

}
