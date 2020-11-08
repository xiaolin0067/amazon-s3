package com.example.model;

import lombok.Data;

/**
 * @author zlin
 * @date 20201108
 */
@Data
public class Result {
    private int code;
    private String message;

    public Result(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
