package com.naga.vo;


import lombok.Data;
import org.codehaus.jackson.annotate.JsonProperty;

@Data
public class IdentityVO {

    @JsonProperty(value = "request_id")
    private String requestId;

    private String status;

    private Integer state;

    @JsonProperty(value = "result_message")
    private String resultMessage;
}
