package com.naga.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 资金账户流水
 * @TableName account_detail
 */
@TableName(value ="account_detail")
@Data
public class AccountDetail implements Serializable {
    /**
     * 主键
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
     * 账户id
     */
    @ApiModelProperty(value = "账户id")
    @TableField(value = "account_id")
    private Long accountId;

    /**
     * 该笔流水资金关联方的账户id
     */
    @ApiModelProperty(value = "该笔流水资金关联方的账户id")
    @TableField(value = "ref_account_id")
    private Long refAccountId;

    /**
     * 订单ID
     */
    @ApiModelProperty(value = "订单ID")
    @TableField(value = "order_id")
    private Long orderId;

    /**
     * 入账为1，出账为2
     */
    @ApiModelProperty(value = "入账为1，出账为2")
    @TableField(value = "direction")
    private Boolean direction;

    /**
     * 业务类型:
        充值(recharge_into)
        提现审核通过(withdrawals_out)
        下单(order_create)
        成交(order_turnover)
        成交手续费(order_turnover_poundage)
        撤单(order_cancel)
        注册奖励(bonus_register)
        提币冻结解冻(withdrawals)
        充人民币(recharge)
        提币手续费(withdrawals_poundage)
        兑换(cny_btcx_exchange)
        奖励充值(bonus_into)
        奖励冻结(bonus_freeze)
     */
    @ApiModelProperty(value = "业务类型")
    @TableField(value = "business_type")
    private String businessType;

    /**
     * 资产数量
     */
    @ApiModelProperty(value = "资产数量")
    @TableField(value = "amount")
    private BigDecimal amount;

    /**
     * 手续费
     */
    @ApiModelProperty(value = "手续费")
    @TableField(value = "fee")
    private BigDecimal fee;

    /**
     * 流水状态：
        充值
        提现
        冻结
        解冻
        转出
        转入
     */
    @ApiModelProperty(value = "流水状态")
    @TableField(value = "remark")
    private String remark;

    /**
     * 写入日期
     */
    @ApiModelProperty(value = "写入日期")
    @TableField(value = "created", fill = FieldFill.INSERT)
    private Date created;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}