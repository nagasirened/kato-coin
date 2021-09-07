package com.naga.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
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
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 钱包服务器ip
     */
    @TableField(value = "rpc_ip")
    private String rpcIp;

    /**
     * 钱包服务器ip
     */
    @TableField(value = "rpc_port")
    private String rpcPort;

    /**
     * 服务是否运行 0:正常,1:停止
     */
    @TableField(value = "running")
    private Integer running;

    /**
     * 钱包服务器区块高度
     */
    @TableField(value = "wallet_number")
    private Long walletNumber;

    /**
     * 
     */
    @TableField(value = "coin_name")
    private String coinName;

    /**
     * 备注信息
     */
    @TableField(value = "mark")
    private String mark;

    /**
     * 真实区块高度
     */
    @TableField(value = "real_number")
    private Long realNumber;

    /**
     * 修改时间
     */
    @TableField(value = "last_update_time")
    private Date lastUpdateTime;

    /**
     * 创建时间
     */
    @TableField(value = "created")
    private Date created;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}