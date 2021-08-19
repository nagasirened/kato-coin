package com.naga.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.naga.config.IdAutoConfiguration;
import com.naga.domain.Sms;
import com.naga.domain.UserAuthAuditRecord;
import com.naga.domain.UserAuthInfo;
import com.naga.dto.SysUserDto;
import com.naga.dto.UserDto;
import com.naga.feign.AdminServiceFeign;
import com.naga.geetest.GeetestLib;
import com.naga.maps.UserDtoMapper;
import com.naga.service.SmsService;
import com.naga.service.UserAuthAuditRecordService;
import com.naga.service.UserAuthInfoService;
import com.naga.service.UserService;
import com.naga.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.naga.mapper.UserMapper;
import com.naga.domain.User;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
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

    @Autowired
    private AdminServiceFeign adminServiceFeign;

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
        long sysUserId = Long.parseLong(authUserId);
        userAuthAuditRecord.setAuditUserId(sysUserId);
        SysUserDto sysUserDto = adminServiceFeign.getSysUserInfo(sysUserId);
        if (Objects.isNull(sysUserDto)) {
            throw new RuntimeException("审核错误，管理员不存在");
        }
        userAuthAuditRecord.setAuditUserName(sysUserDto.getFullName());
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
            throw new IllegalArgumentException("用户信息错误，验证失败");
        }

        user.setAuthStatus(1);
        user.setAuthtime(new Date());
        user.setRealName(userAuthForm.getRealName());
        user.setIdCard(userAuthForm.getIdCard());
        user.setIdCardType(userAuthForm.getIdCardType());
        return updateById(user);
    }

    private void checkForm(UserAuthForm userAuthForm) {
        userAuthForm.check(geetestLib, redisTemplate);
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
        if (Objects.isNull(user)) {
            throw new RuntimeException("处理异常，请重新登录");
        }
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

    /**
     * 根据用户主键ID集合获取用户dto对象
     * @param ids 据用户主键ID集合
     * @return List<UserDto>
     */
    @Override
    public List<UserDto> getBasicUsers(List<Long> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            return Lists.newArrayList();
        }
        List<User> userList = list(new LambdaQueryWrapper<User>().in(User::getId, ids));
        return UserDtoMapper.INSTANCE.convert2Dto(userList);
    }

    /**
     * 用户注册
     * @param registerParam 注册用户的参数
     * @return  注册结果
     */
    @Override
    public boolean register(RegisterParam registerParam) {
        String email  = registerParam.getEmail();
        String mobile = registerParam.getMobile();
        if (StringUtils.isEmpty(email) && StringUtils.isEmpty(mobile)) {
            throw new IllegalArgumentException("手机号或邮箱不能同时为空");
        }

        int count = count(new LambdaQueryWrapper<User>().eq(StringUtils.isNotEmpty(email), User::getEmail, email)
                                                    .eq(StringUtils.isNotEmpty(mobile), User::getMobile, mobile));
        if (count > 0) {
            throw new IllegalArgumentException("手机号或邮箱已经被注册");
        }
        registerParam.check(geetestLib, redisTemplate);
        User user = getUser(registerParam);
        return save(user);
    }

    /**
     * 重置密码
     *
     * @param unSetPasswordParam 重置密码表单
     * @return 结果
     */
    @Override
    public boolean unsetLoginPwd(UnSetPasswordParam unSetPasswordParam) {
        log.info("开始重置密码{}", JSON.toJSONString(unSetPasswordParam, true));
        // 1 极验校验
        unSetPasswordParam.check(geetestLib, redisTemplate);
        // 2 手机号码校验
        String phoneValidateCode = String.valueOf(Optional.ofNullable(
                redisTemplate.opsForValue().get("SMS:FORGOT_VERIFY:" + unSetPasswordParam.getMobile())).orElse(""));
        if (!unSetPasswordParam.getValidateCode().equals(phoneValidateCode)) {
            throw new IllegalArgumentException("手机验证码错误");
        }

        // 3 数据库用户的校验
        String mobile = unSetPasswordParam.getMobile();
        User user = getOne(new LambdaQueryWrapper<User>().eq(User::getMobile, mobile));
        if (Objects.isNull(user)) {
            throw new IllegalArgumentException("该用户不存在");
        }
        String encode = new BCryptPasswordEncoder().encode(unSetPasswordParam.getPassword());
        user.setPassword(encode);
        return updateById(user);
    }

    /**
     * 重置支付密码
     *
     * @param unsetPayPasswordParam 重置支付密码表单
     * @return 结果
     */
    @Override
    public boolean unsetPayPassword(UnsetPayPasswordParam unsetPayPasswordParam) {
        long userId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        User user = getById(userId);
        if (Objects.isNull(user)) {
            throw new IllegalArgumentException("账号异常，请重新登录");
        }
        String validateCode = unsetPayPasswordParam.getValidateCode();
        String phoneValidate = String.valueOf(Optional.ofNullable(
                redisTemplate.opsForValue().get("SMS:FORGOT_PAY_PWD_VERIFY:" + user.getMobile())).orElse(""));
        if (!validateCode.equals(phoneValidate)) {
            throw new IllegalArgumentException("验证码错误");
        }
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        user.setPaypassword(bCryptPasswordEncoder.encode(unsetPayPasswordParam.getPayPassword()));
        return updateById(user);
    }

    /**
     * 注册用户包装数据
     * @param registerParam 注册参数
     * @return  包装后的User对象
     */
    private User getUser(RegisterParam registerParam) {
        User user = new User();
        user.setCountryCode(registerParam.getCountryCode());
        user.setEmail(registerParam.getEmail());
        user.setMobile(registerParam.getMobile());
        String encodePwd = new BCryptPasswordEncoder().encode(registerParam.getPassword());
        user.setPassword(encodePwd);
        user.setPaypassSetting(false);  // 未设置支付密码
        user.setStatus(1);              // 可用
        user.setType(1);                // normal User
        user.setAuthStatus(0);          // 未认证
        user.setLogins(0);
        user.setInviteCode(RandomUtil.randomString(6)); // 用户的邀请码
        if (!StringUtils.isEmpty(registerParam.getInvitionCode())) {
            User userPre = getOne(new LambdaQueryWrapper<User>().eq(User::getInviteCode, registerParam.getInvitionCode()));
            if (userPre != null) {
                user.setDirectInviteid(String.valueOf(userPre.getId())); // 邀请人的id , 需要查询
                user.setInviteRelation(String.valueOf(userPre.getId())); // 邀请人的id , 需要查询
            }

        }
        return user;
    }

}
