package com.naga.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 阿里云：身份证二要素服务
 * 购买之后，搜索云市场，记录自己的 AppKey:203948903
 * AppSecret:RsAxG2Hfopki0l4OCgn7wEInTY59Vllc 和 AppCode:bd6ccd97b0ec45999fdf6a9dee7bb6d3
 */

@Data
@ConfigurationProperties(prefix = "identity")
public class IdProperties {

    /**
     * 身份认证的url地址
     * curl -i -k -X POST 'https://dfidveri.market.alicloudapi.com/verify_id_name'
     * -H 'Authorization:APPCODE 你自己的AppCode' --data 'id_number=445122********33&name=%E9%BB%84%E5%A4%A7%E5%A4%A7'
     */
    private String url;

    private String appKey;

    private String appSecret;

    private String appCode;
}
