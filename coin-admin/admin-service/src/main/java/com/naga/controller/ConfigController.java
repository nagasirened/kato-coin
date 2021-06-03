package com.naga.controller;


import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.naga.domain.Config;
import com.naga.model.R;
import com.naga.service.ConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = "后台参数配置管理")
@RequestMapping("/configs")
public class ConfigController {

    @Autowired
    private ConfigService configService;

    @GetMapping
    @ApiOperation("条件分页查询管理参数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "pageNo"),
            @ApiImplicitParam(name = "size", value = "pageSize"),
            @ApiImplicitParam(name = "type", value = "配置规则类型"),
            @ApiImplicitParam(name = "code", value = "配置规则代码"),
            @ApiImplicitParam(name = "name", value = "配置规则名称"),
    })
    @PreAuthorize("hasAuthority('config_query')")
    public R<Page<Config>> findByPage(Page<Config> page, String type, String code, String name) {
        page.addOrder(OrderItem.desc("created"));
        Page<Config> pageResult = configService.findByPage(page, type, code, name);
        return R.ok(pageResult);
    }

    @PostMapping
    @ApiOperation(value = "新增系统参数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "config", value = "config Json Data")
    })
    @PreAuthorize("hasAuthority('config_create')")
    public R createConfig(@RequestBody @Validated Config config) {
        return configService.save(config) ? R.ok() : R.fail();
    }

    @PutMapping
    @ApiOperation(value = "修改系统参数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sysUser", value = "config Json Data")
    })
    @PreAuthorize("hasAuthority('config_update')")
    public R updateConfig(@RequestBody @Validated Config config) {
        return configService.save(config) ? R.ok() : R.fail();
    }

}
