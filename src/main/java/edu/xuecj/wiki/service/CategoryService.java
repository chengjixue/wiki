package edu.xuecj.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import edu.xuecj.wiki.domain.Category;
import edu.xuecj.wiki.domain.CategoryExample;
import edu.xuecj.wiki.mapper.CategoryMapper;
import edu.xuecj.wiki.req.CategoryQueryReq;
import edu.xuecj.wiki.req.CategorySaveReq;
import edu.xuecj.wiki.resp.CategoryQueryResp;
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
public class CategoryService {
    //    @Resource是jdk自带的注解 也可以用 @Autowired
    @Resource
    private CategoryMapper categoryMapper;
    @Autowired
    private SnowFlake snowFlake;

    public PageResp<CategoryQueryResp> list(CategoryQueryReq req) {
        CategoryExample categoryExample = new CategoryExample();
        categoryExample.setOrderByClause("sort asc");
        CategoryExample.Criteria criteria = categoryExample.createCriteria();

        /*
         * criteria.andNameLike("%"+name+"%");
         * 相当于like的效果，用来模糊查询
         * */

        PageHelper.startPage(req.getPage(), req.getSize());
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);

        PageInfo<Category> pageInfo = new PageInfo<>(categoryList);
        System.out.println("行数---------" + pageInfo.getTotal() + "页数------------" +
                pageInfo.getPages()
        );

//        List<CategoryResp> respList=new ArrayList<>();
//        for (Category category:categoryList) {

//        spring自带的对象复制
//        CategoryResp categoryResp = new CategoryResp();
//        BeanUtils.copyProperties(category,categoryResp);

//        utils工具类对象复制
//            CategoryResp categoryResp = CopyUtil.copy(category, CategoryResp.class);
//            respList.add(categoryResp);
//        }
        PageResp<CategoryQueryResp> pageResp = new PageResp<>();
//        列表复制
        List<CategoryQueryResp> list = CopyUtil.copyList(categoryList, CategoryQueryResp.class);
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    }

    public List<CategoryQueryResp> all() {
        CategoryExample categoryExample = new CategoryExample();
//        根据sort排序
        categoryExample.setOrderByClause("sort asc");
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);
//        列表复制
        List<CategoryQueryResp> list = CopyUtil.copyList(categoryList, CategoryQueryResp.class);
        return list;
    }

    public void save(CategorySaveReq req) {
        Category category = CopyUtil.copy(req, Category.class);
        if (ObjectUtils.isEmpty(req.getId())) {
//            新增
            category.setId(snowFlake.nextId());
            categoryMapper.insert(category);
        } else {
//            更新
            categoryMapper.updateByPrimaryKey(category);
        }
    }

    public void delete(Long id) {
        categoryMapper.deleteByPrimaryKey(id);
    }


}
