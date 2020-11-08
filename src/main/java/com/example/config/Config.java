package com.example.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author zlin
 * @date 20201107
 */
@Data
@Configuration
public class Config {

    @Value("${s3.accessKey}")
    private String accessKey;

    @Value("${s3.secretKey}")
    private String secretKey;

    @Value("${s3.bucketName}")
    private String bucketName;

    @Value("${s3.region}")
    private String region;

    @Value("${s3.uploadPath}")
    private String uploadPath;
}
