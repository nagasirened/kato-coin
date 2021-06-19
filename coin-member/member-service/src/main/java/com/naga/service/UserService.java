package com.naga.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.naga.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.naga.dto.UserDto;
import com.naga.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserService extends IService<User> {

    /**
     * 条件分页查询会员的列表
     *
     * @param page         分页参数
     * @param mobile       会员的手机号
     * @param userId       会员的ID
     * @param userName     会员的名称
     * @param realName     会员的真实名称
     * @param status       会员的状态
     * @param reviewStatus 会员的审核状态
     * @return
     */
    Page<User> findByPage(Page<User> page, String mobile, Long userId, String userName, String realName, Integer status, Integer reviewStatus);

    /**
     * 通过用户的Id 查询该用户邀请的人员
     *
     * @param page   分页参数
     * @param userId 用户的Id
     * @return
     */
    Page<User> findDirectInvitePage(Page<User> page, Long userId);

    /**
     * 获取用户审核详情，包含审核历史和提交审核信息
     *
     * @param id 用户主键
     * @return
     */
    UseAuthInfoVO getUserAuthInfo(Long id);

    /**
     * 审核
     *
     * @param userId     被审核用户主键
     * @param authStatus 审核状态
     * @param authCode   审核信息的唯一authCode
     * @param remark     拒绝的原因
     */
    void updateUserAuthStatus(Long userId, Integer authStatus, Long authCode, String remark);

    /**
     * 用户的实名认证信息
     *
     * @param userAuthForm 实名认证信息
     * @return boolean
     */
    boolean identifyVerify(UserAuthForm userAuthForm);

    /**
     * 用户进行高级认证
     *
     * @param imgList 图片地址集合
     */
    void authUser(List<String> imgList);

    /**
     * 修改电话号码
     *
     * @param updatePhoneParam 修改电话号码Form表单
     * @return 是否成功
     */
    boolean updatePhone(UpdatePhoneParam updatePhoneParam);

    /**
     * 检查手机号是否可用
     *
     * @param mobile      手机号码
     * @param countryCode 国家区号
     * @return 验证是否成功
     */
    boolean checkNewPhone(String mobile, String countryCode);

    /**
     * 根据用户主键ID集合获取用户dto对象
     *
     * @param ids 据用户主键ID集合
     * @return List<UserDto>
     */
    List<UserDto> getBasicUsers(List<Long> ids);

    /**
     * 用户注册
     *
     * @param registerParam 注册用户的参数
     * @return 注册结果
     */
    boolean register(RegisterParam registerParam);

    /**
     * 重置密码
     *
     * @param unSetPasswordParam 重置密码表单
     * @return 结果
     */
    boolean unsetLoginPwd(UnSetPasswordParam unSetPasswordParam);

    /**
     * 重置支付密码
     *
     * @param unsetPayPasswordParam 重置支付密码表单
     * @return 结果
     */
    boolean unsetPayPassword(UnsetPayPasswordParam unsetPayPasswordParam);
}
