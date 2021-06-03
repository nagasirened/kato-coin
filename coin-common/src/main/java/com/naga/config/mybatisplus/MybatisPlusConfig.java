package com.naga.config.mybatisplus;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.incrementer.IKeyGenerator;
import com.baomidou.mybatisplus.extension.incrementer.H2KeyGenerator;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.naga.mapper")
public class MybatisPlusConfig {

    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor(){
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        paginationInterceptor.setDbType(DbType.MYSQL) ;
        return paginationInterceptor ;
    }

    /**
     * 乐观锁
     * 使用方式：在字段上增加注解即可 @Version
     * 仅支持 updateById(id) 与 update(entity, wrapper) 方法
     * 在 update(entity, wrapper) 方法下, wrapper 不能复用!!!
     */
    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor(){
        OptimisticLockerInterceptor optimisticLockerInterceptor = new OptimisticLockerInterceptor();
        return optimisticLockerInterceptor ;
    }

    /**
     * 主键序列的生成
     * 1. ID_WORK 数字类型
     * 2. ID_WORK_STR 字符串类型
     */
    @Bean
    public IKeyGenerator iKeyGenerator(){
        H2KeyGenerator h2KeyGenerator = new H2KeyGenerator();
        return h2KeyGenerator ;
    }

}