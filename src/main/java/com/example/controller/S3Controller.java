package com.example.controller;

import com.example.config.Config;
import com.example.config.UploadFile;
import com.example.model.Result;
import com.example.util.S3Client;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.UUID;

/**
 * @author zlin
 * @date 20201107
 */
@RestController
@RequestMapping("/s3")
public class S3Controller {

    @Autowired
    private Config config;

    private UploadFile curUploadFile;

    private Gson gson = new Gson();

    @PostMapping("/upload")
    public Object upload(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String distUploadPath = config.getUploadPath() + UUID.randomUUID().toString() + "_" + fileName;
        curUploadFile = new UploadFile(fileName, distUploadPath);
        S3Client client = new S3Client(config);
        boolean result =  client.upload(distUploadPath, file);
        return result ? gson.toJson(new Result(200, fileName)) : "上传失败";
    }

    @GetMapping("/download")
    public Object download(HttpServletResponse response) {
        if (curUploadFile == null) {
            return gson.toJson(new Result(202, "当前还未上传文件"));
        }
        OutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            S3Client client = new S3Client(config);
            inputStream = client.getDownloadStream(curUploadFile.getFilePath());
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=" + curUploadFile.getFileName());
            outputStream = response.getOutputStream();
            byte[] bytes = new byte[1024];
            int n;
            while ((n = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, n);
            }
            outputStream.flush();
            inputStream.close();
            outputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
