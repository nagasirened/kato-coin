package com.naga.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.naga.domain.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.naga.dto.SysUserDto;

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
     * @param sysUser   SysUser对象
     * @return          是否新增成功
     */
    boolean addUser(SysUser sysUser);

    /**
     * 修改一个用户
     * @param sysUser       SysUser对象
     * @return              是否修改成功
     */
    boolean updateUser(SysUser sysUser);

    /**
     * 根据管理员用户ID查询管理员基础信息
     * @param userId    管理员用户逐渐
     * @return  管理员用户基础信息SysUserDto
     */
    SysUserDto getSysUserInfo(Long userId);
}
