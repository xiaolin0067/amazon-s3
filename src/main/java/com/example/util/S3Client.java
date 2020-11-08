package com.example.util;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.example.config.Config;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * @author zlin
 * @date 20201107
 */
public class S3Client {

    private Config config;

    private AmazonS3 amazonS3;

    public S3Client(Config config){
        this.config = config;
        BasicAWSCredentials cred
                = new BasicAWSCredentials(config.getAccessKey(), config.getSecretKey());
        this.amazonS3 = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(cred))
                .withRegion(config.getRegion())
                .build();
    }

    public boolean upload(String name, MultipartFile file) {
        try {
            ObjectMetadata meta = new ObjectMetadata();
            meta.setContentLength(file.getSize());
            amazonS3.putObject(config.getBucketName(), name, file.getInputStream(), meta);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public InputStream getDownloadStream(String name) {
        S3Object obj = amazonS3.getObject(config.getBucketName(), name);
        return obj.getObjectContent();
    }
}
