package com.naga.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.naga.domain.AdminBank;
import com.baomidou.mybatisplus.extension.service.IService;

public interface AdminBankService extends IService<AdminBank> {

    /**
     * 分页查询
     * @param page
     * @param bankCard
     * @return
     */
    Page<AdminBank> findByPage(Page<AdminBank> page, String bankCard);
}

