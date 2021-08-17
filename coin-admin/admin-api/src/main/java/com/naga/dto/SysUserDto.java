package com.naga.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "远程获取系统用户基本信息")
public class SysUserDto {

    /**
     * 主键
     */
    @ApiModelProperty(value="主键")
    private Long id;

    /**
     * 账号
     */
    @ApiModelProperty(value="账号")
    private String username;

    /**
     * 姓名
     */
    @ApiModelProperty(value="姓名")
    private String fullName;

    /**
     * 手机号
     */
    @ApiModelProperty(value="手机号")
    private String mobile;

    /**
     * 邮箱
     */
    @ApiModelProperty(value="邮箱")
    private String email;

    /**
     * 状态 0-无效； 1-有效；
     */
    @ApiModelProperty(value="状态 0-无效； 1-有效；")
    private Byte status;
}
