package com.naga.service;

import com.naga.domain.Sms;
import com.baomidou.mybatisplus.extension.service.IService;
public interface SmsService extends IService<Sms>{

    /**
     * 短信发送
     * @param sms 基础信息
     * @return  是否发送成功
     */
    boolean sendMsg(Sms sms);
}
