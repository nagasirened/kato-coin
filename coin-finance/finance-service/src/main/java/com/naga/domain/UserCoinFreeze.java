package com.naga.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * @TableName user_coin_freeze
 */
@TableName(value ="user_coin_freeze")
@Data
public class UserCoinFreeze implements Serializable {
    /**
     * 
     */
    @TableId(value = "user_id")
    @ApiModelProperty(value = "用户ID")
    private Long userId;

    /**
     * 
     */
    @TableField(value = "coin_id")
    @ApiModelProperty(value = "币种ID")
    private Long coinId;

    /**
     * 
     */
    @TableField(value = "freeze")
    @ApiModelProperty(value = "冻结数量")
    private Integer freeze;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}