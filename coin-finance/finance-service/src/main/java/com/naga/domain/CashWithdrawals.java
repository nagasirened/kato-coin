package com.naga.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 提现表
 * @TableName cash_withdrawals
 */
@TableName(value ="cash_withdrawals")
@Data
public class CashWithdrawals implements Serializable {
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
     * 币种ID
     */
    @ApiModelProperty(value = "币种ID")
    @TableField(value = "coin_id")
    private Long coinId;

    /**
     * 资金账户ID
     */
    @ApiModelProperty(value = "资金账户ID")
    @TableField(value = "account_id")
    private Long accountId;

    /**
     * 数量（提现金额）
     */
    @ApiModelProperty(value = "数量（提现金额）")
    @TableField(value = "num")
    private BigDecimal num;

    /**
     * 手续费
     */
    @ApiModelProperty(value = "手续费")
    @TableField(value = "fee")
    private BigDecimal fee;

    /**
     * 到账金额
     */
    @ApiModelProperty(value = "到账金额")
    @TableField(value = "mum")
    private BigDecimal mum;

    /**
     * 开户人
     */
    @ApiModelProperty(value = "开户人")
    @TableField(value = "truename")
    private String truename;

    /**
     * 银行名称
     */
    @ApiModelProperty(value = "银行名称")
    @TableField(value = "bank")
    private String bank;

    /**
     * 银行所在省
     */
    @ApiModelProperty(value = "银行所在省")
    @TableField(value = "bank_prov")
    private String bankProv;

    /**
     * 银行所在市
     */
    @ApiModelProperty(value = "银行所在市")
    @TableField(value = "bank_city")
    private String bankCity;

    /**
     * 开户行
     */
    @ApiModelProperty(value = "开户行")
    @TableField(value = "bank_addr")
    private String bankAddr;

    /**
     * 银行账号
     */
    @ApiModelProperty(value = "银行账号")
    @TableField(value = "bank_card")
    private String bankCard;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    @TableField(value = "remark")
    private String remark;

    /**
     * 当前审核级数
     */
    @ApiModelProperty(value = "当前审核级数")
    @TableField(value = "step")
    private Byte step;

    /**
     * 状态：0-待审核；1-审核通过；2-拒绝；3-提现成功；
     */
    @ApiModelProperty(value = "状态：0-待审核；1-审核通过；2-拒绝；3-提现成功")
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
     * 最后确认提现到账时间
     */
    @ApiModelProperty(value = "最后确认提现到账时间")
    @TableField(value = "last_time")
    private Date lastTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}