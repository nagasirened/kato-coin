package com.naga.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.naga.domain.Coin;
import com.naga.service.CoinService;
import com.naga.mapper.CoinMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 *
 */
@Service
public class CoinServiceImpl extends ServiceImpl<CoinMapper, Coin> implements CoinService{


    /**
     * 数字货币的条件分页查询
     *
     * @param name       数字货币的名称
     * @param type       数字货币类型的名称
     * @param status     数字货币的状态
     * @param title      字货币的标题
     * @param walletType 树字货币的钱包类型名称
     * @param page       分页参数
     * @return 数据货币的分页数据
     */
    @Override
    public Page<Coin> findByPage(String name, String type, Byte status, String title, String walletType, Page<Coin> page) {
        return page(page, new LambdaQueryWrapper<Coin>()
                            .like(StringUtils.isNotEmpty(name), Coin::getName, name)
                            .like(StringUtils.isNotEmpty(title), Coin::getTitle, title)
                            .eq(Objects.nonNull(status), Coin::getStatus, status)
                            .eq(StringUtils.isNotEmpty(type), Coin::getType, type)
                            .eq(StringUtils.isNotEmpty(walletType), Coin::getWallet, walletType));
    }
}




