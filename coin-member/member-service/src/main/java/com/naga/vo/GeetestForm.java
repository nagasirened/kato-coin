package com.naga.vo;

import com.alibaba.fastjson.JSON;
import com.naga.geetest.GeetestLib;
import com.naga.geetest.GeetestLibResult;
import com.naga.utils.IpUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Data
@ApiModel
@Slf4j
public class GeetestForm {

    @ApiModelProperty(value = "用户的UUID")
    public String uuid;

    @ApiModelProperty(value = "极验的challenge")
    public String geetestChallenge;

    @ApiModelProperty(value = "极验的validate")
    public String geetestValidate;

    @ApiModelProperty(value = "极验的seccode")
    public String geetestSeccode;

    public void check(GeetestLib geetestLib, RedisTemplate<String, Object> redisTemplate) {
        String challenge = this.getGeetestChallenge();
        String validate = this.getGeetestValidate();
        String seccode = this.getGeetestSeccode();
        // session必须取出值，若取不出值，直接当做异常退出
        String statusStr = Optional.ofNullable(String.valueOf(redisTemplate.opsForValue().get(GeetestLib.GEETEST_SERVER_STATUS_SESSION_KEY))).orElse("0");
        int status = Integer.parseInt(statusStr);
        String userId = Optional.ofNullable(String.valueOf(redisTemplate.opsForValue().get(GeetestLib.GEETEST_SERVER_USER_KEY + ":" + this.getUuid()))).orElse("");
        GeetestLibResult result;
        if (status == 1) {
            /*
            自定义参数,可选择添加
                user_id 客户端用户的唯一标识，确定用户的唯一性；作用于提供进阶数据分析服务，可在register和validate接口传入，不传入也不影响验证服务的使用；若担心用户信息风险，可作预处理(如哈希处理)再提供到极验
                client_type 客户端类型，web：电脑上的浏览器；h5：手机上的浏览器，包括移动应用内完全内置的web_view；native：通过原生sdk植入app应用的方式；unknown：未知
                ip_address 客户端请求sdk服务器的ip地址
            */
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put("user_id", userId);
            paramMap.put("client_type", "web");
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
