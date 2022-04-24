package edu.xuecj.wiki.controller;

import edu.xuecj.wiki.req.UserLoginReq;
import edu.xuecj.wiki.req.UserQueryReq;
import edu.xuecj.wiki.req.UserResetPasswordReq;
import edu.xuecj.wiki.req.UserSaveReq;
import edu.xuecj.wiki.resp.CommonResp;
import edu.xuecj.wiki.resp.PageResp;
import edu.xuecj.wiki.resp.UserLoginResp;
import edu.xuecj.wiki.resp.UserQueryResp;
import edu.xuecj.wiki.service.UserService;
import edu.xuecj.wiki.utils.SnowFlake;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.concurrent.TimeUnit;

/**
 * @author xuecj
 * @version 1.0
 * @date 2022/4/7 22:09
 */
//RestController是用于返回字符串
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private SnowFlake snowFlake;

    @GetMapping("/list")
    public CommonResp list(@Valid UserQueryReq req) {
        CommonResp<PageResp<UserQueryResp>> resp = new CommonResp<>();
        PageResp<UserQueryResp> list = userService.list(req);
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody UserSaveReq req) {
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        CommonResp resp = new CommonResp<>();
        userService.save(req);
        return resp;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable("id") Long id) {
        CommonResp resp = new CommonResp<>();
        userService.delete(id);
        return resp;
    }

    @PostMapping("/reset-password")
    public CommonResp resetPassword(@Valid @RequestBody UserResetPasswordReq req) {
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        CommonResp resp = new CommonResp<>();
        userService.resetPassword(req);
        return resp;
    }

    @PostMapping("/login")
    public CommonResp login(@Valid @RequestBody UserLoginReq req) {
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        CommonResp<UserLoginResp> resp = new CommonResp<>();
        UserLoginResp userLoginResp = userService.login(req);
//        生成单点登录token，并放入redis
        Long token = snowFlake.nextId();
        System.out.println(token);
        userLoginResp.setToken(token.toString());
        redisTemplate.opsForValue().set(token.toString(), userLoginResp, 60 * 60 * 24, TimeUnit.SECONDS);
        resp.setContent(userLoginResp);
        return resp;
    }

    @GetMapping("/logout/{token}")
    public CommonResp logout(@PathVariable String token) {
        CommonResp resp = new CommonResp<>();
        redisTemplate.delete(token);
        System.out.println("从redis删除token"+token);
        return resp;
    }

}
