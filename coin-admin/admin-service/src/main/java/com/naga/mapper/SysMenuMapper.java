package com.naga.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.naga.domain.SysMenu;

import java.util.List;

public interface SysMenuMapper extends BaseMapper<SysMenu> {

    List<SysMenu> getMenusByUserId(Long userId);

    List<SysMenu> getAllMenus();
}