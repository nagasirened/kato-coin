package com.naga.feign;


import com.naga.config.feign.OAuth2FeignConfig;
import com.naga.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "member-service", configuration = OAuth2FeignConfig.class, path = "/users")
public interface UserServiceFeign {

    /**
     * 用于admin-service 里面远程调用member-service
     * @param ids   用户主键集合
     * @return  List<UserDto>
     */
    @GetMapping("/basic/users")
    List<UserDto> getBasicUsers(@RequestParam("ids") List<Long> ids);
}
