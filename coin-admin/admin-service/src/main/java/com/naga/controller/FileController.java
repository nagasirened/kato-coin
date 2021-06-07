package com.naga.controller;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSS;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.naga.model.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@Api(tags = "控制器")
@Slf4j
public class FileController {

    @Autowired
    @SuppressWarnings("all")
    private OSS ossClient;

    @Value("${spring.cloud.alicloud.access-key}")
    private String accessId;

    @Value("${oss.bucket.name:katouyi-coin-exchange-imgs}")
    private String bucketName;

    @Value("${spring.cloud.alicloud.oss.endpoint}")
    private String endPointAddress;

    @Value("${spring.cloud.alicloud.oss.callbackUrl:http://katouyi.com/upload/result}")
    private String callbackUrl;

    @ApiOperation(value = "文件上传")
    @PostMapping("/image/AliyunImgUpload")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "要上传的文件")
    })
    public R<String> fileUpload(@RequestParam("file")MultipartFile file) throws IOException {
        /**
         * 1. bucketName: 桶的名称
         * 2. 文件名称
         * 3. 文件的输入流
         */
        String fileName = DateUtil.today().replaceAll("-", "/") + "/" + file.getOriginalFilename();
        ossClient.putObject(bucketName, fileName, file.getInputStream());
        String clientAddress = "https://" + bucketName + "." + endPointAddress + "/" + fileName;
        log.info("FileController#fileUpload，上传图片能访问的地址为：{}", clientAddress);
        return R.ok(clientAddress);
    }

    /**
     * 阿里云外部直接传
     * @return Map<String, String>
     */
    @GetMapping("/image/pre/upload")
    @ApiOperation(value = "预上传，获取一个上传的票据提供给前端直接传输，OSS要设置跨域")
    public R<Map<String, String>> asyncUpload() {
        String dir = DateUtil.today().replaceAll("-", "/");
        Map<String, String> uploadPolicy = getUploadPolicy(30L, 3 * 1024 * 1024L, dir);
        return R.ok(uploadPolicy);
    }

    /**
     *
     * @param expireTime    超时时间
     * @param maxFileSize   文件最大Size
     * @param dir           文件夹路径
     * @return
     */
    private Map<String, String> getUploadPolicy(Long expireTime, Long maxFileSize, String dir) {
        long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
        Date expiration = new Date(expireEndTime);
        PolicyConditions policyConds = new PolicyConditions();
        // 设置文件大小限制，3M
        policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, maxFileSize);
        // 设置文件路径
        policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);
        try {
            // 生产一个票据
            String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes("utf-8");
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = ossClient.calculatePostSignature(postPolicy);

            Map<String, String> respMap = new LinkedHashMap<>();
            respMap.put("accessid", accessId);
            respMap.put("policy", encodedPolicy);
            respMap.put("signature", postSignature);
            respMap.put("dir", dir);
            respMap.put("host", "https://" + bucketName + "." + endPointAddress);
            respMap.put("expire", String.valueOf(expireEndTime / 1000));
            // respMap.put("expire", formatISO8601Date(expiration));

            JSONObject jasonCallback = new JSONObject();
            // 回调地址POST，需要是一个公网地址，我先编一个，
            jasonCallback.put("callbackUrl", callbackUrl);
            jasonCallback.put("callbackBody",
                    "filename=${object}&size=${size}&mimeType=${mimeType}&height=${imageInfo.height}&width=${imageInfo.width}");
            jasonCallback.put("callbackBodyType", "application/x-www-form-urlencoded");
            String base64CallbackBody = BinaryUtil.toBase64String(jasonCallback.toString().getBytes());
            respMap.put("callback", base64CallbackBody);
            return respMap;
        } catch (Exception e) {
            log.error("preUpload, message: {}" + e.getMessage());
        } finally {
            ossClient.shutdown();
        }
        return CollectionUtil.newHashMap();
    }
}
