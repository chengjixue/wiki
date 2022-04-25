package edu.xuecj.wiki.job;

import edu.xuecj.wiki.service.DocService;
import edu.xuecj.wiki.utils.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author xuecj
 * @version 1.0
 * @date 2022/4/25 15:28
 */
@Component
public class DocJob {
    private static final Logger LOG = LoggerFactory.getLogger(DocJob.class);

    /*
     * 每30秒更新一次
     * */
    @Autowired
    private DocService docService;
    @Autowired
    private SnowFlake snowFlake;

    @Scheduled(cron = "5/30 * * * * ?")
    public void cron() {
        MDC.put("LOG_ID", String.valueOf(snowFlake.nextId()));
        LOG.info("开始更新电子书下的文档数据");
        long start = System.currentTimeMillis();
        docService.updateEbookInfo();
        long end = System.currentTimeMillis();
        LOG.info("更新电子书下的文档数据完成,耗时{}", end - start);
    }
}
