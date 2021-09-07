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
 * 充值表
 * @TableName cash_recharge
 */
@TableName(value ="cash_recharge")
@Data
public class CashRecharge implements Serializable {
    /**
     * 自增id
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
     * 币种名：cny，人民币；
     */
    @TableField(value = "coin_name")
    private String coinName;

    /**
     * 数量（充值金额）
     */
    @TableField(value = "num")
    private BigDecimal num;

    /**
     * 手续费
     */
    @TableField(value = "fee")
    private BigDecimal fee;

    /**
     * 手续费币种
     */
    @TableField(value = "feecoin")
    private String feecoin;

    /**
     * 成交量（到账金额）
     */
    @TableField(value = "mum")
    private BigDecimal mum;

    /**
     * 类型：alipay，支付宝；cai1pay，财易付；bank，银联；
     */
    @TableField(value = "type")
    private String type;

    /**
     * 充值订单号
     */
    @TableField(value = "tradeno")
    private String tradeno;

    /**
     * 第三方订单号
     */
    @TableField(value = "outtradeno")
    private String outtradeno;

    /**
     * 充值备注备注
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 审核备注
     */
    @TableField(value = "audit_remark")
    private String auditRemark;

    /**
     * 当前审核级数
     */
    @TableField(value = "step")
    private Byte step;

    /**
     * 状态：0-待审核；1-审核通过；2-拒绝；3-充值成功；
     */
    @TableField(value = "status")
    private Byte status;

    /**
     * 创建时间
     */
    @TableField(value = "created")
    private Date created;

    /**
     * 更新时间
     */
    @TableField(value = "last_update_time")
    private Date lastUpdateTime;

    /**
     * 银行卡账户名
     */
    @TableField(value = "name")
    private String name;

    /**
     * 开户行
     */
    @TableField(value = "bank_name")
    private String bankName;

    /**
     * 银行卡号
     */
    @TableField(value = "bank_card")
    private String bankCard;

    /**
     * 最后确认到账时间。
     */
    @TableField(value = "last_time")
    private Date lastTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}