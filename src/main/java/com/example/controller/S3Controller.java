package com.example.controller;

import com.example.config.Config;
import com.example.config.UploadFile;
import com.example.model.Result;
import com.example.util.S3Client;
import com.google.gson.Gson;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

/**
 * @author zlin
 * @date 20201107
 */
@RestController
@RequestMapping("/s3")
public class S3Controller {

    @Resource
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
        try (OutputStream outputStream = response.getOutputStream()) {
            S3Client client = new S3Client(config);
            try (InputStream inputStream = client.getDownloadStream(curUploadFile.getFilePath())) {
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/octet-stream");
                response.addHeader("Content-Disposition", "attachment; filename=" + curUploadFile.getFileName());
                byte[] bytes = new byte[1024 * 10];
                int n;
                while ((n = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, n);
                }
                outputStream.flush();
            }catch (Exception e){
                e.printStackTrace();
                return gson.toJson(new Result(203, "文件下载失败"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return gson.toJson(new Result(203, "文件下载失败"));
        }
        return null;
    }

}
