package com.naga.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.naga.service.UserWalletService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.naga.mapper.UserWalletMapper;
import com.naga.domain.UserWallet;

@Service
public class UserWalletServiceImpl extends ServiceImpl<UserWalletMapper, UserWallet> implements UserWalletService {

    /**
     * 分页查询用户的提币地址
     *
     * @param page   分页参数
     * @param userId 用户的ID
     * @return
     */
    @Override
    public Page<UserWallet> findByPage(Page<UserWallet> page, Long userId) {
        return page(page, new LambdaQueryWrapper<UserWallet>().eq(UserWallet::getUserId, userId));
    }
}
