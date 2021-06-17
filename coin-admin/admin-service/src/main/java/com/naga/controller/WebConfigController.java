package com.naga.controller;


import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.naga.domain.WebConfig;
import com.naga.model.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Objects;

@RestController
@Api(tags = "资源管理控制器")
@RequestMapping("/webConfigs")
public class WebConfigController {

    @Autowired
    private WebConfigService webConfigService;

    @GetMapping
    @ApiOperation("banner分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "pageNo"),
            @ApiImplicitParam(name = "size", value = "pageSize"),
            @ApiImplicitParam(name = "name", value = "资源名称"),
            @ApiImplicitParam(name = "type", value = "资源类型")
    })
    @PreAuthorize("hasAuthority('webConfig_query')")
    public R<Page<WebConfig>> findByPage(Page<WebConfig> page, String name, String type) {
        page.addOrder(OrderItem.desc("created"));
        return R.ok(webConfigService.findByPage(page, name, type));
    }

    @PostMapping
    @ApiOperation(value = "新增banner资源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sysUser", value = "SysUser Json Data")
    })
    @PreAuthorize("hasAuthority('web_config_create')")
    public R createUser(@RequestBody @Validated WebConfig webConfig) {
        if (Objects.isNull(webConfig.getSort()))
            webConfig.setSort(0);
        return webConfigService.save(webConfig) ? R.ok() : R.fail();
    }

    @PutMapping
    @ApiOperation(value = "修改banner资源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sysUser", value = "SysUser Json Data")
    })
    @PreAuthorize("hasAuthority('web_config_update')")
    public R updateUser(@RequestBody @Validated WebConfig webConfig) {
        return webConfigService.updateById(webConfig) ? R.ok() : R.fail();
    }

    @DeleteMapping
    @ApiOperation(value = "删除资源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "要删除的ID集合")
    })
    @PreAuthorize("hasAuthority('web_config_delete')")
    public R deleteUser(Long[] ids) {
        return webConfigService.removeByIds(Arrays.asList(ids)) ? R.ok() : R.fail();
    }
}
