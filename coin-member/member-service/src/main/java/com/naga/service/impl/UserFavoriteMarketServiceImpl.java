package com.naga.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.naga.domain.UserFavoriteMarket;
import com.naga.mapper.UserFavoriteMarketMapper;
import com.naga.service.UserFavoriteMarketService;
@Service
public class UserFavoriteMarketServiceImpl extends ServiceImpl<UserFavoriteMarketMapper, UserFavoriteMarket> implements UserFavoriteMarketService{

}
