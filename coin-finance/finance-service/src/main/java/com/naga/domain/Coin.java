package com.naga.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 币种配置信息
 * @TableName coin
 */
@TableName(value ="coin")
@Data
public class Coin implements Serializable {
    /**
     * 币种ID
     */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 币种名称
     */
    @ApiModelProperty(value = "币种名称")
    @TableField(value = "name")
    private String name;

    /**
     * 币种标题
     */
    @ApiModelProperty(value = "币种标题")
    @TableField(value = "title")
    private String title;

    /**
     * 币种logo
     */
    @ApiModelProperty(value = "币种logo")
    @TableField(value = "img")
    private String img;

    /**
     *  xnb：人民币
        default：比特币系列
        ETH：以太坊
        ethToken：以太坊代币
     */
    @ApiModelProperty(value = "币种类型")
    @TableField(value = "type")
    private String type;

    /**
     * rgb：认购币 qbb：钱包币
     */
    @ApiModelProperty(value = "rgb：认购币 qbb：钱包币")
    @TableField(value = "wallet")
    private String wallet;

    /**
     * 小数位数
     */
    @ApiModelProperty(value = "小数位数")
    @TableField(value = "round")
    private Byte round;

    /**
     * 最小提现单位
     */
    @ApiModelProperty(value = "最小提现单位")
    @TableField(value = "base_amount")
    private BigDecimal baseAmount;

    /**
     * 单笔最小提现数量
     */
    @ApiModelProperty(value = "单笔最小提现数量")
    @TableField(value = "min_amount")
    private BigDecimal minAmount;

    /**
     * 单笔最大提现数量
     */
    @ApiModelProperty(value = "单笔最大提现数量")
    @TableField(value = "max_amount")
    private BigDecimal maxAmount;

    /**
     * 当日最大提现数量
     */
    @ApiModelProperty(value = "当日最大提现数量")
    @TableField(value = "day_max_amount")
    private BigDecimal dayMaxAmount;

    /**
     * status=1：启用0：禁用
     */
    @ApiModelProperty(value = "status1：启用0：禁用")
    @TableField(value = "status")
    private Byte status;

    /**
     * 自动转出数量
     */
    @ApiModelProperty(value = "自动转出数量")
    @TableField(value = "auto_out")
    private Double autoOut;

    /**
     * 手续费率
     */
    @ApiModelProperty(value = "手续费率")
    @TableField(value = "rate")
    private Double rate;

    /**
     * 最低收取手续费个数
     */
    @ApiModelProperty(value = "最低收取手续费个数")
    @TableField(value = "min_fee_num")
    private BigDecimal minFeeNum;

    /**
     * 提现开关
     */
    @ApiModelProperty(value = "提现开关")
    @TableField(value = "withdraw_flag")
    private Byte withdrawFlag;

    /**
     * 充值开关
     */
    @ApiModelProperty(value = "充值开关")
    @TableField(value = "recharge_flag")
    private Byte rechargeFlag;

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