package com.naga.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 币种类型
 * @TableName coin_type
 */
@TableName(value ="coin_type")
@Data
public class CoinType implements Serializable {
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 代码
     */
    @ApiModelProperty(value = "代码")
    @TableField(value = "code")
    private String code;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    @TableField(value = "description")
    private String description;

    /**
     * 状态：0-无效；1-有效；
     */
    @ApiModelProperty(value = "状态：0-无效；1-有效")
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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}