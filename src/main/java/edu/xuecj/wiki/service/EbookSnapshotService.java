package edu.xuecj.wiki.service;

import edu.xuecj.wiki.mapper.EbookSnapshotMapperCust;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
