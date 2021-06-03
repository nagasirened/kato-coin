package com.naga.controller;


import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.naga.domain.SysRole;
import com.naga.model.R;
import com.naga.service.SysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/role")
@Api(tags = "角色管理")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    @GetMapping
    @ApiOperation("条件分页查询")
    @PreAuthorize("hasAuthority('sys_role_query')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页"),
            @ApiImplicitParam(name = "size", value = "每页数量"),
            @ApiImplicitParam(name = "name", value = "角色名，模型查询")
    })
    public R<Page<SysRole>> findByPage(@ApiIgnore Page<SysRole> page, String name) {
        return R.ok(sysRoleService.findByPage(page, name));
    }

    @PostMapping
    @ApiOperation("新增一个角色")
    @PreAuthorize("hasAuthority('sys_role_create')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sysRole", value = "新增一个角色的JSON数据")
    })
    public R createRole(@RequestBody @Validated SysRole sysRole) {
        boolean saveResult = sysRoleService.save(sysRole);
        return saveResult ? R.ok() : R.fail();
    }

    @PostMapping("/delete")
    @ApiOperation("批量删除角色")
    @PreAuthorize("hasAuthority('sys_role_delete')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "要删除的角色ID的数组")
    })
    public R delBatch(@RequestBody String[] ids) {
        List<String> idList = Arrays.asList(ids);
        if (CollectionUtil.isEmpty(idList)) {
            return R.fail("要删除的内容不能为空");
        }
        boolean delResult = sysRoleService.removeByIds(idList);
        return delResult ? R.ok() : R.fail();
    }
}
