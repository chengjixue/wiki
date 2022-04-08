package edu.xuecj.wiki.service;

import edu.xuecj.wiki.domain.Ebook;
import edu.xuecj.wiki.mapper.EbookMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author xuecj
 * @version 1.0
 * @date 2022/4/8 11:44
 */
@Service
public class EbookService {
//    @Resource是jdk自带的注解 也可以用 @Autowired
    @Resource
    private EbookMapper ebookMapper;
    public List<Ebook> list(){
        return ebookMapper.selectByExample(null);
    }

}
