package edu.xuecj.wiki.mapper;

import edu.xuecj.wiki.resp.StatisticResp;

import java.util.List;

/**
 * @author xuecj
 * @version 1.0
 * @date 2022/4/25 20:33
 */
public interface EbookSnapshotMapperCust {
    public void genSnapshot();
    public List<StatisticResp> getStatistic();
    public List<StatisticResp> get30Statistic();
}
