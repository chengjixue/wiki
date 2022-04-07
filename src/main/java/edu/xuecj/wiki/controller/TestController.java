package edu.xuecj.wiki.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xuecj
 * @version 1.0
 * @date 2022/4/7 22:09
 */
@RestController
//RestController是用于返回字符串

public class TestController {
    @RequestMapping("/hello")
    public String hello(){
        return "hello";
    }
}
