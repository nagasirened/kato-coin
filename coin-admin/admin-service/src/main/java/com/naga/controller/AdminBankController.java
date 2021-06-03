package com.naga.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.naga.domain.AdminBank;
import com.naga.model.R;
import com.naga.service.AdminBankService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = "公司银行卡的配置")
@RequestMapping("/adminBanks")
public class AdminBankController {

    @Autowired
    private AdminBankService adminBankService;

    @GetMapping
    @ApiOperation("公司银行卡分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "pageNo"),
            @ApiImplicitParam(name = "size", value = "pageSize"),
            @ApiImplicitParam(name = "bankCard", value = "公司银行卡"),
    })
    @PreAuthorize("hasAuthority('admin_bank_query')")
    public R<Page<AdminBank>> findByPage(Page<AdminBank> page, String bankCard) {
        return R.ok(adminBankService.findByPage(page, bankCard));
    }

    @PostMapping
    @ApiOperation(value = "新增公司银行卡")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "adminBank", value = "adminBank Json Data")
    })
    @PreAuthorize("hasAuthority('web_config_create')")
    public R createAdminBank(@RequestBody @Validated AdminBank adminBank) {
        return adminBankService.save(adminBank) ? R.ok() : R.fail();
    }


    @PutMapping
    @ApiOperation(value = "修改公司银行卡")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "adminBank", value = "adminBank Json Data")
    })
    @PreAuthorize("hasAuthority('web_config_update')")
    public R updateAdminBank(@RequestBody @Validated AdminBank adminBank) {
        return adminBankService.updateById(adminBank) ? R.ok() : R.fail();
    }

    @PutMapping("/switch")
    @ApiOperation(value = "启用/禁用银行卡")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bankId", value = "公司银行卡数据ID"),
            @ApiImplicitParam(name = "status", value = "状态"),
    })
    @PreAuthorize("hasAuthority('web_config_update')")
    public R updateAdminBank(Long bankId, Integer status) {
        AdminBank adminBank = AdminBank.builder().id(bankId).status(status).build();
        return adminBankService.updateById(adminBank) ? R.ok() : R.fail();
    }
}
