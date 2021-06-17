package com.naga.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.naga.domain.Config;

public interface ConfigService extends IService<Config>{

    /**
     * 条件分页查询
     * @param page  分页条件
     * @param type  类型
     * @param code  规则代码
     * @param name  规则名
     * @return  Page<Config>
     */
    Page<Config> findByPage(Page<Config> page, String type, String code, String name);

    /**
     * 获取配置字段
     * @param sign  配置规则代码
     * @return  Config
     */
    Config getConfigByCode(String sign);
}
