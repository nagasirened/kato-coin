package com.naga.service.impl;

import com.naga.service.UserLoginLogService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.naga.mapper.UserLoginLogMapper;
import com.naga.domain.UserLoginLog;

@Service
public class UserLoginLogServiceImpl extends ServiceImpl<UserLoginLogMapper, UserLoginLog> implements UserLoginLogService {

}
