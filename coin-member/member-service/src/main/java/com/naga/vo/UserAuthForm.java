package com.naga.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UserAuthForm extends GeetestForm{

    @NotEmpty
    @ApiModelProperty(value = "用户的真实姓名")
    private String realName;

    @NotNull
    @ApiModelProperty(value = "用户的证件类型")
    private Integer idCardType;

    @NotEmpty
    @ApiModelProperty(value = "用户的证件号码")
    private String idCard;
}
