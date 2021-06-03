package com.naga.service;

import com.naga.domain.SysMenu;
import com.naga.domain.SysRolePrivilege;
import com.baomidou.mybatisplus.extension.service.IService;
import com.naga.model.RolePrivilegeParam;

import java.util.List;

public interface SysRolePrivilegeService extends IService<SysRolePrivilege>{

    /**
     * 查询角色的权限
     * @param roleId
     * @return
     */
    List<SysMenu> findSysMenuAndPrivilege(Long roleId);

    /**
     * 授予权限
     * @param rolePrivilegeParam
     * @return
     */
    boolean grantPrivileges(RolePrivilegeParam rolePrivilegeParam);
}
