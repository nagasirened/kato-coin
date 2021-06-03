package com.naga.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.naga.domain.UserAuthAuditRecord;
import com.naga.domain.UserAuthInfo;
import com.naga.service.UserAuthAuditRecordService;
import com.naga.service.UserAuthInfoService;
import com.naga.vo.UseAuthInfoVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
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
}