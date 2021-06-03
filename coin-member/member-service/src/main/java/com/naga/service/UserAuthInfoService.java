package com.naga.service;

import com.naga.domain.UserAuthInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface UserAuthInfoService extends IService<UserAuthInfo>{

    /**
     * 用户未被认证,根据userId查询用户提交的审核信息
     * @param userId 用户主键
     * @return List<UserAuthInfo>
     */
    List<UserAuthInfo> getUserAuthInfoByUserId(Long userId);

    /**
     * 用户被审核过，根据审核authCode查询用户提交的审核信息
     * @param authCode   认证的唯一code
     * @return List<UserAuthInfo>
     */
    List<UserAuthInfo> getUserAuthInfoByAuthCode(Long authCode);
}
