package com.naga.config.swagger;


import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableSwagger2
@EnableConfigurationProperties(SwaggerProperties.class) // 读取Swagger2的配置类
public class SwaggerAutoConfiguration {

    private SwaggerProperties swaggerProperties;

    public SwaggerAutoConfiguration(SwaggerProperties swaggerProperties) {
        this.swaggerProperties = swaggerProperties;
    }

    @Bean
    public Docket docket() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(swaggerProperties.getBasePackage()))
                .paths(PathSelectors.any())
                .build();
        /** 安全的配置 */
        docket.securitySchemes(securitySchemes())               // 安全的规则
                .securityContexts(securityContexts());          // 安全配置的上下文
        return docket;
    }

    /**
     * api 信息的简介
     *
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .contact(new Contact(swaggerProperties.getName(), swaggerProperties.getUrl(), swaggerProperties.getEmail()))  // 联系人
                .title(swaggerProperties.getTitle())                                                                          // 标题
                .description(swaggerProperties.getDescription())                                                              // 描述信息
                .version(swaggerProperties.getVersion())                                                                      // 版本
                .termsOfServiceUrl(swaggerProperties.getTermsOfServiceUrl())                                                  // 团队信息
                .build();

    }

    /**
     * 安全的规则配置
     * 1.name    为参数名   name在swagger鉴权中使用
     * 2.keyname 是页面传值显示的 keyname，就是Header的key
     * @return
     */
    private List<SecurityScheme> securitySchemes() {
        return Arrays.asList(new ApiKey("Authorization", "Authorization", "header"));
    }

    /**
     * 安全的上下文
     *
     * @return
     */
    private List<SecurityContext> securityContexts() {
        return Arrays.asList(new SecurityContext(
                Arrays.asList(new SecurityReference("Authorization",
                        new AuthorizationScope[]{new AuthorizationScope("global", "accessResource")})),
                PathSelectors.any()
        ));
    }


}
