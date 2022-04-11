package edu.xuecj.wiki.controller;

import edu.xuecj.wiki.req.EbookReq;
import edu.xuecj.wiki.resp.CommonResp;
import edu.xuecj.wiki.resp.EbookResp;
import edu.xuecj.wiki.resp.PageResp;
import edu.xuecj.wiki.service.EbookService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public CommonResp list(EbookReq req){
        CommonResp<PageResp<EbookResp>> resp = new CommonResp<>();
        PageResp<EbookResp> list=ebookService.list(req);
        resp.setContent(list);
        return resp;
    }

}
