package edu.xuecj.wiki.controller;

import edu.xuecj.wiki.domain.Ebook;
import edu.xuecj.wiki.service.EbookService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

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
    public List<Ebook> list(){
        return ebookService.list();
    }

}
