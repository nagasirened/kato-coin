package com.naga.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * @TableName user_account_freeze
 */
@TableName(value ="user_account_freeze")
@Data
@ApiModel("用户资金冻结")
public class UserAccountFreeze implements Serializable {
    /**
     * 
     */
    @TableId(value = "user_id")
    @ApiModelProperty(value = "用户ID")
    private Long userId;

    /**
     * 
     */
    @ApiModelProperty(value = "冻结金额")
    @TableField(value = "freeze")
    private BigDecimal freeze;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}