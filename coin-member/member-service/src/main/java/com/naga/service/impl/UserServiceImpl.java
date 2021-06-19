package com.naga.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Snowflake;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.naga.config.IdAutoConfiguration;
import com.naga.domain.Sms;
import com.naga.domain.UserAuthAuditRecord;
import com.naga.domain.UserAuthInfo;
import com.naga.geetest.GeetestLib;
import com.naga.service.SmsService;
import com.naga.service.UserAuthAuditRecordService;
import com.naga.service.UserAuthInfoService;
import com.naga.service.UserService;
import com.naga.vo.UpdatePhoneParam;
import com.naga.vo.UseAuthInfoVO;
import com.naga.vo.UserAuthForm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.naga.mapper.UserMapper;
import com.naga.domain.User;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    @SuppressWarnings("all")
    private UserAuthInfoService userAuthInfoService;

    @Autowired
    @SuppressWarnings("all")
    private UserAuthAuditRecordService userAuthAuditRecordService;

    @Autowired
    private SmsService smsService;

    @Autowired
    private GeetestLib geetestLib;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private Snowflake snowflake;

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

    /**
     * 用户进行高级认证
     * @param imgList   图片地址集合
     */
    @Override
    @Transactional
    public void authUser(List<String> imgList) {
        if (CollectionUtil.isEmpty(imgList)) {
            throw new IllegalArgumentException("用户证件信息为null");
        }
        String userIdStr = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        long userId = Long.parseLong(userIdStr);
        User user = getById(Long.parseLong(userIdStr));
        if (Objects.isNull(user)) {
            throw new RuntimeException("处理异常，请重新登录");
        }
        long authCode = snowflake.nextId();
        List<UserAuthInfo> userAuthInfoList = new ArrayList<>(imgList.size());
        for (int i = 0; i < imgList.size(); i++) {
            UserAuthInfo userAuthInfo = new UserAuthInfo();
            userAuthInfo.setUserId(userId);
            userAuthInfo.setAuthCode(authCode);
            userAuthInfo.setImageUrl(imgList.get(i));
            userAuthInfo.setSerialno(i + 1); // 1正面  2反面  3手持
            userAuthInfoList.add(userAuthInfo);
        }

        userAuthInfoService.saveBatch(userAuthInfoList);
        // 等待审核
        user.setReviewsStatus(0);
        updateById(user);
    }

    /**
     * 修改电话号码
     * @param updatePhoneParam 修改电话号码Form表单
     * @return 是否成功
     */
    @Override
    public boolean updatePhone(UpdatePhoneParam updatePhoneParam) {
        long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        User user = getById(userId);
        // 验证新旧手机的验证码
        String oldVerifyCode = String.valueOf(Optional.ofNullable(redisTemplate.opsForValue().get("SMS:VERIFY_OLD_PHONE:" + user.getMobile())).orElse(""));
        if (!updatePhoneParam.getOldValidateCode().equals(oldVerifyCode)) {
            throw new IllegalArgumentException("旧手机的验证码错误");
        }

        String newVerifyCode = String.valueOf(Optional.ofNullable(redisTemplate.opsForValue().get("SMS:CHANGE_PHONE_VERIFY:" + updatePhoneParam.getNewMobilePhone())).orElse(""));
        if (!updatePhoneParam.getValidateCode().equals(newVerifyCode)) {
            throw new IllegalArgumentException("新手机的验证码错误");
        }
        user.setMobile(updatePhoneParam.getNewMobilePhone());
        return updateById(user);
    }

    /**
     * 检查手机号是否可用
     * @param mobile    手机号码
     * @param countryCode   国家区号
     * @return  验证是否成功
     */
    @Override
    public boolean checkNewPhone(String mobile, String countryCode) {
        int count = count(new LambdaQueryWrapper<User>()
                .eq(User::getMobile, mobile)
                .eq(User::getCountryCode,countryCode));
        // 有用户占用这个手机号
        if(count>0) {
            throw new IllegalArgumentException("该手机号已经被占用");
        }
        // 2 向新的手机发送短信
        Sms sms = new Sms();
        sms.setMobile(mobile);
        sms.setCountryCode(countryCode);
        sms.setTemplateCode("CHANGE_PHONE_VERIFY");
        return smsService.sendMsg(sms) ;
    }


}
