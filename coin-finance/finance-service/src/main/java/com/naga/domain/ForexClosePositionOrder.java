package com.naga.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 平仓详情
 * @TableName forex_close_position_order
 */
@TableName(value ="forex_close_position_order")
@Data
@ApiModel(value = "平仓详情")
public class ForexClosePositionOrder implements Serializable {
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
     * 交易对名称
     */
    @ApiModelProperty(value = "交易对名称")
    @TableField(value = "market_name")
    private String marketName;

    /**
     * 持仓方向：1-买；2-卖
     */
    @TableField(value = "持仓方向：1-买；2-卖")
    private Byte type;

    /**
     * 资金账户ID
     */
    @ApiModelProperty(value = "资金账户ID")
    @TableField(value = "account_id")
    private Long accountId;

    /**
     * 委托订单号
     */
    @ApiModelProperty(value = "委托订单号")
    @TableField(value = "entrust_order_id")
    private Long entrustOrderId;

    /**
     * 成交订单号
     */
    @ApiModelProperty(value = "成交订单号")
    @TableField(value = "order_id")
    private Long orderId;

    /**
     * 成交价
     */
    @ApiModelProperty(value = "成交价")
    @TableField(value = "price")
    private BigDecimal price;

    /**
     * 成交数量
     */
    @ApiModelProperty(value = "成交数量")
    @TableField(value = "num")
    private BigDecimal num;

    /**
     * 关联开仓订单号
     */
    @ApiModelProperty(value = "关联开仓订单号")
    @TableField(value = "open_id")
    private Long openId;

    /**
     * 平仓盈亏
     */
    @ApiModelProperty(value = "平仓盈亏")
    @TableField(value = "profit")
    private BigDecimal profit;

    /**
     * 返回还保证金
     */
    @ApiModelProperty(value = "返回还保证金")
    @TableField(value = "unlock_margin")
    private BigDecimal unlockMargin;

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