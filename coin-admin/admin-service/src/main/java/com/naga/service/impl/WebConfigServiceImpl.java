package com.naga.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.naga.service.WebConfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.naga.mapper.WebConfigMapper;
import com.naga.domain.WebConfig;

@Service
public class WebConfigServiceImpl extends ServiceImpl<WebConfigMapper, WebConfig> implements WebConfigService {

    /**
     * 分页查询
     * @param page
     * @param name
     * @param type
     * @return
     */
    @Override
    public Page<WebConfig> findByPage(Page<WebConfig> page, String name, String type) {
        return page(page, new LambdaQueryWrapper<WebConfig>()
                .like(StringUtils.isNotEmpty(name), WebConfig::getName, name)
                .eq(StringUtils.isNotEmpty(type), WebConfig::getType, type));
    }
}
