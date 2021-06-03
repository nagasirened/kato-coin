package com.naga.controller;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.naga.domain.SysUser;
import com.naga.model.R;
import com.naga.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Arrays;

@RestController
@RequestMapping("/user")
@Api(tags = "员工管理")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @GetMapping
    @ApiOperation(value = "分页查询用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页"),
            @ApiImplicitParam(name = "size", value = "每页的数量"),
            @ApiImplicitParam(name = "mobile", value = "手机号码"),
            @ApiImplicitParam(name = "fullName", value = "员工全名"),
    })
    //@PreAuthorize("hasAuthority('sys_user_query')")
    public R<Page<SysUser>> findByPage(@ApiIgnore Page<SysUser> page, String mobile, String fullName) {
        page.addOrder(OrderItem.desc("last_update_time"));
        Page<SysUser> sysUserPage = sysUserService.findByPage(page, mobile, fullName);
        return R.ok(sysUserPage);
    }

    @PostMapping
    @ApiOperation(value = "新增用户，同时给用户分配角色信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sysUser", value = "SysUser Json Data")
    })
    @PreAuthorize("hasAuthority('sys_user_query')")
    public R createUser(@RequestBody @Validated SysUser sysUser) {
        boolean result = sysUserService.addUser(sysUser);
        return result ? R.ok() : R.fail();
    }

    @PutMapping
    @ApiOperation(value = "修改用户，同时给用户分配角色信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sysUser", value = "SysUser Json Data")
    })
    @PreAuthorize("hasAuthority('sys_user_update')")
    public R updateUser(@RequestBody @Validated SysUser sysUser) {
        boolean result = sysUserService.updateUser(sysUser);
        return result ? R.ok() : R.fail();
    }

    @DeleteMapping
    @ApiOperation(value = "删除用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "删除用户信息")
    })
    @PreAuthorize("hasAuthority('sys_user_delete')")
    public R deleteUser(Long[] ids) {
        boolean removeResult = sysUserService.removeByIds(Arrays.asList(ids));
        return removeResult ? R.ok() : R.fail();
    }
}
