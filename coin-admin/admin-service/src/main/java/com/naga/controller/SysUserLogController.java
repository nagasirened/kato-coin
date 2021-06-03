package com.naga.controller;


import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.naga.domain.SysUserLog;
import com.naga.model.R;
import com.naga.service.SysUserLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@Api(tags = "用户的操作记录查询")
@RequestMapping("/sysUserLog")
public class SysUserLogController {

    @Autowired
    private SysUserLogService sysUserLogService;

    @GetMapping
    @ApiOperation(value = "分页查询用户操作记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页"),
            @ApiImplicitParam(name = "size", value = "每页的数量"),
    })
    @PreAuthorize("hasAuthority('sys_user_query')")
    public R<Page<SysUserLog>> findByPage(@ApiIgnore Page<SysUserLog> page) {
        page.addOrder(OrderItem.desc("created"));
        return R.ok(sysUserLogService.page(page));
    }
}
