package com.naga.config;

import com.alibaba.fastjson.JSON;
import com.naga.vo.IdentityVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@Slf4j
@Configuration
@EnableConfigurationProperties(IdProperties.class)
public class IdAutoConfiguration {

    public static IdProperties idProperties;

    public static RestTemplate restTemplate = new RestTemplate();

    public IdAutoConfiguration(IdProperties idProperties) {
        IdAutoConfiguration.idProperties = idProperties;
    }

    /**
     * curl -i -k -X POST 'https://dfidveri.market.alicloudapi.com/verify_id_name'
     * -H 'Authorization:APPCODE 你自己的AppCode' --data 'id_number=445122********33&name=%E9%BB%84%E5%A4%A7%E5%A4%A7'
     */
    public static boolean checkId(String idNumber, String name) {
        String url = idProperties.getUrl();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "APPCODE " + idProperties.getAppCode());
        HashMap<String, String> params = new HashMap<>();
        params.put("id_number", idNumber);
        params.put("name", name);
        ResponseEntity<IdentityVO> responseEntity = restTemplate.postForEntity(url, headers, IdentityVO.class, params);
        HttpStatus statusCode = responseEntity.getStatusCode();
        if (statusCode.equals(HttpStatus.BAD_REQUEST)) {
            log.error("核验测失败");
            return false;
        } else if (statusCode.equals(HttpStatus.FORBIDDEN)) {
            log.error("调用频率超出限额");
            return false;
        } else if (statusCode.equals(HttpStatus.INTERNAL_SERVER_ERROR)) {
            log.error("服务器内部错误");
            return false;
        }
        IdentityVO body = responseEntity.getBody();
        log.info(JSON.toJSONString(body));
        return true;
    }

    /**
     * OK
     * {
     *   "request_id": "TID8bf47ab6eda64476973cc5f5b6ebf57e",
     *   "status": "OK",
     *   "state": 1,
     * }
     *
     * Error
     * {
     *     "status": "QUERY_FAILED",
     *     "result_message": "参数为空或格式错误",
     *     "request_id": "TIDfe55898b0029464193665c3446f39e66"
     * }
     *
     * 400	DETECTION_FAILED	核验测失败
     * 403	RATE_LIMIT_EXCEEDED	调用频率超出限额
     * 500	INTERNAL_ERROR	服务器内部错误
     */
}
