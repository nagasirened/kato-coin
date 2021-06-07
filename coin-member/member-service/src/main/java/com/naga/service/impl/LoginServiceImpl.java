package com.naga.service.impl;

import com.alibaba.fastjson.JSON;
import com.naga.feign.JwtToken;
import com.naga.feign.OAuth2FeignClient;
import com.naga.geetest.GeetestLib;
import com.naga.service.LoginService;
import com.naga.vo.LoginForm;
import com.naga.vo.LoginUserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private OAuth2FeignClient oAuth2FeignClient;

    @Value("${basic.token:Basic Y29pbi1hcGk6Y29pbi1zZWNyZXQ=}")
    private String basicToken;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private GeetestLib geetestLib;


    /**
     * 登录请求
     * @param loginForm 登录请求参数
     * @return LoginUserVO
     */
    @Override
    public LoginUserVO login(LoginForm loginForm) {
        log.info("用户{}开始登录, 参数：{}", loginForm.getUsername(), JSON.toJSONString(loginForm));
        checkFormData(loginForm);
        ResponseEntity<JwtToken> jwtTokenResponseEntity = oAuth2FeignClient.getToken("password", loginForm.getUsername(), loginForm.getPassword(), "member_type", basicToken);
        if (jwtTokenResponseEntity.getStatusCode().equals(HttpStatus.OK)) {
            JwtToken jwtToken = jwtTokenResponseEntity.getBody();
            // token 必须包含bearer
            LoginUserVO loginUserVO = LoginUserVO.builder()
                    .accessToken(jwtToken.getTokenType() + " " +jwtToken.getAccessToken())
                    .refreshToken(jwtToken.getRefreshToken())
                    .expire(jwtToken.getExpires_in())
                    .username(loginForm.getUsername()).build();
            // 使用网关解决登出的问题: token 是直接存储的
            redisTemplate.opsForValue().set(jwtToken.getAccessToken(), "", jwtToken.getExpires_in(), TimeUnit.SECONDS);
            return loginUserVO;
        }
        return null;
    }

    /**
     * 校验登录参数
     * @param loginForm
     */
    private void checkFormData(LoginForm loginForm) {
        loginForm.check(loginForm, geetestLib, redisTemplate);
    }
}
