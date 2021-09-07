package com.naga.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 创新交易持仓信息
 * @TableName forex_account
 */
@TableName(value ="forex_account")
@Data
@ApiModel(value = "创新交易持仓信息")
public class ForexAccount implements Serializable {
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID")
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 交易对ID
     */
    @ApiModelProperty(value = "交易对ID")
    @TableField(value = "market_id")
    private Long marketId;

    /**
     * 交易对
     */
    @ApiModelProperty(value = "交易对")
    @TableField(value = "market_name")
    private String marketName;

    /**
     * 持仓方向：1-买；2-卖
     */
    @ApiModelProperty(value = "持仓方向：1-买；2-卖")
    @TableField(value = "type")
    private Byte type;

    /**
     * 持仓量
     */
    @ApiModelProperty(value = "持仓量")
    @TableField(value = "amount")
    private BigDecimal amount;

    /**
     * 冻结持仓量
     */
    @ApiModelProperty(value = "冻结持仓量")
    @TableField(value = "lock_amount")
    private BigDecimal lockAmount;

    /**
     * 状态：1-有效；2-锁定；
     */
    @ApiModelProperty(value = "状态：1-有效；2-锁定")
    @TableField(value = "status")
    private Byte status;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    @TableField(value = "last_update_time", fill = FieldFill.INSERT_UPDATE)
    private Date lastUpdateTime;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @TableField(value = "created", fill = FieldFill.INSERT)
    private Date created;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}