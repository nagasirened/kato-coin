package com.naga.service;

import com.naga.domain.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SysMenuService extends IService<SysMenu>{

    /**
     * 根据用户ID，获取菜单信息
     */
    List<SysMenu> getMenusByUserId(Long userId);
}
