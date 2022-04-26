package edu.xuecj.wiki.service;

import edu.xuecj.wiki.mapper.EbookSnapshotMapperCust;
import edu.xuecj.wiki.resp.StatisticResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xuecj
 * @version 1.0
 * @date 2022/4/25 20:31
 */
@Service
public class EbookSnapshotService {
    @Autowired
    private EbookSnapshotMapperCust ebookSnapshotMapperCust;

    public void genSnapshot() {
        ebookSnapshotMapperCust.genSnapshot();
    }

    /*
     * 获取首页数值数据：总阅读数，今日阅读数
     * */
    public List<StatisticResp> getStatistic() {
        return ebookSnapshotMapperCust.getStatistic();
    }
    public List<StatisticResp> get30Statistic() {
        return ebookSnapshotMapperCust.get30Statistic();
    }
}
