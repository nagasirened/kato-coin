package com.naga.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 币种余额
 * @TableName coin_balance
 */
@TableName(value ="coin_balance")
@Data
public class CoinBalance implements Serializable {
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 币种ID
     */
    @ApiModelProperty(value = "币种ID")
    @TableField(value = "coin_id")
    private Long coinId;

    /**
     * 币种名称
     */
    @ApiModelProperty(value = "币种名称")
    @TableField(value = "coin_name")
    private String coinName;

    /**
     * 系统余额（根据充值提币计算）
     */
    @ApiModelProperty(value = "系统余额（根据充值提币计算）")
    @TableField(value = "system_balance")
    private BigDecimal systemBalance;

    /**
     * 币种类型
     */
    @ApiModelProperty(value = "币种类型")
    @TableField(value = "coin_type")
    private String coinType;

    /**
     * 归集账户余额
     */
    @ApiModelProperty(value = "归集账户余额")
    @TableField(value = "collect_account_balance")
    private BigDecimal collectAccountBalance;

    /**
     * 钱包账户余额
     */
    @ApiModelProperty(value = "钱包账户余额")
    @TableField(value = "loan_account_balance")
    private BigDecimal loanAccountBalance;

    /**
     * 手续费账户余额(eth转账需要手续费)
     */
    @ApiModelProperty(value = "手续费账户余额(eth转账需要手续费)")
    @TableField(value = "fee_account_balance")
    private BigDecimal feeAccountBalance;

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

    /**
     * 充值帐户余额
     */
    @ApiModelProperty(value = "充值帐户余额")
    @TableField(value = "recharge_account_balance")
    private BigDecimal rechargeAccountBalance;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}