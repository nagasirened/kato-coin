package com.naga.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.naga.domain.SysRole;

public interface SysRoleMapper extends BaseMapper<SysRole> {

    /** 根据用户id，查询角色的code */
    String getUserRoleCode(Long userId);
}