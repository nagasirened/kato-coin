package com.naga.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.checkerframework.checker.units.qual.A;

/**
 * 开仓订单信息
 * @TableName forex_open_position_order
 */
@TableName(value ="forex_open_position_order")
@Data
@ApiModel("开仓订单信息")
public class ForexOpenPositionOrder implements Serializable {
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
     * 结算币种
     */
    @ApiModelProperty(value = "结算币种")
    @TableField(value = "coin_id")
    private Long coinId;

    /**
     * 持仓方向：1-买；2-卖
     */
    @ApiModelProperty(value = "持仓方向：1-买；2-卖")
    @TableField(value = "type")
    private Byte type;

    /**
     * 资金账户ID
     */
    @ApiModelProperty(value = "资金账户ID")
    @TableField(value = "account_id")
    private Long accountId;

    /**
     * 委托订单
     */
    @ApiModelProperty(value = "委托订单")
    @TableField(value = "entrust_order_id")
    private Long entrustOrderId;

    /**
     * 成交订单号
     */
    @ApiModelProperty(value = "成交订单号")
    @TableField(value = "order_id")
    private Long orderId;

    /**
     * 成交价格
     */
    @ApiModelProperty(value = "成交价格")
    @TableField(value = "price")
    private BigDecimal price;

    /**
     * 成交数量
     */
    @ApiModelProperty(value = "成交数量")
    @TableField(value = "num")
    private BigDecimal num;

    /**
     * 扣除保证金
     */
    @ApiModelProperty(value = "扣除保证金")
    @TableField(value = "lock_margin")
    private BigDecimal lockMargin;

    /**
     * 平仓量
     */
    @ApiModelProperty(value = "平仓量")
    @TableField(value = "close_num")
    private BigDecimal closeNum;

    /**
     * 状态：1：未平仓；2-已平仓
     */
    @ApiModelProperty(value = "状态：1：未平仓；2-已平仓")
    @TableField(value = "status")
    private Byte status;

    /**
     * 修改时间
     */
    @TableField(value = "last_update_time", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "修改时间")
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