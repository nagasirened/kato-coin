package com.naga.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
    * 实名认证审核信息
    */
@ApiModel(value="com-naga-domain-UserAuthAuditRecord")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "user_auth_audit_record")
public class UserAuthAuditRecord {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    @ApiModelProperty(value="主键")
    private Long id;

    /**
     * 对应user_auth_info表code
     */
    @TableField(value = "auth_code")
    @ApiModelProperty(value="对应user_auth_info表code")
    private Long authCode;

    /**
     * 用户ID
     */
    @TableField(value = "user_id")
    @ApiModelProperty(value="用户ID")
    private Long userId;

    /**
     * 状态1同意2拒絕
     */
    @TableField(value = "`status`")
    @ApiModelProperty(value="状态1同意2拒絕")
    private Integer status;

    /**
     * 备注
     */
    @TableField(value = "remark")
    @ApiModelProperty(value="备注")
    private String remark;

    /**
     * 当前审核级数
     */
    @TableField(value = "step")
    @ApiModelProperty(value="当前审核级数")
    private Byte step;

    /**
     * 审核人ID
     */
    @TableField(value = "audit_user_id")
    @ApiModelProperty(value="审核人ID")
    private Long auditUserId;

    /**
     * 审核人
     */
    @TableField(value = "audit_user_name")
    @ApiModelProperty(value="审核人")
    private String auditUserName;

    /**
     * 创建时间
     */
    @TableField(value = "created")
    @ApiModelProperty(value="创建时间")
    private Date created;
}