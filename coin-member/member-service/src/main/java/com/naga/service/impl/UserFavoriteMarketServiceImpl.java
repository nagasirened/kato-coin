package com.naga.service.impl;

import com.naga.service.UserFavoriteMarketService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.naga.domain.UserFavoriteMarket;
import com.naga.mapper.UserFavoriteMarketMapper;

@Service
public class UserFavoriteMarketServiceImpl extends ServiceImpl<UserFavoriteMarketMapper, UserFavoriteMarket> implements UserFavoriteMarketService {

}
