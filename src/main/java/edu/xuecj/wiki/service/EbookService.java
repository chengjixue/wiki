package edu.xuecj.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import edu.xuecj.wiki.domain.Ebook;
import edu.xuecj.wiki.domain.EbookExample;
import edu.xuecj.wiki.mapper.EbookMapper;
import edu.xuecj.wiki.req.EbookQueryReq;
import edu.xuecj.wiki.req.EbookSaveReq;
import edu.xuecj.wiki.resp.EbookQueryResp;
import edu.xuecj.wiki.resp.PageResp;
import edu.xuecj.wiki.utils.CopyUtil;
import edu.xuecj.wiki.utils.SnowFlake;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private SnowFlake snowFlake;

    public PageResp<EbookQueryResp> list(EbookQueryReq req) {
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        /*
         * criteria.andNameLike("%"+name+"%");
         * 相当于like的效果，用来模糊查询
         * */
        if (!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%" + req.getName() + "%");
        }
        PageHelper.startPage(req.getPage(), req.getSize());
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);

        PageInfo<Ebook> pageInfo = new PageInfo<>(ebookList);
        System.out.println("行数---------" + pageInfo.getTotal() + "页数------------" +
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
        PageResp<EbookQueryResp> pageResp = new PageResp<>();
//        列表复制
        List<EbookQueryResp> list = CopyUtil.copyList(ebookList, EbookQueryResp.class);
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    }

    public void save(EbookSaveReq req) {
        Ebook ebook = CopyUtil.copy(req, Ebook.class);
        if (ObjectUtils.isEmpty(req.getId())) {
//            新增
            ebook.setId(snowFlake.nextId());
            ebookMapper.insert(ebook);
        } else {
//            更新
            ebookMapper.updateByPrimaryKey(ebook);
        }
    }
    public void delete(Long id){
        ebookMapper.deleteByPrimaryKey(id);
    }


}
