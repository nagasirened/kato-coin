package com.naga.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.naga.mapper.SysMenuMapper;
import com.naga.domain.SysMenu;

@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService{

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Autowired
    private SysRoleService sysRoleService;

    @Override
    public List<SysMenu> getMenusByUserId(Long userId) {
        // 如果用户是超级管理员，就获取所有的菜单  否则如果该用户不是超级管理员，就根据角色管理菜单
        if (sysRoleService.isSuperAdmin(userId)) {
            return sysMenuMapper.getAllMenus();
        }
        return sysMenuMapper.getMenusByUserId(userId);
    }
}
