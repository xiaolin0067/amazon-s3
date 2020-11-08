package com.example.config;

import lombok.Data;

/**
 * @author zlin
 * @date 20201107
 */
@Data
public class UploadFile {

    private String fileName;

    private String filePath;

    public UploadFile(String fileName, String filePath) {
        this.fileName = fileName;
        this.filePath = filePath;
    }
}
