package com.naga.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.naga.domain.Coin;
import com.naga.model.R;
import com.naga.service.CoinService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/coins")
@Api(tags = "数字货币的数据接口")
public class CoinController {

    @Autowired
    private CoinService coinService;

    @GetMapping
    @ApiOperation(value = "分页条件查询数字货币")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name" ,value = "数字货币的名称") ,
            @ApiImplicitParam(name = "type" ,value = "数字货币类型的名称") ,
            @ApiImplicitParam(name = "status" ,value = "数字货币类型的状态") ,
            @ApiImplicitParam(name = "title" ,value = "数字货币类型的标题") ,
            @ApiImplicitParam(name = "wallet_type" ,value = "数字货币钱包类型") ,
            @ApiImplicitParam(name = "current" ,value = "当前页") ,
            @ApiImplicitParam(name = "size" ,value = "每页显示的条数") ,
    })
    public R<Page<Coin>> findByPage(String name, String type, Byte status, String title, String walletType, @ApiIgnore Page<Coin> page){
        Page<Coin> coinPage =  coinService.findByPage(name, type, status, title, walletType, page) ;
        return R.ok(coinPage) ;
    }
}
