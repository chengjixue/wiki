package edu.xuecj.wiki.job;

import edu.xuecj.wiki.service.DocService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOG= LoggerFactory.getLogger(DocJob.class);

    /*
    * 每30秒更新一次
    * */
    @Autowired
    DocService docService;
    @Scheduled(cron = "5/30 * * * * ?")
    public void cron(){
        docService.updateEbookInfo();
    }
}
