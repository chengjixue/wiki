package edu.xuecj.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import edu.xuecj.wiki.domain.Doc;
import edu.xuecj.wiki.domain.DocExample;
import edu.xuecj.wiki.mapper.DocMapper;
import edu.xuecj.wiki.req.DocQueryReq;
import edu.xuecj.wiki.req.DocSaveReq;
import edu.xuecj.wiki.resp.DocQueryResp;
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
public class DocService {
    //    @Resource是jdk自带的注解 也可以用 @Autowired
    @Resource
    private DocMapper docMapper;
    @Autowired
    private SnowFlake snowFlake;

    public PageResp<DocQueryResp> list(DocQueryReq req) {
        DocExample docExample = new DocExample();
        docExample.setOrderByClause("sort asc");
        DocExample.Criteria criteria = docExample.createCriteria();

        /*
         * criteria.andNameLike("%"+name+"%");
         * 相当于like的效果，用来模糊查询
         * */

        PageHelper.startPage(req.getPage(), req.getSize());
        List<Doc> docList = docMapper.selectByExample(docExample);

        PageInfo<Doc> pageInfo = new PageInfo<>(docList);
        System.out.println("行数---------" + pageInfo.getTotal() + "页数------------" +
                pageInfo.getPages()
        );

//        List<DocResp> respList=new ArrayList<>();
//        for (Doc doc:docList) {

//        spring自带的对象复制
//        DocResp docResp = new DocResp();
//        BeanUtils.copyProperties(doc,docResp);

//        utils工具类对象复制
//            DocResp docResp = CopyUtil.copy(doc, DocResp.class);
//            respList.add(docResp);
//        }
        PageResp<DocQueryResp> pageResp = new PageResp<>();
//        列表复制
        List<DocQueryResp> list = CopyUtil.copyList(docList, DocQueryResp.class);
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    }

    public List<DocQueryResp> all() {
        DocExample docExample = new DocExample();
//        根据sort排序
        docExample.setOrderByClause("sort asc");
        List<Doc> docList = docMapper.selectByExample(docExample);
//        列表复制
        List<DocQueryResp> list = CopyUtil.copyList(docList, DocQueryResp.class);
        return list;
    }

    public void save(DocSaveReq req) {
        Doc doc = CopyUtil.copy(req, Doc.class);
        if (ObjectUtils.isEmpty(req.getId())) {
//            新增
            doc.setId(snowFlake.nextId());
            docMapper.insert(doc);
        } else {
//            更新
            docMapper.updateByPrimaryKey(doc);
        }
    }

    public void delete(Long id) {
        docMapper.deleteByPrimaryKey(id);
    }


}
