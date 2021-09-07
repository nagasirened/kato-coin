package com.naga.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 数字货币充值记录
 * @TableName coin_recharge
 */
@TableName(value ="coin_recharge")
@Data
public class CoinRecharge implements Serializable {
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
     * 币种名称
     */
    @ApiModelProperty(value = "币种名称")
    @TableField(value = "coin_name")
    private String coinName;

    /**
     * 币种类型
     */
    @ApiModelProperty(value = "币种类型")
    @TableField(value = "coin_type")
    private String coinType;

    /**
     * 钱包地址
     */
    @ApiModelProperty(value = "钱包地址")
    @TableField(value = "address")
    private String address;

    /**
     * 充值确认数
     */
    @ApiModelProperty(value = "充值确认数")
    @TableField(value = "confirm")
    private Integer confirm;

    /**
     * 状态：0-待入帐；1-充值失败，2到账失败，3到账成功
     */
    @ApiModelProperty(value = "状态：0-待入帐；1-充值失败，2到账失败，3到账成功")
    @TableField(value = "status")
    private Integer status;

    /**
     * 交易id
     */
    @ApiModelProperty(value = "交易id")
    @TableField(value = "txid")
    private String txid;

    /**
     * 金额
     */
    @ApiModelProperty(value = "金额")
    @TableField(value = "amount")
    private BigDecimal amount;

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