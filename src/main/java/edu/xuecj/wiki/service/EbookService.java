package edu.xuecj.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import edu.xuecj.wiki.domain.Ebook;
import edu.xuecj.wiki.domain.EbookExample;
import edu.xuecj.wiki.mapper.EbookMapper;
import edu.xuecj.wiki.req.EbookReq;
import edu.xuecj.wiki.resp.EbookResp;
import edu.xuecj.wiki.utils.CopyUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

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

    public List<EbookResp> list(EbookReq req) {
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        /*
         * criteria.andNameLike("%"+name+"%");
         * 相当于like的效果，用来模糊查询
         * */
        if(!ObjectUtils.isEmpty(req.getName())){
            criteria.andNameLike("%" + req.getName() + "%");
        }
        PageHelper.startPage(1,3);
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);

        PageInfo<Ebook> pageInfo=new PageInfo<>(ebookList);
        System.out.println( "行数---------"+pageInfo.getTotal()+"页数------------"+
        pageInfo.getPages()
        );

//        List<EbookResp> respList=new ArrayList<>();
//        for (Ebook ebook:ebookList) {

//        spring自带的对象复制
//        EbookResp ebookResp = new EbookResp();
//        BeanUtils.copyProperties(ebook,ebookResp);

//        utils工具类对象复制
//            EbookResp ebookResp = CopyUtil.copy(ebook, EbookResp.class);
//            respList.add(ebookResp);
//        }
//        列表复制
        List<EbookResp> list = CopyUtil.copyList(ebookList, EbookResp.class);
        return list;
    }

}
