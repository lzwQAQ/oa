package com.kuyuner.core.sys.service.impl;

import com.kuyuner.common.idgen.IdGenerate;
import com.kuyuner.common.lang.StringUtils;
import com.kuyuner.common.controller.ListJson;
import com.kuyuner.common.persistence.TreeEntity;
import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.common.persistence.Page;
import com.kuyuner.core.sys.dao.MenuDao;
import com.kuyuner.core.sys.entity.Menu;
import com.kuyuner.core.sys.service.MenuService;
import com.kuyuner.core.sys.security.UserUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 菜单Service层实现
 *
 * @author administrator
 */
@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuDao menuDao;

    @Override
    public PageJson findPageList(String pageNum, String pageSize, Menu menu) {
        Page<Menu> page = new Page<>(pageNum, pageSize);
        page.start();
        menuDao.findList(menu);
        page.end();
        return new PageJson(page);
    }

    @Override
    public Menu get(String id) {
        return menuDao.get(new Menu(id));
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultJson saveOrUpdate(Menu menu) {
        if (StringUtils.isBlank(menu.getId())) {
            if (StringUtils.isNotBlank(menu.getParentId())) {
                Menu parent = menuDao.get(menu.getParent());
                menu.setTreeLevel(parent.getTreeLevel() + 1);
                if (parent.isLeaf()) {
                    //将父节点更新为非最后一个节点
                    parent.setLeaf(false);
                    menuDao.update(parent);
                }
            } else {
                menu.setTreeLevel(TreeEntity.ROOT_LEVEL);
            }
            menu.setId(IdGenerate.uuid());
            menu.setTreeLeaf(TreeEntity.LAST_LEAF);
            menu.setCreater(UserUtils.getPrincipal().getId());
            menuDao.insert(menu);
        } else {
            Menu oldMenu = menuDao.get(new Menu(menu.getId()));
            if (!StringUtils.equals(menu.getParentId(), oldMenu.getParentId())) {
                if (StringUtils.isNotBlank(menu.getParentId())) {
                    Menu parent = menuDao.get(menu.getParent());
                    menu.setTreeLevel(parent.getTreeLevel() + 1);
                } else {
                    menu.setParentId("");
                    menu.setTreeLevel(TreeEntity.ROOT_LEVEL);
                }
                menuDao.update(menu);
                updateOldParentLeaf(oldMenu.getParentId());
                updateParentLeaf(menu.getParentId());
                updateChildrenLevel(menu.getId(), menu.getTreeLevel());
            } else {
                menuDao.update(menu);
            }

        }
        return ResultJson.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultJson deletes(String... ids) {
        for (String id : ids) {
            Menu menu = menuDao.get(new Menu(id));
            menuDao.deletes(id);
            deleteAllChildren(id);
            updateOldParentLeaf(menu.getParentId());
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
        List<Menu> list = menuDao.findAllChildren(parentId);
        for (Menu oldMenu : list) {
            updateChildrenLevel(oldMenu.getId(), treeLevel + 1);
            Menu menu = new Menu(oldMenu.getId());
            menu.setTreeLevel(treeLevel + 1);
            menuDao.update(menu);
        }
    }

    /**
     * 现有父节点变为非叶子节点
     *
     * @param parentId 父类ID
     */
    private void updateParentLeaf(String parentId) {
        if (StringUtils.isNotBlank(parentId)) {
            Menu menu = new Menu(parentId);
            menu.setLeaf(false);
            menuDao.update(menu);
        }
    }

    /**
     * 更新父节点的叶子状态
     *
     * @param parentId 父类ID
     */
    private void updateOldParentLeaf(String parentId) {
        if (StringUtils.isNotBlank(parentId)) {
            int count = menuDao.findChildrenCount(parentId);
            Menu menu = new Menu(parentId);
            menu.setLeaf(count == 0);
            menuDao.update(menu);
        }
    }

    /**
     * 删除所有子节点
     *
     * @param parentId 父类ID
     */
    private void deleteAllChildren(String parentId) {
        if (StringUtils.isNotBlank(parentId)) {
            List<Menu> list = menuDao.findAllChildren(parentId);
            for (Menu menu : list) {
                deleteAllChildren(menu.getId());
                menuDao.deletes(menu.getId());
            }
        }
    }

    @Override
    public ListJson findList(Menu menu) {
        return new ListJson(menuDao.findList(menu));
    }

    @Override
    public List<Menu> findAllListBySort(String userId) {
        return menuDao.findAllListBySort(userId);
    }

    @Override
    public ListJson findMenuAll() {
        List<Map<String, Object>> list = menuDao.findMenuAll();
        return new ListJson(list);
    }
}