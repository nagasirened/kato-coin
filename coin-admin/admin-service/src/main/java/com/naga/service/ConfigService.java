package com.naga.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.naga.domain.Config;
import com.baomidou.mybatisplus.extension.service.IService;
public interface ConfigService extends IService<Config>{

    /**
     * 条件分页查询
     * @param page
     * @param type
     * @param code
     * @param name
     * @return
     */
    Page<Config> findByPage(Page<Config> page, String type, String code, String name);
}
