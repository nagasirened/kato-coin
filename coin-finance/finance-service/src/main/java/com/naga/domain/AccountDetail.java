package com.naga.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 资金账户流水
 * @TableName account_detail
 */
@TableName(value ="account_detail")
@Data
public class AccountDetail implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 币种id
     */
    @TableField(value = "coin_id")
    private Long coinId;

    /**
     * 账户id
     */
    @TableField(value = "account_id")
    private Long accountId;

    /**
     * 该笔流水资金关联方的账户id
     */
    @TableField(value = "ref_account_id")
    private Long refAccountId;

    /**
     * 订单ID
     */
    @TableField(value = "order_id")
    private Long orderId;

    /**
     * 入账为1，出账为2
     */
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
    @TableField(value = "business_type")
    private String businessType;

    /**
     * 资产数量
     */
    @TableField(value = "amount")
    private BigDecimal amount;

    /**
     * 手续费
     */
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
    @TableField(value = "remark")
    private String remark;

    /**
     * 日期
     */
    @TableField(value = "created")
    private Date created;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}