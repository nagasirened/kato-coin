package com.naga.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.naga.domain.Config;
import com.naga.mapper.ConfigMapper;
import com.naga.service.ConfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, Config> implements ConfigService {

    @Override
    public Page<Config> findByPage(Page<Config> page, String type, String code, String name) {
        return page(page, new LambdaQueryWrapper<Config>()
                .like(StringUtils.isNotEmpty(type), Config::getType, type)
                .like(StringUtils.isNotEmpty(code), Config::getCode, code)
                .like(StringUtils.isNotEmpty(name), Config::getName, name));
    }

    @Override
    public Config getConfigByCode(String code) {
        return getOne(new LambdaQueryWrapper<Config>().eq(Config::getCode, code));
    }
}
