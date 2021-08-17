package com.naga.feign;


import com.naga.config.feign.OAuth2FeignConfig;
import com.naga.dto.SysUserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "admin-service", configuration = OAuth2FeignConfig.class, path = "/admin")
public interface AdminServiceFeign {

    @GetMapping("/sysUser/info")
    SysUserDto getSysUserInfo(Long userId);
}
