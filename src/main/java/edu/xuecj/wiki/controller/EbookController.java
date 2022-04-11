package edu.xuecj.wiki.controller;

import edu.xuecj.wiki.req.EbookQueryReq;
import edu.xuecj.wiki.req.EbookSaveReq;
import edu.xuecj.wiki.resp.CommonResp;
import edu.xuecj.wiki.resp.EbookQueryResp;
import edu.xuecj.wiki.resp.PageResp;
import edu.xuecj.wiki.service.EbookService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author xuecj
 * @version 1.0
 * @date 2022/4/7 22:09
 */
//RestController是用于返回字符串
@RestController
@RequestMapping("/ebook")
public class EbookController {
    @Resource
    private EbookService ebookService;
    @GetMapping("/list")
    public CommonResp list(EbookQueryReq req){
        CommonResp<PageResp<EbookQueryResp>> resp = new CommonResp<>();
        PageResp<EbookQueryResp> list=ebookService.list(req);
        resp.setContent(list);
        return resp;
    }
    //    json格式的提交要加@RequestBody
    @PostMapping("/save")

    public CommonResp save(@RequestBody EbookSaveReq req){
        CommonResp resp = new CommonResp<>();
        ebookService.save(req);
        return resp;
    }

}
