package edu.xuecj.wiki.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author xuecj
 * @version 1.0
 * @date 2022/4/25 15:28
 */
@Component
public class TestJob {
    private static final Logger LOG= LoggerFactory.getLogger(TestJob.class);

    /*
    * 固定时间间隔，fixedRate单位毫秒
    * */
    @Scheduled(fixedRate = 1000)
    public void simple(){
        SimpleDateFormat formatter=new SimpleDateFormat("mm:ss");
        String dateString=formatter.format(new Date());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LOG.info("每隔5秒执行一次 {}",dateString);
    }

    /*
    * 自定义cron表达式执行
    * 只有等上一次的执行难完成才会执行下一次，错过就错过
    * */
    @Scheduled(cron = "0/2 * * * * ?")
    public void cron(){
        SimpleDateFormat formatter=new SimpleDateFormat("mm:ss SSS");
        String dateString=formatter.format(new Date());
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LOG.info("每隔2秒执行一次 {}",dateString);
    }
}
