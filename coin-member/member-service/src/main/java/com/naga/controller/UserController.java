package com.naga.controller;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.naga.domain.User;
import com.naga.dto.UserDto;
import com.naga.feign.UserServiceFeign;
import com.naga.model.R;
import com.naga.service.UserService;
import com.naga.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/users")
@Api(tags = "会员的控制器")
public class UserController implements UserServiceFeign {

    @Autowired
    @SuppressWarnings("all")
    private UserService userService;

    @GetMapping
    @ApiOperation(value = "查询会员的列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current",value = "当前页"),
            @ApiImplicitParam(name = "size",value = "每页显示的条数"),
            @ApiImplicitParam(name = "mobile",value = "会员的手机号"),
            @ApiImplicitParam(name = "userId",value = "会员的Id,精确查询"),
            @ApiImplicitParam(name = "userName",value = "会员的名称"),
            @ApiImplicitParam(name = "realName",value = "会员的真实名称"),
            @ApiImplicitParam(name = "status",value = "会员的状态")

    })
    @PreAuthorize("hasAuthority('user_query')")
    public R<Page<User>> findByPage(@ApiIgnore Page<User> page, String mobile, Long userId,
                                    String userName, String realName, Integer status){
        page.addOrder(OrderItem.desc("last_update_time"));
        Page<User> userPage = userService.findByPage(page, mobile, userId, userName, realName, status, null);
        return R.ok(userPage);
    }

    @PutMapping("/status")
    @ApiOperation(value = "切换用户的状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id" ,value = "会员的id"),
            @ApiImplicitParam(name = "status" ,value = "会员的状态"),
    })
    @PreAuthorize("hasAuthority('user_update')")
    public R updateStatus(Long id, Integer status){
        User user = new User();
        user.setId(id);
        user.setStatus(status);
        return userService.updateById(user) ? R.ok() : R.fail();
    }

    @PutMapping
    @ApiOperation(value = "修改用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user" ,value = "会员的json数据"),
    })
    @PreAuthorize("hasAuthority('user_update')")
    public R updateUser(@RequestBody @Validated User user){
        return userService.updateById(user) ? R.ok() : R.fail();
    }



    @PostMapping
    @ApiOperation(value = "新增用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user" ,value = "会员的json数据"),
    })
    @PreAuthorize("hasAuthority('user_create')")
    public R createUser(@RequestBody @Validated User user){
        return userService.save(user) ? R.ok() : R.fail();
    }



    @GetMapping("info")
    @ApiOperation(value = "查看用户详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id" ,value = "user的主键"),
    })
    public R<User> getUserInfo(Long id) {
        User user = userService.getById(id);
        user.setPassword(null);
        user.setPaypassword(null);
        return R.ok(user);
    }



    @GetMapping("/directInvites")
    @ApiOperation(value = "查询该用户邀请的用户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId" ,value = "该用户的Id"),
            @ApiImplicitParam(name = "current", value = "当前页"),
            @ApiImplicitParam(name = "size", value = "每页显示的条数"),

    })
    public R<Page<User>> getDirectInvites(@ApiIgnore Page<User> page, Long userId){
        Page<User> userPage = userService.findDirectInvitePage(page, userId);
        return R.ok(userPage);
    }


    @GetMapping("/auths")
    @ApiOperation(value = "查询会员的列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current",value = "当前页"),
            @ApiImplicitParam(name = "size",value = "每页显示的条数"),
            @ApiImplicitParam(name = "mobile",value = "会员的手机号"),
            @ApiImplicitParam(name = "userId",value = "会员的Id,精确查询"),
            @ApiImplicitParam(name = "userName",value = "会员的名称"),
            @ApiImplicitParam(name = "realName",value = "会员的真实名称"),
            @ApiImplicitParam(name = "reviewStatus",value = "审核状态")

    })
    @PreAuthorize("hasAuthority('user_query')")
    public R<Page<User>> findUserAuths(@ApiIgnore Page<User> page, String mobile, Long userId,
                                    String userName, String realName, Integer reviewStatus){
        page.addOrder(OrderItem.desc("last_update_time")) ;
        Page<User> userPage = userService.findByPage(page,mobile,userId,userName,realName, null, reviewStatus);
        return R.ok(userPage) ;
    }



    /**
     * 询用户的认证详情
     */
    @GetMapping("/auth/info")
    @ApiOperation(value = "查询用户的认证详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户的Id")
    })
    public R<UseAuthInfoVO> getUserAuthInfo(Long id) {
        return R.ok(userService.getUserAuthInfo(id));
    }

    @PostMapping("/auths/status")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户的ID"),
            @ApiImplicitParam(name = "authStatus", value = "用户的审核状态"),
            @ApiImplicitParam(name = "authCode", value = "一组图片的唯一标识"),
            @ApiImplicitParam(name = "remark", value = "审核拒绝的原因")
    })
    public R updateUserAuthStatus(@RequestParam(required = true) Long id,
                                  @RequestParam(required = true) Integer authStatus,
                                  @RequestParam(required = true) Long authCode,
                                  String remark) {
        // 审核: 1 修改user 里面的reviewStatus   2 在authAuditRecord 里面添加一条记录
        userService.updateUserAuthStatus(id, authStatus, authCode, remark);
        return R.ok();
    }


    @GetMapping("/current/info")
    @ApiOperation(value = "获取当前登录用户的详情")
    public R<User> currentUserInfo(){
        String idStr = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        User user = userService.getById(Long.valueOf(idStr));
        user.setPassword("*****");
        user.setPaypassword("*****");
        user.setAccessKeyId("*****");
        user.setAccessKeySecret("*****");
        return R.ok(user) ;
    }


    @PostMapping("/authAccount")
    @ApiOperation(value = "用户的实名认证（如身份证）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "" ,value = "")
    })
    public R identifyCheck(@RequestBody UserAuthForm userAuthForm){
        return userService.identifyVerify(userAuthForm) ? R.ok() : R.fail();
    }


    @PostMapping("/authUser")
    @ApiOperation(value = "用户进行高级认证，即上传了身份证信息后点击认证")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "imgs",value ="用户的图片地址" )
    })
    public R authUser(@RequestBody  String[] imgs){
        userService.authUser(Arrays.asList(imgs)) ;
        return R.ok() ;
    }

    @PostMapping("/phone")
    @ApiOperation(value = "修改手机号")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "updatePhoneParam",value = "updatePhoneParam 的json数据")
    })
    public R updatePhone(@RequestBody UpdatePhoneParam updatePhoneParam){
        return userService.updatePhone(updatePhoneParam) ? R.ok() : R.fail();
    }


    @GetMapping("/checkTel")
    @ApiOperation(value = "检查新的手机号是否可用,如可用,则给该新手机发送验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "新的手机号"),
            @ApiImplicitParam(name = "countryCode", value = "手机号的区域")
    })
    public R checkNewPhone(@RequestParam String mobile,
                           @RequestParam String countryCode){
        return userService.checkNewPhone(mobile,countryCode) ? R.ok():R.fail("新的手机号校验失败");
    }

    /**
     * 用于admin-service 里面远程调用member-service
     * @param ids   用户主键集合
     * @return  List<UserDto>
     */
    @Override
    public List<UserDto> getBasicUsers(List<Long> ids) {
        return userService.getBasicUsers(ids);
    }

    @PostMapping("/register")
    @ApiOperation(value = "用户的注册")
    public R register(@RequestBody RegisterParam registerParam) {
        return userService.register(registerParam) ? R.ok() : R.fail("注册失败");
    }

    @PostMapping("/setPassword")
    @ApiOperation(value = "用户重置密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "unSetPasswordParam", value = "unSetPasswordParam json")
    })
    public R unsetPassword(@RequestBody @Validated UnSetPasswordParam unSetPasswordParam) {
        return userService.unsetLoginPwd(unSetPasswordParam) ? R.ok() : R.fail("重置失败");
    }

    @PostMapping("/setPayPassword")
    @ApiOperation(value = "重新设置交易密码")
    public R setPayPassword(@RequestBody @Validated UnsetPayPasswordParam unsetPayPasswordParam) {
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        return userService.unsetPayPassword(unsetPayPasswordParam) ? R.ok() : R.fail("重置失败");
    }

}