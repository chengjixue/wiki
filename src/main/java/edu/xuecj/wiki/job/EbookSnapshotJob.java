package edu.xuecj.wiki.job;

import edu.xuecj.wiki.service.EbookSnapshotService;
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
 * @date 2022/4/25 20:27
 */
@Component
public class EbookSnapshotJob {
    private static final Logger LOG = LoggerFactory.getLogger(DocJob.class);

    /*
     * 每30秒更新一次
     * */
    @Autowired
    private EbookSnapshotService ebookSnapshotService;
    @Autowired
    private SnowFlake snowFlake;

    @Scheduled(cron = "5/30 * * * * ?")
    public void cron() {
        MDC.put("LOG_ID", String.valueOf(snowFlake.nextId()));
        LOG.info("生成今日电子书下快照开始");
        long start = System.currentTimeMillis();
        ebookSnapshotService.genSnapshot();
        long end = System.currentTimeMillis();
        LOG.info("生成今日电子书下快照结束,耗时{}", end - start);
    }
}
