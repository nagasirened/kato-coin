package com.naga.filter;

import com.alibaba.fastjson.JSONObject;
import com.google.common.net.HttpHeaders;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Set;


@Component
public class JwtCheckFilter implements GlobalFilter, Ordered {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Value("${security.pass.paths:/admin/login,/user/gt/register,/user/login,/user/users/register}")
    private Set<String> passPaths;


    /**
     * 拦截顺序，
     * @return
     */
    @Override
    public int getOrder() {
        return 0;
    }

    /**
     * 过滤器逻辑:接口必须包含token
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1.如果接口需要token才能访问
        if (!isRequireToken(exchange)) {
            return chain.filter(exchange);
        }
        // 2.获取用户的token
        String token = getUserToken(exchange);
        // 判断token的有效性
        if (StringUtils.isEmpty(token)) {
            return buildNoAuthorizationResult(exchange);
        }
        if (stringRedisTemplate.hasKey(token)) {
            return chain.filter(exchange);
        }
        return buildNoAuthorizationResult(exchange);
    }

    /**
     * 校验当前请求路径是否需要验证token
     * @param exchange
     * @return
     */
    private boolean isRequireToken(ServerWebExchange exchange) {
        String path = exchange.getRequest().getURI().getPath();
        if (passPaths.contains(path)) {
            return false;
        }
        return true;
    }

    /**
     * 获取token
     * @param exchange
     * @return
     */
    private String getUserToken(ServerWebExchange exchange) {
        String bearerToken = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        return StringUtils.isEmpty(bearerToken) ?
                null : bearerToken.replace("bearer ", "");
    }

    /**
     * 给用户响应一个没有通过认证的错误
     * @return
     */
    private Mono<Void> buildNoAuthorizationResult(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().set("Content-Type", "application/json");
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        JSONObject result = new JSONObject();
        result.put("data", null);
        result.put("code", "401");
        result.put("msg", "token is not exist");
        DataBuffer wrap = response.bufferFactory().wrap(result.toJSONString().getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Flux.just(wrap));
    }


}
