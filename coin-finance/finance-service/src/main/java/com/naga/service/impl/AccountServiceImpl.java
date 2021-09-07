package com.naga.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.naga.domain.Account;
import com.naga.service.AccountService;
import com.naga.mapper.AccountMapper;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account>
implements AccountService{

}




