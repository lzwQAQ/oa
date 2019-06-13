package com.kuyuner.core.sys.service.impl;

import com.kuyuner.common.controller.PageJson;
import com.kuyuner.common.controller.ResultJson;
import com.kuyuner.common.idgen.IdGenerate;
import com.kuyuner.common.lang.StringUtils;
import com.kuyuner.common.persistence.Page;
import com.kuyuner.core.sys.dao.RoleDao;
import com.kuyuner.core.sys.entity.Role;
import com.kuyuner.core.sys.security.UserUtils;
import com.kuyuner.core.sys.service.RoleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 角色Service层实现
 *
 * @author administrator
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;

    @Override
    public PageJson findPageList(String pageNum, String pageSize, Role role) {
        Page<Role> page = new Page<>(pageNum, pageSize);
        page.start();
        roleDao.findList(role);
        page.end();
        return new PageJson(page);
    }

    @Override
    public Role get(String id) {
        return roleDao.get(new Role(id));
    }

    @Override
    public ResultJson saveOrUpdate(Role role) {
        int count;
        if (StringUtils.isBlank(role.getId())) {
            role.setId(IdGenerate.uuid());
            role.setCreater(UserUtils.getPrincipal().getId());
            count = roleDao.insert(role);
        } else {
            count = roleDao.update(role);
        }
        return count > 0 ? ResultJson.ok() : ResultJson.failed();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultJson deletes(String... ids) {
        roleDao.deletes(ids);
        return ResultJson.ok();
    }

    @Override
    public ResultJson findAllPermissions(String roleId) {
        List<Map<String, Object>> menus = roleDao.findAllMenus();
        List<Map<String, Object>> list = roleDao.findAllPermissions(roleId);
        Map<String, Object> map = new HashMap<>(2);
        map.put("menus", menus);
        map.put("permissions", list);
        return ResultJson.ok(map);
    }

    @Override
    @Transactional
    public ResultJson savePermissions(String roleId, String[] split) {
        roleDao.deleteByRoleId(roleId);
        roleDao.savePermissions(roleId, split);
        return ResultJson.ok();
    }

}