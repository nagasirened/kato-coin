package com.naga.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 数字货币提现记录
 * @TableName coin_withdraw
 */
@TableName(value ="coin_withdraw")
@Data
@ApiModel(value = "数字货币提现记录")
public class CoinWithdraw implements Serializable {
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
     * 交易id
     */
    @ApiModelProperty(value = "交易id")
    @TableField(value = "txid")
    private String txid;

    /**
     * 提现量
     */
    @ApiModelProperty(value = "提现量")
    @TableField(value = "num")
    private BigDecimal num;

    /**
     * 手续费()
     */
    @ApiModelProperty(value = "手续费")
    @TableField(value = "fee")
    private BigDecimal fee;

    /**
     * 实际提现
     */
    @ApiModelProperty(value = "实际提现")
    @TableField(value = "mum")
    private BigDecimal mum;

    /**
     * 0站内1其他2手工提币
     */
    @ApiModelProperty(value = "0站内1其他2手工提币")
    @TableField(value = "type")
    private Boolean type;

    /**
     * 链上手续费花费
     */
    @ApiModelProperty(value = "链上手续费花费")
    @TableField(value = "chain_fee")
    private BigDecimal chainFee;

    /**
     * 区块高度
     */
    @ApiModelProperty(value = "区块高度")
    @TableField(value = "block_num")
    private Integer blockNum;

    /**
     * 后台审核人员提币备注备注
     */
    @ApiModelProperty(value = "后台审核人员提币备注备注")
    @TableField(value = "remark")
    private String remark;

    /**
     * 钱包提币备注备注
     */
    @ApiModelProperty(value = "钱包提币备注备注")
    @TableField(value = "wallet_mark")
    private String walletMark;

    /**
     * 当前审核级数
     */
    @ApiModelProperty(value = "当前审核级数")
    @TableField(value = "step")
    private Byte step;

    /**
     * 状态：0-审核中；1-成功；2-拒绝；3-撤销；4-审核通过；5-打币中；
     */
    @ApiModelProperty(value = "状态：0-审核中；1-成功；2-拒绝；3-撤销；4-审核通过；5-打币中")
    @TableField(value = "status")
    private Boolean status;

    /**
     * 审核时间
     */
    @ApiModelProperty(value = "审核时间")
    @TableField(value = "audit_time")
    private Date auditTime;

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