package com.naga.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class UserInfoController {

    /**
     * 调用接口的时候，直接localhost:9999/user/info?access_token=8ba0c250-4fb4-47eb-90e3-c2f9fdd3f016 即可获取用户信息
     * 或者在header里面加 Authorization   "bearer 8ba0c250-4fb4-47eb-90e3-c2f9fdd3f016"
     * @param principal
     * @return
     */
    @GetMapping("/user/info")
    public Principal userInfo(Principal principal) {
        // authentication 就是获取的Principal
        // Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return principal;
    }
}
