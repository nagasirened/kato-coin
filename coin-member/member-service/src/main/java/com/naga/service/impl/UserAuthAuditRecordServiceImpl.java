package com.naga.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.naga.service.UserAuthAuditRecordService;
import org.springframework.stereotype.Service;

import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.naga.mapper.UserAuthAuditRecordMapper;
import com.naga.domain.UserAuthAuditRecord;

@Service
public class UserAuthAuditRecordServiceImpl extends ServiceImpl<UserAuthAuditRecordMapper, UserAuthAuditRecord> implements UserAuthAuditRecordService {

    /**
     * 获取一个用户的审核记录
     * @param userId 用户主键
     * @return List<UserAuthAuditRecord>
     */
    @Override
    public List<UserAuthAuditRecord> getUserAuthAuditRecordList(Long userId) {
        return list(new LambdaQueryWrapper<UserAuthAuditRecord>()
                        .eq(UserAuthAuditRecord::getUserId, userId)
                        .orderByDesc(UserAuthAuditRecord::getCreated)
                        .last("limit 3"));
    }
}
