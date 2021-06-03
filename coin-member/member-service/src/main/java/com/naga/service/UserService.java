package com.naga.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.naga.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.naga.vo.UseAuthInfoVO;

public interface UserService extends IService<User>{

    /**
     * 条件分页查询会员的列表
     * @param page
     * 分页参数
     * @param mobile
     * 会员的手机号
     * @param userId
     * 会员的ID
     * @param userName
     * 会员的名称
     * @param realName
     * 会员的真实名称
     * @param status
     * 会员的状态
     * @param reviewStatus
     * 会员的审核状态
     * @return
     */
    Page<User> findByPage(Page<User> page, String mobile, Long userId, String userName, String realName, Integer status, Integer reviewStatus);

    /**
     * 通过用户的Id 查询该用户邀请的人员
     * @param page
     * 分页参数
     * @param userId
     * 用户的Id
     * @return
     */
    Page<User> findDirectInvitePage(Page<User> page, Long userId);

    /**
     * 获取用户审核详情，包含审核历史和提交审核信息
     * @param id
     * 用户主键
     * @return
     */
    UseAuthInfoVO getUserAuthInfo(Long id);

}
