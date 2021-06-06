package com.naga.feign;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class JwtToken {

    @JsonProperty(value = "access_token")
    private String accessToken;

    /** token的类型 */
    @JsonProperty(value = "token_type")
    private String tokenType;

    @JsonProperty(value = "refresh_token")
    private String refreshToken;

    /** 过期时间 */
    @JsonProperty(value = "expires_in")
    private Long expires_in;

    /** token的范围 */
    private String scope;

    /** token的颁发凭证 */
    private String jti;
}

