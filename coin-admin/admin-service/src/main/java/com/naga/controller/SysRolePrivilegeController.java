package com.naga.controller;

import com.naga.domain.SysMenu;
import com.naga.model.R;
import com.naga.model.RolePrivilegeParam;
import com.naga.service.SysRolePrivilegeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(tags = "角色权限的配置")
public class SysRolePrivilegeController {

    @Autowired
    private SysRolePrivilegeService sysRolePrivilegeService;

    @GetMapping("/roles_privilege")
    @ApiOperation("查询角色的权限列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID")
    })
    @PreAuthorize("hasAuthority('roles_privilege_query')")
    public R<List<SysMenu>> findSysMenuAndPrivilege(Long roleId) {
        List<SysMenu> sysMenus = sysRolePrivilegeService.findSysMenuAndPrivilege(roleId);
        return R.ok(sysMenus);
    }

    @PostMapping("/grant_privilege")
    @ApiOperation("授予角色某种权限")
    @PreAuthorize("hasAuthority('roles_privilege_update')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "rolePrivilegeParam", value = "rolePrivilegeParam JSON数据")
    })
    public R grantPrivileges(@RequestBody @Validated RolePrivilegeParam rolePrivilegeParam) {
        boolean result = sysRolePrivilegeService.grantPrivileges(rolePrivilegeParam);
        return result ? R.ok() : R.fail();
    }

}
