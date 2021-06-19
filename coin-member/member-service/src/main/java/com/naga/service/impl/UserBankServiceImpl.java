package com.naga.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.naga.domain.User;
import com.naga.service.UserBankService;
import com.naga.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.naga.domain.UserBank;
import com.naga.mapper.UserBankMapper;


@Service
public class UserBankServiceImpl extends ServiceImpl<UserBankMapper, UserBank> implements UserBankService {

    @Autowired
    private UserService userService;

    /**
     * 查询用户的银行卡信息
     *
     * @param page  分页参数
     * @param userId 用户的Id
     * @return
     */
    @Override
    public Page<UserBank> findByPage(Page<UserBank> page, Long userId) {
        return page(page, new LambdaQueryWrapper<UserBank>().eq(Objects.nonNull(userId), UserBank::getUserId, userId));
    }

    /**
     * 获取用户的银行卡信息
     * @return UserBank
     */
    @Override
    public UserBank getCurrentUserBank() {
        long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        return getOne(new LambdaQueryWrapper<UserBank>().eq(UserBank::getUserId, userId)
                                                .eq(UserBank::getStatus, 1));
    }

    @Override
    public boolean bindBank(UserBank userBank) {
        // 支付密码的判断
        long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        String payPassword = userBank.getPayPassword();
        User user = userService.getById(userId);
        // 绑定银行卡需要验证支付密码对不对
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        if(!bCryptPasswordEncoder.matches(payPassword, user.getPaypassword())){
            throw new IllegalArgumentException("用户的支付密码错误") ;
        }

        // 有Id 代表是修改操作  没有ID代表是新增绑定
        Long id = userBank.getId();
        if(id!=null) {
            UserBank userBankDb = getById(id);
            if (Objects.isNull(userBankDb)){
                throw new IllegalArgumentException("用户的银行卡的ID输入错误") ;
            }
            return updateById(userBank);
        }
        userBank.setUserId(userId);
        return save(userBank);
    }
}
