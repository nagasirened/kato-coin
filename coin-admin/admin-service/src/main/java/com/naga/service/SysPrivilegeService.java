package com.naga.service;

import com.naga.domain.SysPrivilege;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SysPrivilegeService extends IService<SysPrivilege>{


    /**
     * 获取该菜单下的ID
     * @param menuId
     * @param roleId
     */
    List<SysPrivilege> getPrivilegeByMenuIdAndRoleId(Long menuId, Long roleId);
}
