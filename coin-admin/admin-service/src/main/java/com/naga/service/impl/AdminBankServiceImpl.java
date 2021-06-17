package com.naga.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.naga.domain.AdminBank;
import com.naga.mapper.AdminBankMapper;

@Service
public class AdminBankServiceImpl extends ServiceImpl<AdminBankMapper, AdminBank> implements AdminBankService {

    /**
     * 分页查询公司银行卡
     * @param page
     * @param bankCard
     * @return
     */
    @Override
    public Page<AdminBank> findByPage(Page<AdminBank> page, String bankCard) {
        return page(page, new LambdaQueryWrapper<AdminBank>()
                            .like(StringUtils.isNotEmpty(bankCard), AdminBank::getBankCard, bankCard));
    }
}

