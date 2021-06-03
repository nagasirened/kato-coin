package com.naga.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

/** 开启授权服务器 */
@EnableAuthorizationServer
@Configuration
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    @Qualifier(value = "coinUserDetailService")
    private UserDetailsService coinUserDetailService;

    /**
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;
     */

    /**
     * 添加第三方客户端, 一般来说会使用数据库去存储和加载
     * @param clients  第三方客户端
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        /** 在内存中加一个默认的cliendId和ClientSecret */
        clients.inMemory()
                .withClient("coin-api")                                     // 第三方客户端的名称
                .secret(passwordEncoder.encode("coin-secret"))         // 第三方客户端的密钥
                /* FIXME 加入refresh_token之后，会多出来一种认证方式 */
                .authorizedGrantTypes("authorization_code", "password", "refresh_token")  //允许授权类型
                .scopes("all")                                                      // 第三方客户端的授权范围
                .accessTokenValiditySeconds(3600 * 24 * 100)                               // ak的有效期
                .refreshTokenValiditySeconds(3600 * 24 * 300)                              // rk的有效期

                // 应用之间内部访问的 OAuth2FeignRequestInterceptor 中的getToken()方法
                .and()
                .withClient("inside-app")
                .secret(passwordEncoder.encode("inside-secret"))
                .authorizedGrantTypes("client_credentials")
                .scopes("all")
                .accessTokenValiditySeconds(3600 * 24 * 7)
                ;

        super.configure(clients);
    }
    /**
     * 配置验证管理器
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager)
                .userDetailsService(coinUserDetailService)
                // .tokenStore(redisTokenStore())  // 使用redis存储token
                .tokenStore(jwtTokenStore())
                .tokenEnhancer(jwtAccessTokenConverter())
        ;
        super.configure(endpoints);
    }

    private TokenStore jwtTokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();

        // 加载私钥
        ClassPathResource classPathResource = new ClassPathResource("coinexchange.jks");
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(classPathResource, "coinexchange".toCharArray());
        converter.setKeyPair(keyStoreKeyFactory.getKeyPair("coinexchange", "coinexchange".toCharArray()));
        return converter;
    }

    /**
    public TokenStore redisTokenStore(){
        return new RedisTokenStore(redisConnectionFactory);
    }
    */


    public static void main(String[] args) {
        double log = Math.log(8) / Math.log(2);
        System.out.println(log);
    }

}
