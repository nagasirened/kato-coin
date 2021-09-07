package com.naga.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 持仓账户流水
 * @TableName forex_account_detail
 */
@TableName(value ="forex_account_detail")
@Data
@ApiModel(value = "持仓账户流水")
public class ForexAccountDetail implements Serializable {
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 持仓账户ID
     */
    @ApiModelProperty(value = "持仓账户ID")
    @TableField(value = "account_id")
    private Long accountId;

    /**
     * 收支类型：开仓；2-平仓；
     */
    @ApiModelProperty(value = "收支类型：开仓；2-平仓；")
    @TableField(value = "type")
    private Byte type;

    /**
     * 持仓量
     */
    @ApiModelProperty(value = "持仓量")
    @TableField(value = "amount")
    private BigDecimal amount;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    @TableField(value = "remark")
    private String remark;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @TableField(value = "created", fill = FieldFill.INSERT)
    private Date created;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}