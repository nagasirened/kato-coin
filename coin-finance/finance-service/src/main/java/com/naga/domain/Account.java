package com.naga.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户财产记录
 * @TableName account
 */
@TableName(value ="account")
@Data
public class Account implements Serializable {
    /**
     * 自增id
     */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 币种id
     */
    @ApiModelProperty(value = "币种id")
    @TableField(value = "coin_id")
    private Long coinId;

    /**
     * 账号状态：1，正常；2，冻结；
     */
    @ApiModelProperty(value = "账号状态：1，正常；2，冻结")
    @TableField(value = "status")
    private Boolean status;

    /**
     * 币种可用金额
     */
    @ApiModelProperty(value = "币种可用金额")
    @TableField(value = "balance_amount")
    private BigDecimal balanceAmount;

    /**
     * 币种冻结金额
     */
    @ApiModelProperty(value = "币种冻结金额")
    @TableField(value = "freeze_amount")
    private BigDecimal freezeAmount;

    /**
     * 累计充值金额
     */
    @ApiModelProperty(value = "累计充值金额")
    @TableField(value = "recharge_amount")
    private BigDecimal rechargeAmount;

    /**
     * 累计提现金额
     */
    @ApiModelProperty(value = "累计提现金额")
    @TableField(value = "withdrawals_amount")
    private BigDecimal withdrawalsAmount;

    /**
     * 净值
     */
    @ApiModelProperty(value = "净值")
    @TableField(value = "net_value")
    private BigDecimal netValue;

    /**
     * 占用保证金
     */
    @ApiModelProperty(value = "占用保证金")
    @TableField(value = "lock_margin")
    private BigDecimal lockMargin;

    /**
     * 持仓盈亏/浮动盈亏
     */
    @ApiModelProperty(value = "持仓盈亏/浮动盈亏")
    @TableField(value = "float_profit")
    private BigDecimal floatProfit;

    /**
     * 总盈亏
     */
    @ApiModelProperty(value = "总盈亏")
    @TableField(value = "total_profit")
    private BigDecimal totalProfit;

    /**
     * 充值地址
     */
    @ApiModelProperty(value = "充值地址")
    @TableField(value = "rec_addr")
    private String recAddr;

    /**
     * 版本号
     */
    @ApiModelProperty(value = "版本号")
    @TableField(value = "version")
    private Long version;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
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