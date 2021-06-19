package com.naga.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.enums.ApiErrorCode;
import com.baomidou.mybatisplus.extension.exceptions.ApiException;
import com.naga.constant.Constants;
import com.naga.domain.SysMenu;
import com.naga.feign.JwtToken;
import com.naga.feign.OAuth2FeignClient;
import com.naga.model.LoginResult;
import com.naga.service.SysLoginService;
import com.naga.service.SysMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SysLoginServiceImpl implements SysLoginService {

    @Autowired
    private OAuth2FeignClient oAuth2FeignClient;

    @Autowired
    private SysMenuService sysMenuService;

    @Value("${basic.token:Basic Y29pbi1hcGk6Y29pbi1zZWNyZXQ=}")
    private String basicToken;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public LoginResult login(String username, String password) {
        // 1.获取token需要远程调用authorization-server 的服务
        ResponseEntity<JwtToken> tokenResponseEntity = oAuth2FeignClient.getToken(Constants.PASSWORD_GRANT_TYPE, username, password, "admin_type", basicToken);
        if (tokenResponseEntity.getStatusCode() != HttpStatus.OK) {
            throw new ApiException(ApiErrorCode.FAILED);
        }
        JwtToken jwtToken = tokenResponseEntity.getBody();
        log.info("SysLoginServiceImpl#jwtToken，用户 {} 获取token成功", username);
        String accessToken = jwtToken.getAccessToken();
        // 2.获取菜单信息
        Jwt jwt = JwtHelper.decode(accessToken);
        JSONObject jwtJson = JSON.parseObject(jwt.getClaims());
        Long userId = Long.valueOf(jwtJson.getString("user_name"));
        List<SysMenu> menus = sysMenuService.getMenusByUserId(userId);
        // 3.获取权限：权限信息在jwtJSON中已经包含了
        JSONArray authoritiesJsonArray = jwtJson.getJSONArray("authorities");
        List<SimpleGrantedAuthority> authorities = authoritiesJsonArray.stream()
                .map(item -> new SimpleGrantedAuthority(item.toString()))
                .collect(Collectors.toList());

        // 🌟🌟🌟 将token存储在redis中，配合网关做JWT登出的功能 🌟🌟🌟
        stringRedisTemplate.opsForValue().set(accessToken, "1", jwtToken.getExpires_in(), TimeUnit.SECONDS);
        // 返回给前端的数据，少一个bearer
        return new LoginResult(jwtToken.getTokenType() + " " + accessToken, menus, authorities);
    }
}
