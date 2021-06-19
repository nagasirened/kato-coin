package com.naga.controller;


import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.naga.domain.SysPrivilege;
import com.naga.model.R;
import com.naga.service.SysPrivilegeService;
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

import java.util.Date;

@RestController
@RequestMapping("/privilege")
@Api(tags = "权限管理控制器")
public class SysPrivilegeController {

    @Autowired
    private SysPrivilegeService sysPrivilegeService;

    @GetMapping
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页"),
            @ApiImplicitParam(name = "size", value = "pageSize")
    })
    @PreAuthorize("hasAuthority('sys_privilege_query')")
    public R<Page<SysPrivilege>> findByPage(@ApiIgnore Page<SysPrivilege> page) {
        // 根据最近新增和修改的时间，倒序排序
        page.addOrder(OrderItem.desc("last_update_time"));
        Page<SysPrivilege> sysPrivilegePage = sysPrivilegeService.page(page);
        return R.ok(sysPrivilegePage);
    }

    @PostMapping
    @ApiOperation(value = "新增权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sysPrivilege", value = "sysPrivilege的JSON数据")
    })
    @PreAuthorize("hasAuthority('sys_privilege_create')")
    public R createPrivilege(@RequestBody @Validated SysPrivilege sysPrivilege) {
        Date now = new Date();
        sysPrivilege.setCreated(now);
        sysPrivilege.setLastUpdateTime(now);
        sysPrivilege.setCreateBy(Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString()));
        boolean result = sysPrivilegeService.save(sysPrivilege);
        return result ? R.ok() : R.fail();
    }

    @PutMapping
    @ApiOperation(value = "修改一个权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sysPrivilege", value = "sysPrivilege的JSON数据")
    })
    @PreAuthorize("hasAuthority('sys_privilege_update')")
    public R updatePrivilege(@RequestBody @Validated SysPrivilege sysPrivilege) {
        sysPrivilege.setLastUpdateTime(new Date());
        sysPrivilege.setModifyBy(Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString()));
        boolean result = sysPrivilegeService.updateById(sysPrivilege);
        return result ? R.ok() : R.fail();
    }

}
