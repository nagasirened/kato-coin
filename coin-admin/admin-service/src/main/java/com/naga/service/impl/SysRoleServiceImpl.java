package com.naga.service.impl;

import cn.hutool.db.sql.Order;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.naga.mapper.SysRoleMapper;
import com.naga.domain.SysRole;
import com.naga.service.SysRoleService;
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService{

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Override
    public boolean isSuperAdmin(Long userId) {
        String roleCode = sysRoleMapper.getUserRoleCode(userId);
        if (StringUtils.equals(roleCode, "ADMIN_ROLE")) {
            return true;
        }
        return false;
    }

    @Override
    public Page<SysRole> findByPage(Page<SysRole> page, String name) {
        page.addOrder(OrderItem.desc("last_update_time"));
        return page(page, new LambdaQueryWrapper<SysRole>().like(StringUtils.isNotEmpty(name), SysRole::getName, name));
    }
}
