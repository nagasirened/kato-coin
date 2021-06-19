package com.naga.controller;


import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.naga.domain.UserBank;
import com.naga.model.R;
import com.naga.service.UserBankService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/userBanks")
@Api(tags = "会员的银行卡管理")
public class UserBankController {

    @Autowired
    private UserBankService userBankService;

    @GetMapping
    @ApiOperation(value = "分页查询用户的银行卡")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "会员的Id"),
            @ApiImplicitParam(name = "current", value = "当前页"),
            @ApiImplicitParam(name = "size", value = "每页显示的条数")
    })
    @PreAuthorize("hasAuthority('user_bank_query')")
    public R<Page<UserBank>> findByPage(Page<UserBank> page, Long userId){
        page.addOrder(OrderItem.desc("last_update_time"));
        Page<UserBank> userBankPage = userBankService.findByPage(page, userId);
        return R.ok(userBankPage);
    }

    @PutMapping("/status")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "银行卡的Id"),
            @ApiImplicitParam(name = "status", value = "银行卡的状态"),
    })
    @ApiOperation(value = "修改银行卡的状态")
    @PreAuthorize("hasAuthority('user_bank_update')")
    public R updateStatus(Long id, Integer status){
        UserBank userBank = new UserBank();
        userBank.setId(id);
        userBank.setStatus(status);
        return userBankService.updateById(userBank) ? R.ok() : R.fail();
    }

    @PutMapping
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userBank", value = "银行卡的json数据"),
    })
    @ApiOperation(value = "修改银行卡")
    @PreAuthorize("hasAuthority('user_bank_update')")
    public R updateUserBank(@RequestBody @Validated UserBank userBank){
        return userBankService.updateById(userBank) ? R.ok() : R.fail();
    }

    @PostMapping
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userBank", value = "银行卡的json数据"),
    })
    @ApiOperation(value = "新增银行卡")
    @PreAuthorize("hasAuthority('user_bank_create')")
    public R createUserBank(@RequestBody @Validated UserBank userBank){
        return userBankService.save(userBank) ? R.ok() : R.fail();
    }

    @GetMapping("/current")
    @ApiOperation(value = "查询用户的卡号")
    public R<UserBank> currentUserBank(){
        UserBank userBank = userBankService.getCurrentUserBank();
        return R.ok(userBank);
    }

    @PostMapping("/bind")
    @ApiOperation(value = "绑定银行卡")
    public R bindBank(@RequestBody @Validated UserBank userBank) {
        return userBankService.bindBank(userBank) ? R.ok() : R.fail();
    }
}

