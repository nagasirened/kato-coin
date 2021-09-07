package com.naga.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 监测当前服务器Ip状态
 * @TableName coin_server
 */
@TableName(value ="coin_server")
@Data
public class CoinServer implements Serializable {
    /**
     * 自增id
     */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 钱包服务器ip
     */
    @ApiModelProperty(value = "钱包服务器ip")
    @TableField(value = "rpc_ip")
    private String rpcIp;

    /**
     * 钱包服务器port
     */
    @ApiModelProperty(value = "钱包服务器port")
    @TableField(value = "rpc_port")
    private String rpcPort;

    /**
     * 服务是否运行 0:正常,1:停止
     */
    @ApiModelProperty(value = "服务是否运行 0:正常,1:停止")
    @TableField(value = "running")
    private Integer running;

    /**
     * 钱包服务器区块高度
     */
    @ApiModelProperty(value = "钱包服务器区块高度")
    @TableField(value = "wallet_number")
    private Long walletNumber;

    /**
     * 币种名称
     */
    @ApiModelProperty(value = "币种名称")
    @TableField(value = "coin_name")
    private String coinName;

    /**
     * 备注信息
     */
    @ApiModelProperty(value = "备注信息")
    @TableField(value = "mark")
    private String mark;

    /**
     * 真实区块高度
     */
    @ApiModelProperty(value = "真实区块高度")
    @TableField(value = "real_number")
    private Long realNumber;

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