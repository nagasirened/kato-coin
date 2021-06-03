package com.naga.config.jetcache;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCreateCacheAnnotation
@EnableMethodCache(basePackages = "com.naga.service.impl")
public class JetCacheConfig {

}