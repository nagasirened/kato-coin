package com.naga.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;

import java.util.Objects;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.naga.domain.UserBank;
import com.naga.mapper.UserBankMapper;


@Service
public class UserBankServiceImpl extends ServiceImpl<UserBankMapper, UserBank> implements UserBankService{

    /**
     * 查询用户的银行卡信息
     *
     * @param page  分页参数
     * @param userId 用户的Id
     * @return
     */
    @Override
    public Page<UserBank> findByPage(Page<UserBank> page, Long userId) {
        return page(page, new LambdaQueryWrapper<UserBank>().eq(Objects.nonNull(userId), UserBank::getUserId, userId));
    }
}
