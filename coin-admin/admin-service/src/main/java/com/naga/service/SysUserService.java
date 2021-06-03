package com.naga.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.naga.domain.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
public interface SysUserService extends IService<SysUser>{


    /**
     * 分页查询员工
     * @param page
     * @param mobile
     * @param fullName
     * @return
     */
    Page<SysUser> findByPage(Page<SysUser> page, String mobile, String fullName);

    /**
     * 新增一个用户
     * @param sysUser
     * @return
     */
    boolean addUser(SysUser sysUser);

    /**
     * 修改一个用户
     * @param sysUser
     * @return
     */
    boolean updateUser(SysUser sysUser);
}
