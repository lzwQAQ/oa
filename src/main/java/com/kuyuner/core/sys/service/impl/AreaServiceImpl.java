package com.kuyuner.core.sys.service.impl;

import com.kuyuner.common.idgen.IdGenerate;
import com.kuyuner.common.lang.StringUtils;
import com.kuyuner.common.controller.ListJson;
import com.kuyuner.common.persistence.TreeEntity;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.common.persistence.Page;
import com.kuyuner.core.sys.dao.AreaDao;
import com.kuyuner.core.sys.entity.Area;
import com.kuyuner.core.sys.service.AreaService;
import com.kuyuner.core.sys.security.UserUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 区划Service层实现
 *
 * @author administrator
 */
@Service
public class AreaServiceImpl implements AreaService {

    @Autowired
    private AreaDao areaDao;

    @Override
    public PageJson findPageList(String pageNum, String pageSize, Area area) {
        Page<Area> page = new Page<>(pageNum, pageSize);
        page.start();
        areaDao.findList(area);
        page.end();
        return new PageJson(page);
    }

    @Override
    public Area get(String id) {
        return areaDao.get(new Area(id));
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultJson saveOrUpdate(Area area) {
        if (StringUtils.isBlank(area.getId())) {
            if (StringUtils.isNotBlank(area.getParentId())) {
                Area parent = areaDao.get(area.getParent());
                area.setTreeLevel(parent.getTreeLevel() + 1);
                if (parent.isLeaf()) {
                    //将父节点更新为非最后一个节点
                    parent.setLeaf(false);
                    areaDao.update(parent);
                }
            } else {
                area.setTreeLevel(TreeEntity.ROOT_LEVEL);
            }
            area.setId(IdGenerate.uuid());
            area.setTreeLeaf(TreeEntity.LAST_LEAF);
            area.setCreater(UserUtils.getPrincipal().getId());
            areaDao.insert(area);
        } else {
            Area oldArea = areaDao.get(new Area(area.getId()));
            if (!StringUtils.equals(area.getParentId(), oldArea.getParentId())) {
                if (StringUtils.isNotBlank(area.getParentId())) {
                    Area parent = areaDao.get(area.getParent());
                    area.setTreeLevel(parent.getTreeLevel() + 1);
                } else {
                    area.setParentId("");
                    area.setTreeLevel(TreeEntity.ROOT_LEVEL);
                }
                areaDao.update(area);
                updateOldParentLeaf(oldArea.getParentId());
                updateParentLeaf(area.getParentId());
                updateChildrenLevel(area.getId(), area.getTreeLevel());
            } else {
                areaDao.update(area);
            }

        }
        return ResultJson.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultJson deletes(String... ids) {
        for (String id : ids) {
            Area area = areaDao.get(new Area(id));
            areaDao.deletes(id);
            deleteAllChildren(id);
            updateOldParentLeaf(area.getParentId());
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
        List<Area> list = areaDao.findAllChildren(parentId);
        for (Area oldArea : list) {
            updateChildrenLevel(oldArea.getId(), treeLevel + 1);
            Area area = new Area(oldArea.getId());
            area.setTreeLevel(treeLevel + 1);
            areaDao.update(area);
        }
    }

    /**
     * 现有父节点变为非叶子节点
     *
     * @param parentId 父类ID
     */
    private void updateParentLeaf(String parentId) {
        if (StringUtils.isNotBlank(parentId)) {
            Area area = new Area(parentId);
            area.setLeaf(false);
            areaDao.update(area);
        }
    }

    /**
     * 更新父节点的叶子状态
     *
     * @param parentId 父类ID
     */
    private void updateOldParentLeaf(String parentId) {
        if (StringUtils.isNotBlank(parentId)) {
            int count = areaDao.findChildrenCount(parentId);
            Area area = new Area(parentId);
            area.setLeaf(count == 0);
            areaDao.update(area);
        }
    }

    /**
     * 删除所有子节点
     *
     * @param parentId 父类ID
     */
    private void deleteAllChildren(String parentId) {
        if (StringUtils.isNotBlank(parentId)) {
            List<Area> list = areaDao.findAllChildren(parentId);
            for (Area area : list) {
                deleteAllChildren(area.getId());
                areaDao.deletes(area.getId());
            }
        }
    }

    @Override
    public ListJson findList(Area area) {
        return new ListJson(areaDao.findList(area));
    }
}