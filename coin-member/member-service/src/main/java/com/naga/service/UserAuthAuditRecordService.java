package com.naga.service;

import com.naga.domain.UserAuthAuditRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface UserAuthAuditRecordService extends IService<UserAuthAuditRecord>{

    /**
     * 获取一个用户的审核记录
     * @param userId 用户主键
     * @return List<UserAuthAuditRecord>
     */
    List<UserAuthAuditRecord> getUserAuthAuditRecordList(Long userId);
}
