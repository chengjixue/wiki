package edu.xuecj.wiki.service;

import edu.xuecj.wiki.domain.Ebook;
import edu.xuecj.wiki.domain.EbookExample;
import edu.xuecj.wiki.mapper.EbookMapper;
import edu.xuecj.wiki.req.EbookReq;
import edu.xuecj.wiki.resp.EbookResp;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
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
    public List<EbookResp> list(EbookReq  req){
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        /*
        * criteria.andNameLike("%"+name+"%");
        * 相当于like的效果，用来模糊查询
        * */
        criteria.andNameLike("%"+req.getName()+"%");
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);
        List<EbookResp> respList=new ArrayList<>();
        for (Ebook ebook:ebookList) {
            EbookResp ebookResp = new EbookResp();
            ebookResp.setId(ebook.getId());
            BeanUtils.copyProperties(ebook,ebookResp);
            respList.add(ebookResp);
        }
        return respList;
    }

}
