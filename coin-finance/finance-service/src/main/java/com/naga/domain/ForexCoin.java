package com.naga.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.checkerframework.checker.units.qual.A;

/**
 * 创新交易币种表
 * @TableName forex_coin
 */
@TableName(value ="forex_coin")
@Data
@ApiModel("创新交易币种表")
public class ForexCoin implements Serializable {
    /**
     * 
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
     * 排序
     */
    @ApiModelProperty(value = "排序")
    @TableField(value = "sort")
    private Byte sort;

    /**
     * 状态: 0禁用 1启用
     */
    @ApiModelProperty(value = "状态: 0禁用 1启用")
    @TableField(value = "status")
    private Boolean status;

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