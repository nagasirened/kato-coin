package com.naga.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
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
     * 币种名：cny，人民币；
     */
    @ApiModelProperty(value = "币种名：cny，人民币")
    @TableField(value = "coin_name")
    private String coinName;

    /**
     * 数量（充值金额）
     */
    @ApiModelProperty(value = "数量（充值金额）")
    @TableField(value = "num")
    private BigDecimal num;

    /**
     * 手续费
     */
    @ApiModelProperty(value = "手续费")
    @TableField(value = "fee")
    private BigDecimal fee;

    /**
     * 手续费币种
     */
    @ApiModelProperty(value = "手续费币种")
    @TableField(value = "feecoin")
    private String feecoin;

    /**
     * 成交量（到账金额）
     */
    @ApiModelProperty(value = "成交量（到账金额）")
    @TableField(value = "mum")
    private BigDecimal mum;

    /**
     * 类型：alipay，支付宝；cai1pay，财易付；bank，银联；
     */
    @ApiModelProperty(value = "类型：alipay，支付宝；cai1pay，财易付；bank，银联")
    @TableField(value = "type")
    private String type;

    /**
     * 充值订单号
     */
    @ApiModelProperty(value = "充值订单号")
    @TableField(value = "tradeno")
    private String tradeno;

    /**
     * 第三方订单号
     */
    @ApiModelProperty(value = "第三方订单号")
    @TableField(value = "outtradeno")
    private String outtradeno;

    /**
     * 充值备注备注
     */
    @ApiModelProperty(value = "充值备注备注")
    @TableField(value = "remark")
    private String remark;

    /**
     * 审核备注
     */
    @ApiModelProperty(value = "审核备注")
    @TableField(value = "audit_remark")
    private String auditRemark;

    /**
     * 当前审核级数
     */
    @ApiModelProperty(value = "当前审核级数")
    @TableField(value = "step")
    private Byte step;

    /**
     * 状态：0-待审核；1-审核通过；2-拒绝；3-充值成功；
     */
    @ApiModelProperty(value = "状态：0-待审核；1-审核通过；2-拒绝；3-充值成功")
    @TableField(value = "status")
    private Byte status;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @TableField(value = "created", fill = FieldFill.INSERT)
    private Date created;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    @TableField(value = "last_update_time", fill = FieldFill.INSERT_UPDATE)
    private Date lastUpdateTime;

    /**
     * 银行卡账户名
     */
    @ApiModelProperty(value = "银行卡账户名")
    @TableField(value = "name")
    private String name;

    /**
     * 开户行
     */
    @ApiModelProperty(value = "开户行")
    @TableField(value = "bank_name")
    private String bankName;

    /**
     * 银行卡号
     */
    @ApiModelProperty(value = "银行卡号")
    @TableField(value = "bank_card")
    private String bankCard;

    /**
     * 最后确认到账时间。
     */
    @ApiModelProperty(value = "最后确认到账时间")
    @TableField(value = "last_time")
    private Date lastTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}