package com.naga.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "authorization-server")
public interface OAuth2FeignClient {

    /**
     * 这个接口的名字是SpringOAuth2自带的
     * @param grantType
     * @param username
     * @param password
     * @param loginType
     * @param basicToken
     * @return
     */
    @PostMapping("/oauth/token")
    ResponseEntity<JwtToken> getToken(@RequestParam("grant_type") String grantType,
                                      @RequestParam("username") String username,
                                      @RequestParam("password") String password,
                                      @RequestParam("login_type") String loginType,
                                      @RequestHeader("Authorization") String basicToken
                                      // Basic Auth  中加入clientId和clientSecret, 加密后实际上是在header中加入Key为 Authorization value为 Basic Y29pbi1hcGk6Y29pbi1zZWNyZXQ=
                                      );
}
