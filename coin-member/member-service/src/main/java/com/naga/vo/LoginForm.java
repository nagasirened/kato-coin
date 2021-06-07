package com.naga.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@ApiModel(value = "前端的登录参数")
public class LoginForm extends GeetestForm{

    @ApiModelProperty(value = "电话的国家区号")
    private String countryCode;

    @ApiModelProperty(value = "用户名称")
    private String username;

    @ApiModelProperty(value = "用户密码")
    private String password;

}