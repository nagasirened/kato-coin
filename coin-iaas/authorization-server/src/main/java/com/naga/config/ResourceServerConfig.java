package com.naga.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;


/**
 * 资源服务器配置
 */
@EnableResourceServer
@Configuration
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    /**
     * 资源服务器的默认实现：在Controller中的/user/info接口中，从header中获取token，然后默认向认证服务器获取 Principal 信息
     */

}
