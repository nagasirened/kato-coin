package com.naga.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.naga.config.IdAutoConfiguration;
import com.naga.domain.UserAuthAuditRecord;
import com.naga.domain.UserAuthInfo;
import com.naga.geetest.GeetestLib;
import com.naga.service.UserAuthAuditRecordService;
import com.naga.service.UserAuthInfoService;
import com.naga.vo.UseAuthInfoVO;
import com.naga.vo.UserAuthForm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.naga.mapper.UserMapper;
import com.naga.domain.User;
import com.naga.service.UserService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService{

    @Autowired
    private UserAuthInfoService userAuthInfoService;

    @Autowired
    private UserAuthAuditRecordService userAuthAuditRecordService;

    @Autowired
    private GeetestLib geetestLib;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 条件分页查询会员的列表
     *
     * @param page     分页参数
     * @param mobile   会员的手机号
     * @param userId   会员的ID
     * @param userName 会员的名称
     * @param realName 会员的真实名称
     * @param status   会员的状态
     * @return  分页用户信息
     */
    @Override
    public Page<User> findByPage(Page<User> page, String mobile, Long userId, String userName, String realName, Integer status, Integer reviewStatus) {
        return page(page, new LambdaQueryWrapper<User>()
                        .like(!StringUtils.isEmpty(mobile), User::getMobile, mobile)
                        .like(!StringUtils.isEmpty(userName), User::getUsername, userName)
                        .like(!StringUtils.isEmpty(realName), User::getRealName, realName)
                        .eq(Objects.nonNull(userId), User::getId, userId)
                        .eq(Objects.nonNull(status), User::getStatus, status)
                        .eq(Objects.nonNull(reviewStatus), User::getReviewsStatus, reviewStatus));
    }


    /**
     * 通过用户的Id 查询该用户邀请的人员
     *
     * @param page   分页参数
     * @param userId 用户的Id
     * @return       用户邀请人员列表
     */
    @Override
    public Page<User> findDirectInvitePage(Page<User> page, Long userId) {
        return page(page, new LambdaQueryWrapper<User>().eq(User::getDirectInviteid, userId));
    }

    /**
     * 获取用户审核详情，包含审核历史和提交审核信息
     * @param id 用户主键
     * @return UseAuthInfoVO
     */
    @Override
    public UseAuthInfoVO getUserAuthInfo(Long id) {
        User user = getById(id);
        List<UserAuthInfo> userAuthInfoList = null;
        List<UserAuthAuditRecord> userAuthAuditRecordList = null;
        if (Objects.nonNull(user)) {
            // user的审核状态,1通过,2拒绝,0,待审核   0的话就没有审核记录  但是有待审核提交的信息
            Integer reviewsStatus = user.getReviewsStatus();
            if (Objects.isNull(reviewsStatus) || reviewsStatus.equals(0)) {
                userAuthAuditRecordList = Collections.emptyList();
                userAuthInfoList = userAuthInfoService.getUserAuthInfoByUserId(id);
            } else {
                userAuthAuditRecordList = userAuthAuditRecordService.getUserAuthAuditRecordList(id);
                // 按照时间倒序排列，index=0的值就是最近被认证的一次，且一定存在. 获取最近这一次的审核信息即可
                Long authCode = userAuthAuditRecordList.get(0).getAuthCode();
                userAuthInfoList = userAuthInfoService.getUserAuthInfoByAuthCode(authCode);
            }
        }
        return new UseAuthInfoVO(user, userAuthInfoList, userAuthAuditRecordList);
    }

    /**
     * 审核
     * @param userId            被审核用户主键
     * @param authStatus        审核状态
     * @param authCode          审核信息的唯一authCode
     * @param remark            拒绝的原因
     */
    @Override
    public void updateUserAuthStatus(Long userId, Integer authStatus, Long authCode, String remark) {
        User user = new User();
        user.setReviewsStatus(authStatus);
        user.setId(userId);
        this.updateById(user);

        // 新增审核记录
        String authUserId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        UserAuthAuditRecord userAuthAuditRecord = new UserAuthAuditRecord();
        userAuthAuditRecord.setUserId(userId);
        userAuthAuditRecord.setAuthCode(authCode);
        userAuthAuditRecord.setStatus(authStatus);
        userAuthAuditRecord.setRemark(remark);
        userAuthAuditRecord.setAuditUserId(Long.parseLong(authUserId));
        // TODO  审核人名称，远程调用admin-service  因为管理员和非管理员不是一个库
        userAuthAuditRecord.setAuditUserName("test");

        userAuthAuditRecordService.save(userAuthAuditRecord);
    }

    @Override
    public User getById(Serializable id) {
        User user = super.getById(id);
        if (Objects.isNull(user)) {
            throw new IllegalArgumentException("用户不存在");
        }

        Integer seniorAuthStatus;
        String seniorAuthDesc;
        // reviewsStatus, 审核状态 1通过,2拒绝,0待审核
        Integer reviewsStatus = user.getReviewsStatus();
        if (Objects.isNull(reviewsStatus)) {
            seniorAuthStatus = 0;
            seniorAuthDesc = "资料未填写";
        } else {
            switch (reviewsStatus) {
                case 1:
                    seniorAuthStatus = 1;
                    seniorAuthDesc = "审核通过";
                    break;
                case 2:
                    seniorAuthStatus = 2;
                    // 时间倒序排列的错误原因
                    List<UserAuthAuditRecord> userAuthAuditRecordList = userAuthAuditRecordService.getUserAuthAuditRecordList(user.getId());
                    if (CollectionUtil.isNotEmpty(userAuthAuditRecordList)) {
                        seniorAuthDesc = userAuthAuditRecordList.get(0).getRemark();
                    } else {
                        seniorAuthDesc = "未知原因";
                    }
                    break;
                case 0:
                    seniorAuthStatus = 0;
                    seniorAuthDesc = "待审核";
                    break;
                default:
                    throw new RuntimeException("获取高级认证信息错误");
            }
        }
        user.setSeniorAuthStatus(seniorAuthStatus);
        user.setSeniorAuthDesc(seniorAuthDesc);
        return user;
    }

    @Override
    public boolean identifyVerify(UserAuthForm userAuthForm) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        User user = getById(Long.parseLong(userId));
        if (!user.getAuthStatus().equals(0)) {
            throw new IllegalArgumentException("用户已认证成功");
        }
        // 极验验证
        checkForm(userAuthForm);
        // 实名认证
        boolean result = IdAutoConfiguration.checkId(userAuthForm.getIdCard(), userAuthForm.getRealName());
        if (!result) {
            throw new IllegalArgumentException("用户信息错误");
        }

        user.setAuthStatus(1);
        user.setAuthtime(new Date());
        user.setRealName(userAuthForm.getRealName());
        user.setIdCard(userAuthForm.getIdCard());
        user.setIdCardType(userAuthForm.getIdCardType());
        return updateById(user);
    }

    private void checkForm(UserAuthForm userAuthForm) {
        userAuthForm.check(userAuthForm, geetestLib, redisTemplate);
    }
}
