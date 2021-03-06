package edu.xuecj.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import edu.xuecj.wiki.domain.Content;
import edu.xuecj.wiki.domain.Doc;
import edu.xuecj.wiki.domain.DocExample;
import edu.xuecj.wiki.exception.BusinessException;
import edu.xuecj.wiki.exception.BusinessExceptionCode;
import edu.xuecj.wiki.mapper.ContentMapper;
import edu.xuecj.wiki.mapper.DocMapper;
import edu.xuecj.wiki.mapper.DocMapperCust;
import edu.xuecj.wiki.req.DocQueryReq;
import edu.xuecj.wiki.req.DocSaveReq;
import edu.xuecj.wiki.resp.DocQueryResp;
import edu.xuecj.wiki.resp.PageResp;
import edu.xuecj.wiki.utils.CopyUtil;
import edu.xuecj.wiki.utils.RedisUtil;
import edu.xuecj.wiki.utils.RequestContext;
import edu.xuecj.wiki.utils.SnowFlake;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    @Resource
    private SnowFlake snowFlake;
    @Resource
    private ContentMapper contentMapper;
    @Resource
    private DocMapperCust docMapperCust;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private WsService wsService;

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

    public List<DocQueryResp> all(Long ebookId) {
        DocExample docExample = new DocExample();
        docExample.createCriteria().andEbookIdEqualTo(ebookId);
//        根据sort排序
        docExample.setOrderByClause("sort asc");
        List<Doc> docList = docMapper.selectByExample(docExample);
//        列表复制
        List<DocQueryResp> list = CopyUtil.copyList(docList, DocQueryResp.class);
        return list;
    }

    /*
     * 保存
     * */
    @Transactional
    public void save(DocSaveReq req) {
        Doc doc = CopyUtil.copy(req, Doc.class);
        Content content = CopyUtil.copy(req, Content.class);
        if (ObjectUtils.isEmpty(req.getId())) {
//            新增
            doc.setId(snowFlake.nextId());
            doc.setViewCount(0);
            doc.setVoteCount(0);
            docMapper.insert(doc);
            content.setId(doc.getId());
            contentMapper.insert(content);
        } else {
//            更新
            docMapper.updateByPrimaryKey(doc);
            int count = contentMapper.updateByPrimaryKeyWithBLOBs(content);
            if (count == 0) {
                contentMapper.insert(content);
            }
        }
    }

    public void delete(Long id) {
        docMapper.deleteByPrimaryKey(id);
    }

    public void delete(List<String> ids) {
        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();
        criteria.andIdIn(ids);
        docMapper.deleteByExample(docExample);
    }

    public String findContent(Long id) {
        Content content = contentMapper.selectByPrimaryKey(id);
//        文档书加一
        docMapperCust.increaseViewCount(id);
        if (ObjectUtils.isEmpty(content)) {
            return "";
        } else {
            return content.getContent();
        }
    }
    /*
    * 点赞
    * */
    public void vote(Long id) {
//        远程ip+doc。id作为key，24小时不能重赴
        String ip = RequestContext.getRemoteAddr();
        if (redisUtil.validateRepeat("doc_vote" + id + "_" + ip, 1)) {
            docMapperCust.increaseVoteCount(id);
        } else {
            throw new BusinessException(BusinessExceptionCode.VOTE_REPEAT);
        }
//        推送消息

        Doc docDB=docMapper.selectByPrimaryKey(id);
       String logId= MDC.get("LOG_ID");
        wsService.sendInfo("【"+docDB.getName()+"】被点赞了",logId);
    }


    public void updateEbookInfo(){
        docMapperCust.updateEbookInfo();
    }
}
