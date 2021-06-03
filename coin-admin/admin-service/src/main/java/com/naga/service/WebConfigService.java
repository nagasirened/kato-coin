package com.naga.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.naga.domain.WebConfig;
import com.baomidou.mybatisplus.extension.service.IService;
import com.naga.model.R;

public interface WebConfigService extends IService<WebConfig>{

    Page<WebConfig> findByPage(Page<WebConfig> page, String name, String type);

}
