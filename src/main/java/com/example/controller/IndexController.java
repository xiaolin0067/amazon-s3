package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author zlin
 * @date 20201107
 */
@Controller
public class IndexController {

    @RequestMapping("/index")
    public String index(){
        // 访问前端页面
        return "upload.html";
    }
}
