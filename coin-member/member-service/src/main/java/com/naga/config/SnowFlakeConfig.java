package com.naga.config;


import cn.hutool.core.lang.Snowflake;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SnowFlakeConfig {

    @Value("${snowflake.workerId:0}")
    private Long workerId;

    @Value("${snowflake.dataCenterId:2}")
    private Long dataCenterId;

    @Bean
    public Snowflake snowflake() {
        return new Snowflake(workerId, dataCenterId);
    }

}
