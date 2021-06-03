package com.naga.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.naga.domain.UserBank;
import com.baomidou.mybatisplus.extension.service.IService;


public interface UserBankService extends IService<UserBank>{

    /**
     * 查询用户的银行卡信息
     * @param page 分页参数
     * @param userId 用户的Id
     * @return 分页结果
     */
    Page<UserBank> findByPage(Page<UserBank> page, Long userId);

}
