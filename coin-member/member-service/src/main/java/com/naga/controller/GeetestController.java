package com.naga.controller;


import com.naga.geetest.GeetestLib;
import com.naga.geetest.GeetestLibResult;
import com.naga.model.R;
import com.naga.utils.IpUtil;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashMap;
import java.util.Map;

@RestController
@Api(tags = "极域验证的数据接口")
@RequestMapping("/gt")
public class GeetestController {

    @Autowired
    private GeetestLib geetestLib;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @GetMapping("/register")
    public R<String> gtRegister(String uuid) {
        String digestmod = "md5";

        // 构造极验数据包,四个参数是示例要求的
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("digestmod", digestmod);
        paramMap.put("user_id", uuid);
        paramMap.put("client_type", "web");
        paramMap.put("ip_address", IpUtil.getIpAddr(servletRequestAttributes.getRequest()));
        GeetestLibResult registerResult = geetestLib.register(digestmod, paramMap);
        redisTemplate.opsForValue().set(GeetestLib.GEETEST_SERVER_STATUS_SESSION_KEY + ":" + uuid, registerResult.getStatus());
        redisTemplate.opsForValue().set(GeetestLib.GEETEST_SERVER_USER_KEY + ":" + uuid, uuid);
        return R.ok(registerResult.getData());
    }
}
