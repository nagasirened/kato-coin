package com.naga.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 充值审核记录
 * @TableName cash_recharge_audit_record
 */
@TableName(value ="cash_recharge_audit_record")
@Data
public class CashRechargeAuditRecord implements Serializable {
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 充值订单号
     */
    @ApiModelProperty(value = "充值订单号")
    @TableField(value = "order_id")
    private Long orderId;

    /**
     * 状态
     */
    @ApiModelProperty(value = "状态")
    @TableField(value = "status")
    private Byte status;

    /**
     * 审核备注
     */
    @ApiModelProperty(value = "审核备注")
    @TableField(value = "remark")
    private String remark;

    /**
     * 当前审核级数
     */
    @ApiModelProperty(value = "当前审核级数")
    @TableField(value = "step")
    private Byte step;

    /**
     * 审核人ID
     */
    @ApiModelProperty(value = "审核人ID")
    @TableField(value = "audit_user_id")
    private Long auditUserId;

    /**
     * 审核人
     */
    @ApiModelProperty(value = "审核人")
    @TableField(value = "audit_user_name")
    private String auditUserName;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @TableField(value = "created", fill = FieldFill.INSERT)
    private Date created;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}