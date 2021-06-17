package com.naga.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.alicloud.sms.ISmsService;
import com.alibaba.fastjson.JSON;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.naga.domain.Config;
import com.naga.service.ConfigService;
import com.naga.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.naga.domain.Sms;
import com.naga.mapper.SmsMapper;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class SmsServiceImpl extends ServiceImpl<SmsMapper, Sms> implements SmsService {

    @Autowired
    private ConfigService configService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    @SuppressWarnings("all")
    private ISmsService iSmsService;

    @Override
    public boolean sendMsg(Sms sms) {
        SendSmsRequest smsRequest = buildSmsRequest(sms);
        try {
            SendSmsResponse sendSmsResponse = iSmsService.sendSmsRequest(smsRequest);
            log.info("发送的结果为{}", JSON.toJSONString(sendSmsResponse, true));
            // 发送成功,否则失败
            if ("OK".equals(sendSmsResponse.getCode())) {
                sms.setStatus(1);
                return save(sms);
            } else {
                return false;
            }
        } catch (Exception e) {
            log.error("SmsServiceImpl#sendMsg, send sms fail, sms: {}, smsRequest: {}", JSON.toJSONString(sms), JSON.toJSONString(smsRequest));
        }
        return false;
    }

    /**
     * 构建一个发送短信的请求对象
     * @param sms   sms基础信息
     * @return  SendSmsRequest
     */
    private SendSmsRequest buildSmsRequest(Sms sms) {
        SendSmsRequest sendSmsRequest = new SendSmsRequest();
        //发送给谁
        sendSmsRequest.setPhoneNumbers(sms.getMobile());
        // 设置签名---公司里面不会随便的改变
        Config signConfig = configService.getConfigByCode("SIGN");
        sendSmsRequest.setSignName(signConfig.getValue());

        // 获取短信模版 -- 根据传入的 templateCode 动态改变
        Config configByCode = configService.getConfigByCode(sms.getTemplateCode());
        if (configByCode == null) {
            throw new IllegalArgumentException("您输入的签名不存在");
        }
        sendSmsRequest.setTemplateCode(configByCode.getValue());
        // 验证码 SMS:templateCode:{mobile}, 5分钟失效
        String code = RandomUtil.randomNumbers(6);
        redisTemplate.opsForValue().set("SMS:" + sms.getTemplateCode() + ":" + sms.getMobile(), code,5, TimeUnit.MINUTES);
        sendSmsRequest.setTemplateParam("{\"code\":\"" + code + "\"}");

        String desc = configByCode.getDesc(); //  sign:您的验证码${code}，该验证码5分钟内有效，请勿泄漏于他人！
        String content = signConfig.getValue() + ":" + desc.replaceAll("\\$\\{code\\}", code);
        sms.setContent(content); // 最后短信的内容
        return sendSmsRequest;
    }
}
