package com.naga.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.naga.domain.SysUserRole;
import com.naga.dto.SysUserDto;
import com.naga.maps.SysUserDtoMapper;
import com.naga.service.SysUserRoleService;
import com.naga.service.SysUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.naga.mapper.SysUserMapper;
import com.naga.domain.SysUser;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    public static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private SysUserRoleService sysUserRoleService;

    /**
     * 分页查询用户信息
     * @param page
     * @param mobile
     * @param fullName
     * @return
     */
    @Override
    public Page<SysUser> findByPage(Page<SysUser> page, String mobile, String fullName) {
        LambdaQueryWrapper<SysUser> lambdaQueryWrapper = new LambdaQueryWrapper<SysUser>()
                .like(StringUtils.isNotEmpty(mobile), SysUser::getMobile, mobile)
                .like(StringUtils.isNotEmpty(fullName), SysUser::getFullName, fullName);
        Page<SysUser> iPage = page(page, lambdaQueryWrapper);
        if (CollectionUtil.isNotEmpty(iPage.getRecords())) {
            iPage.getRecords().forEach(item -> {
                List<SysUserRole> userRoleList = sysUserRoleService.list(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, item.getId()));
                String roleStrings = userRoleList.stream()
                        .map(temp -> String.valueOf(temp.getRoleId()))
                        .collect(Collectors.joining(","));
                item.setRoleStrings(roleStrings);
            });
        }
        return iPage;
    }

    /**
     * 新增一个用户
     * @param sysUser
     * @return
     */
    @Transactional
    @Override
    public boolean addUser(SysUser sysUser) {
        // 保存用户
        String password = sysUser.getPassword();
        sysUser.setPassword(passwordEncoder.encode(password));
        sysUser.setStatus(Byte.valueOf("1"));
        save(sysUser);
        // 保存用户角色表
        saveUserRoleShip(sysUser.getRoleStrings(), sysUser.getId());
        return true;
    }

    /**
     * 修改一个用户
     * @param sysUser
     * @return
     */
    @Override
    public boolean updateUser(SysUser sysUser) {
        updateById(sysUser);
        // 删除用户角色
        sysUserRoleService.remove(new LambdaQueryWrapper<SysUserRole>()
                .eq(SysUserRole::getUserId, sysUser.getId()));
        // 重新保存用户角色
        saveUserRoleShip(sysUser.getRoleStrings(), sysUser.getId());
        return false;
    }

    /**
     * 根据管理员用户ID查询管理员基础信息
     * @param userId    管理员用户逐渐
     * @return  管理员用户基础信息SysUserDto
     */
    @Override
    public SysUserDto getSysUserInfo(Long userId) {
        SysUser sysUser = this.getById(userId);
        if (Objects.isNull(sysUser)) {
            return null;
        }
        return SysUserDtoMapper.INSTANCE.convert2Dto(sysUser);
    }

    private void saveUserRoleShip(String roleStrings, Long userId) {
        if (StringUtils.isNotEmpty(roleStrings)) {
            String[] roleIds = roleStrings.split(",");
            List<SysUserRole> userRoleList = new ArrayList<>(roleIds.length);
            for (String roleId : roleIds) {
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setUserId(userId);
                sysUserRole.setRoleId(Long.valueOf(roleId));
                userRoleList.add(sysUserRole);
            }
            sysUserRoleService.saveBatch(userRoleList);
        }
    }

    @Transactional
    @Override
    public boolean removeByIds(Collection<? extends Serializable> idList) {
        // 删除SysUser先
        super.removeByIds(idList);
        // 然后删除用户-角色信息
        sysUserRoleService.remove(new LambdaQueryWrapper<SysUserRole>().in(SysUserRole::getUserId, idList));
        return true;
    }
}
