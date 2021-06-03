package com.naga.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.naga.domain.Sms;
import com.naga.mapper.SmsMapper;
import com.naga.service.SmsService;
@Service
public class SmsServiceImpl extends ServiceImpl<SmsMapper, Sms> implements SmsService{

}
