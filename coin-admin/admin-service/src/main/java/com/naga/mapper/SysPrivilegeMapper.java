package com.naga.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.naga.domain.SysPrivilege;

import java.util.List;

public interface SysPrivilegeMapper extends BaseMapper<SysPrivilege> {

    /**
     *
     * @param roleId
     * @return
     */
    List<Long> getPrivilegeByRoleId(Long roleId);

}