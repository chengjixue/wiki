package edu.xuecj.wiki.controller;

import edu.xuecj.wiki.req.UserQueryReq;
import edu.xuecj.wiki.req.UserSaveReq;
import edu.xuecj.wiki.resp.CommonResp;
import edu.xuecj.wiki.resp.UserQueryResp;
import edu.xuecj.wiki.resp.PageResp;
import edu.xuecj.wiki.service.UserService;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

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
    @GetMapping("/list")
    public CommonResp list(@Valid UserQueryReq req){
        CommonResp<PageResp<UserQueryResp>> resp = new CommonResp<>();
        PageResp<UserQueryResp> list=userService.list(req);
        resp.setContent(list);
        return resp;
    }
    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody UserSaveReq req){
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        CommonResp resp = new CommonResp<>();
        userService.save(req);
        return resp;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable("id") Long id){
        CommonResp resp = new CommonResp<>();
        userService.delete(id);
        return resp;
    }



}
