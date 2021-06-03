package com.naga.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.naga.mapper.UserLoginLogMapper;
import com.naga.domain.UserLoginLog;
import com.naga.service.UserLoginLogService;
@Service
public class UserLoginLogServiceImpl extends ServiceImpl<UserLoginLogMapper, UserLoginLog> implements UserLoginLogService{

}
