package com.naga.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 币种配置信息
 * @TableName coin_config
 */
@TableName(value ="coin_config")
@Data
public class CoinConfig implements Serializable {
    /**
     * 币种ID(对应coin表ID)
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
     * btc-比特币系列；eth-以太坊；ethToken-以太坊代币；etc-以太经典；\r\n\r\n
     */
    @ApiModelProperty(value = "btc-比特币系列；eth-以太坊；ethToken-以太坊代币；etc-以太经典")
    @TableField(value = "coin_type")
    private String coinType;

    /**
     * 钱包最低留存的币
     */
    @ApiModelProperty(value = "钱包最低留存的币")
    @TableField(value = "credit_limit")
    private BigDecimal creditLimit;

    /**
     * 当触发改状态的时候,开始归集
     */
    @ApiModelProperty(value = "当触发改状态的时候,开始归集")
    @TableField(value = "credit_max_limit")
    private BigDecimal creditMaxLimit;

    /**
     * rpc服务ip
     */
    @ApiModelProperty(value = "rpc服务ip")
    @TableField(value = "rpc_ip")
    private String rpcIp;

    /**
     * rpc服务port
     */
    @ApiModelProperty(value = "rpc服务port")
    @TableField(value = "rpc_port")
    private String rpcPort;

    /**
     * rpc用户
     */
    @ApiModelProperty(value = "rpc用户")
    @TableField(value = "rpc_user")
    private String rpcUser;

    /**
     * rpc密码
     */
    @ApiModelProperty(value = "rpc密码")
    @TableField(value = "rpc_pwd")
    private String rpcPwd;

    /**
     * 最后一个区块
     */
    @ApiModelProperty(value = "最后一个区块")
    @TableField(value = "last_block")
    private String lastBlock;

    /**
     * 钱包用户名
     */
    @ApiModelProperty(value = "钱包用户名")
    @TableField(value = "wallet_user")
    private String walletUser;

    /**
     * 钱包密码
     */
    @ApiModelProperty(value = "钱包密码")
    @TableField(value = "wallet_pass")
    private String walletPass;

    /**
     * 代币合约地址
     */
    @ApiModelProperty(value = "代币合约地址")
    @TableField(value = "contract_address")
    private String contractAddress;

    /**
     * context
     */
    @ApiModelProperty(value = "context")
    @TableField(value = "context")
    private String context;

    /**
     * 最低确认数
     */
    @ApiModelProperty(value = "最低确认数")
    @TableField(value = "min_confirm")
    private Integer minConfirm;

    /**
     * 定时任务
     */
    @ApiModelProperty(value = "定时任务")
    @TableField(value = "task")
    private String task;

    /**
     * 是否可用0不可用,1可用
     */
    @ApiModelProperty(value = "是否可用0不可用,1可用")
    @TableField(value = "status")
    private Integer status;

    /**
     * 自动绘制限制
     */
    @ApiModelProperty(value = "自动绘制限制")
    @TableField(value = "auto_draw_limit")
    private BigDecimal autoDrawLimit;

    /**
     * 自动绘制
     */
    @ApiModelProperty(value = "自动绘制")
    @TableField(value = "auto_draw")
    private Integer autoDraw;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}