package com.naga.service.impl;

import com.alibaba.fastjson.JSON;
import com.naga.feign.JwtToken;
import com.naga.feign.OAuth2FeignClient;
import com.naga.geetest.GeetestLib;
import com.naga.geetest.GeetestLibResult;
import com.naga.service.LoginService;
import com.naga.utils.IpUtil;
import com.naga.vo.LoginForm;
import com.naga.vo.LoginUserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
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
        String challenge = loginForm.getGeetestChallenge();
        String validate = loginForm.getGeetestValidate();
        String seccode = loginForm.getGeetestSeccode();
        // session必须取出值，若取不出值，直接当做异常退出
        String statusStr = Optional.ofNullable(String.valueOf(redisTemplate.opsForValue().get(GeetestLib.GEETEST_SERVER_STATUS_SESSION_KEY))).orElse("0");
        int status = Integer.parseInt(statusStr);
        String userId = Optional.ofNullable(String.valueOf(redisTemplate.opsForValue().get(GeetestLib.GEETEST_SERVER_USER_KEY + ":" + loginForm.getUuid()))).orElse("");
        GeetestLibResult result;
        if (status == 1) {
            /*
            自定义参数,可选择添加
                user_id 客户端用户的唯一标识，确定用户的唯一性；作用于提供进阶数据分析服务，可在register和validate接口传入，不传入也不影响验证服务的使用；若担心用户信息风险，可作预处理(如哈希处理)再提供到极验
                client_type 客户端类型，web：电脑上的浏览器；h5：手机上的浏览器，包括移动应用内完全内置的web_view；native：通过原生sdk植入app应用的方式；unknown：未知
                ip_address 客户端请求sdk服务器的ip地址
            */
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put("user_id", userId);
            paramMap.put("client_type", "web");
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            paramMap.put("ip_address", IpUtil.getIpAddr(servletRequestAttributes.getRequest()));
            result = geetestLib.successValidate(challenge, validate, seccode, paramMap);
            log.info("验证的结果为:{}", JSON.toJSONString(result));
        } else {
            result = geetestLib.failValidate(challenge, validate, seccode);
        }
        if(result.getStatus() != 1){
            log.error("验证发生异常，结果如下：{}",JSON.toJSONString(result,true));
            throw new IllegalArgumentException("验证码验证异常") ;
        }
    }
}
