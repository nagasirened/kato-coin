package com.naga.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.naga.mapper.UserAuthInfoMapper;
import com.naga.domain.UserAuthInfo;
import com.naga.service.UserAuthInfoService;


@Service
public class UserAuthInfoServiceImpl extends ServiceImpl<UserAuthInfoMapper, UserAuthInfo> implements UserAuthInfoService{

    /**
     * 用户未被认证,根据userId查询用户提交的审核信息
     * @param userId 用户主键
     * @return 用户审核信息列表
     */
    @Override
    public List<UserAuthInfo> getUserAuthInfoByUserId(Long userId) {
        List<UserAuthInfo> userAuthInfoList = list(new LambdaQueryWrapper<UserAuthInfo>()
                .eq(UserAuthInfo::getUserId, userId)
                .orderByDesc(UserAuthInfo::getCreated)
                .last("limit 3"));
        return CollectionUtil.isEmpty(userAuthInfoList) ? Collections.emptyList() : userAuthInfoList;
    }

    /**
     * 用户被审核过，根据审核authCode查询用户提交的审核信息
     * @param authCode   认证的唯一code
     * @return List<UserAuthInfo>
     */
    @Override
    public List<UserAuthInfo> getUserAuthInfoByAuthCode(Long authCode) {
        return list(new LambdaQueryWrapper<UserAuthInfo>().eq(UserAuthInfo::getAuthCode, authCode));
    }
}
