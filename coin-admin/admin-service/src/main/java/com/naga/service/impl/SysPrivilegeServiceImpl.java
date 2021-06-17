package com.naga.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.naga.domain.SysPrivilege;
import com.naga.mapper.SysPrivilegeMapper;

@Service
public class SysPrivilegeServiceImpl extends ServiceImpl<SysPrivilegeMapper, SysPrivilege> implements SysPrivilegeService{

    @Autowired
    private SysPrivilegeMapper sysPrivilegeMapper;

    @Override
    public List<SysPrivilege> getPrivilegeByMenuIdAndRoleId(Long menuId, Long roleId) {
        // 1.查询该菜单下的所有的权限
        LambdaQueryWrapper<SysPrivilege> queryWrapper = new LambdaQueryWrapper<SysPrivilege>().eq(SysPrivilege::getMenuId, menuId);
        List<SysPrivilege> sysPrivileges = list(queryWrapper);
        if (CollectionUtil.isEmpty(sysPrivileges)) {
            return Collections.EMPTY_LIST;
        }
        // 2.当前角色是否包含该权限也设置进去
        for (SysPrivilege sysPrivilege : sysPrivileges) {
            List<Long> rolePrivilegeList = sysPrivilegeMapper.getPrivilegeByRoleId(roleId);
            if (rolePrivilegeList.contains(sysPrivilege.getId())) {
                sysPrivilege.setOwn(1);
            }
        }
        return sysPrivileges;
    }
}
