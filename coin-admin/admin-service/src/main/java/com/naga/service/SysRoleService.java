package com.naga.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.naga.domain.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;

public interface SysRoleService extends IService<SysRole>{

    /** 判断是不是超级管理员 */
    boolean isSuperAdmin(Long userId);

    /** 分页模糊查询角色信息 */
    Page<SysRole> findByPage(Page<SysRole> page, String name);
}
