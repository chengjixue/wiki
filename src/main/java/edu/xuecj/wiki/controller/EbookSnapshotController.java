package edu.xuecj.wiki.controller;

import edu.xuecj.wiki.resp.CommonResp;
import edu.xuecj.wiki.resp.StatisticResp;
import edu.xuecj.wiki.service.EbookSnapshotService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author xuecj
 * @version 1.0
 * @date 2022/4/25 20:52
 */
@RestController
@RequestMapping("/ebook-snapshot")
public class EbookSnapshotController {
    @Resource
    private EbookSnapshotService ebookSnapshotService;

    @GetMapping("/get-statistic")
    public CommonResp getStatistic() {
        List<StatisticResp> statisticResp=  ebookSnapshotService.getStatistic();
        CommonResp<List<StatisticResp>> commonResp=new CommonResp<>();
        commonResp.setContent(statisticResp);
        return commonResp;
    }
    @GetMapping("/get-30-statistic")
    public CommonResp get30Statistic() {
        List<StatisticResp> statisticResp=  ebookSnapshotService.get30Statistic();
        CommonResp<List<StatisticResp>> commonResp=new CommonResp<>();
        commonResp.setContent(statisticResp);
        return commonResp;
    }

}
