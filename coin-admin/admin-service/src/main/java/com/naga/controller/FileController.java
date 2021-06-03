package com.naga.controller;


import cn.hutool.core.date.DateUtil;
import com.aliyun.oss.OSS;
import com.naga.model.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@Api(tags = "控制器")
@Slf4j
public class FileController {

    @Autowired
    private OSS ossClient;

    @Value("${oss.bucket.name:katouyi-coin-exchange-imgs}")
    private String bucketName;

    @Value("${spring.cloud.alicloud.oss.endpoint}")
    private String endPointAddress;

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
}
