package edu.xuecj.wiki.controller;

import edu.xuecj.wiki.domain.Test;
import edu.xuecj.wiki.service.TestService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author xuecj
 * @version 1.0
 * @date 2022/4/7 22:09
 */
@RestController
//RestController是用于返回字符串

public class TestController {
    //    在 : 后面加入值在没有配置情况下使用
    @Value("${test.hello:TEST}")
    private String testHello;
    @Resource
    private TestService testService;
    @Resource
    private RedisTemplate redisTemplate;

    @RequestMapping("/hello")
    public String hello() {
        return "hello" + testHello;
    }

    @GetMapping("/test/list")
    public List<Test> list() {
        return testService.list();
    }

    @RequestMapping("/redis/get/{key}")
    public Object get(@PathVariable Long key) {
        Object obj = redisTemplate.opsForValue().get(key);
        System.out.println(key+ ":======================================" + obj);
        return obj;
    }

}
