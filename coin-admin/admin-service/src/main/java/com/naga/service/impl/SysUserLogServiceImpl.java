package com.naga.service.impl;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.naga.mapper.SysUserLogMapper;
import com.naga.domain.SysUserLog;
import com.naga.service.SysUserLogService;
@Service
public class SysUserLogServiceImpl extends ServiceImpl<SysUserLogMapper, SysUserLog> implements SysUserLogService{

    @Async("asyncThreadPool")
    @Override
    public boolean save(SysUserLog entity) {
        return super.save(entity);
    }
}
