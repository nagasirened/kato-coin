package com.naga.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
    * 系统菜单
    */
@ApiModel(value="com-naga-domain-SysMenu")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "sys_menu")
public class SysMenu {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    @ApiModelProperty(value="主键")
    private Long id;

    /**
     * 上级菜单ID
     */
    @TableField(value = "parent_id")
    @ApiModelProperty(value="上级菜单ID")
    private Long parentId;

    /**
     * 上级菜单唯一KEY值
     */
    @TableField(value = "parent_key")
    @ApiModelProperty(value="上级菜单唯一KEY值")
    private String parentKey;

    /**
     * 类型 1-分类 2-节点
     */
    @TableField(value = "`type`")
    @ApiModelProperty(value="类型 1-分类 2-节点")
    private Byte type;

    /**
     * 名称
     */
    @TableField(value = "`name`")
    @ApiModelProperty(value="名称")
    private String name;

    /**
     * 描述
     */
    @TableField(value = "`desc`")
    @ApiModelProperty(value="描述")
    private String desc;

    /**
     * 目标地址
     */
    @TableField(value = "target_url")
    @ApiModelProperty(value="目标地址")
    private String targetUrl;

    /**
     * 排序索引
     */
    @TableField(value = "sort")
    @ApiModelProperty(value="排序索引")
    private Integer sort;

    /**
     * 状态 0-无效； 1-有效；
     */
    @TableField(value = "`status`")
    @ApiModelProperty(value="状态 0-无效； 1-有效；")
    private Byte status;

    /**
     * 创建人
     */
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    @ApiModelProperty(value="创建人")
    private Long createBy;

    /**
     * 修改人
     */
    @TableField(value = "modify_by", fill = FieldFill.UPDATE)
    @ApiModelProperty(value="修改人")
    private Long modifyBy;

    /**
     * 创建时间
     */
    @TableField(value = "created", fill = FieldFill.INSERT)
    @ApiModelProperty(value="创建时间")
    private Date created;

    /**
     * 修改时间
     */
    @TableField(value = "last_update_time", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value="修改时间")
    private Date lastUpdateTime;

    /**
     * 一个菜单对应多个权限
     */
    @TableField(exist = false)
    @ApiModelProperty("该菜单下的所有的权限")
    private List<SysPrivilege> privileges = Lists.newArrayList();

    /**
     * 菜单的子菜单
     */
    @TableField(exist = false)
    @ApiModelProperty("该菜单下的子菜单")
    private List<SysMenu> children = Lists.newArrayList();

    /**
     * 拼接菜单的唯一key即可
     */
    @TableField(exist = false)
    @ApiModelProperty("该菜单的唯一key值")
    private String menuKey;

    public String getMenuKey() {
        if (Objects.nonNull(parentId)) {
            return parentId + "." + id;
        }
        return String.valueOf(id);
    }
}