package com.naga.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import com.naga.domain.SysMenu;
import com.naga.domain.SysPrivilege;
import com.naga.model.RolePrivilegeParam;
import com.naga.service.SysMenuService;
import com.naga.service.SysPrivilegeService;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.naga.mapper.SysRolePrivilegeMapper;
import com.naga.domain.SysRolePrivilege;
import com.naga.service.SysRolePrivilegeService;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SysRolePrivilegeServiceImpl extends ServiceImpl<SysRolePrivilegeMapper, SysRolePrivilege> implements SysRolePrivilegeService{

    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private SysPrivilegeService sysPrivilegeService;

    @Autowired
    private SysRolePrivilegeService sysRolePrivilegeService;

    @Override
    public List<SysMenu> findSysMenuAndPrivilege(Long roleId) {
        // 页面中显示的是二级菜单和二级菜单的权限
        List<SysMenu> list = sysMenuService.list();
        if (CollectionUtil.isEmpty(list)) {
            return Lists.newArrayList();
        }
        // 获取一级菜单
        List<SysMenu> rootMenus = list.stream()
                .filter(item -> Objects.isNull(item.getParentId()))
                .collect(Collectors.toList());
        if (CollectionUtil.isEmpty(rootMenus)) {
            return Lists.newArrayList();
        }
        // 查询所有的二级菜单
        List<SysMenu> subMenus = new ArrayList<>();
        for (SysMenu rootMenu : rootMenus) {
            subMenus.addAll(getChildMenus(rootMenu.getId(), roleId, list));
        }

        return null;
    }

    /**
     * 查询菜单的子菜单  该方法不好，n的次方了
     * @param parentId
     * @param roleId
     * @param list
     * @return
     */
    private List<SysMenu> getChildMenus(Long parentId, Long roleId, List<SysMenu> list) {
        List<SysMenu> children = new ArrayList<>();
        for (SysMenu item : list) {
            if (item.getParentId() == parentId) {
                children.add(item);
                item.setChildren(getChildMenus(item.getId(), roleId, list));  // 递归调用设置多级菜单
                List<SysPrivilege> privilegeList = sysPrivilegeService.getPrivilegeByMenuIdAndRoleId(item.getId(), roleId);
                item.setPrivileges(privilegeList);
            }
        }
        return children;
    }

    /**
     * 给角色授权权限
     * @param rolePrivilegeParam
     * @return
     */
    @Transactional
    @Override
    public boolean grantPrivileges(RolePrivilegeParam rolePrivilegeParam) {
        Long roleId = rolePrivilegeParam.getRoleId();
        // 先删除之前该角色的权限
        sysRolePrivilegeService.remove(new LambdaQueryWrapper<SysRolePrivilege>()
                        .eq(SysRolePrivilege::getRoleId, roleId));
        // 新增该角色的权限
        List<SysRolePrivilege> rolePrivilegeList = new ArrayList<>();
        for (Long privilegeId : rolePrivilegeParam.getPrivilegeIdList()) {
            SysRolePrivilege sysRolePrivilege = new SysRolePrivilege();
            sysRolePrivilege.setRoleId(roleId);
            sysRolePrivilege.setPrivilegeId(privilegeId);
            rolePrivilegeList.add(sysRolePrivilege);
        }
        sysRolePrivilegeService.saveBatch(rolePrivilegeList);
        return true;
    }
}
