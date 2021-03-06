package com.naga.config.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 该类使用在FeignClient中用于token的传递，即服务之间相互调用时，需要传递token进行用户验证
 */
@Slf4j
public class OAuth2FeignConfig implements RequestInterceptor {

    /**
     * Called for every request. Add data using methods on the supplied {@link RequestTemplate}.
     *
     * @param template
     */
    @Override
    public void apply(RequestTemplate template) {
        // 1 我们可以从request的上下文环境里面获取token
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            log.info("没有请求的上下文,故无法进行token的传递");
        }
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        String header = request.getHeader(HttpHeaders.AUTHORIZATION); // 获取我们请求上下文的头里面的AUTHORIZATION
        if (!StringUtils.isEmpty(header)) {
            template.header(HttpHeaders.AUTHORIZATION, header);
            log.info("本次token传递成功,token的值为:{}", header);
        }
    }
}