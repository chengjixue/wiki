package edu.xuecj.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import edu.xuecj.wiki.domain.User;
import edu.xuecj.wiki.domain.UserExample;
import edu.xuecj.wiki.exception.BusinessException;
import edu.xuecj.wiki.exception.BusinessExceptionCode;
import edu.xuecj.wiki.mapper.UserMapper;
import edu.xuecj.wiki.req.UserQueryReq;
import edu.xuecj.wiki.req.UserResetPasswordReq;
import edu.xuecj.wiki.req.UserSaveReq;
import edu.xuecj.wiki.resp.PageResp;
import edu.xuecj.wiki.resp.UserQueryResp;
import edu.xuecj.wiki.utils.CopyUtil;
import edu.xuecj.wiki.utils.SnowFlake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author xuecj
 * @version 1.0
 * @date 2022/4/8 11:44
 */
@Service
public class UserService {
    //    @Resource是jdk自带的注解 也可以用 @Autowired
    @Resource
    private UserMapper userMapper;
    @Autowired
    private SnowFlake snowFlake;

    public PageResp<UserQueryResp> list(UserQueryReq req) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getLoginName())) {
            criteria.andLoginNameEqualTo(req.getLoginName());
        }
        PageHelper.startPage(req.getPage(), req.getSize());
        List<User> userList = userMapper.selectByExample(userExample);

        PageInfo<User> pageInfo = new PageInfo<>(userList);
        System.out.println("行数---------" + pageInfo.getTotal() + "页数------------" +
                pageInfo.getPages()
        );

//        List<UserResp> respList=new ArrayList<>();
//        for (User user:userList) {

//        spring自带的对象复制
//        UserResp userResp = new UserResp();
//        BeanUtils.copyProperties(user,userResp);

//        utils工具类对象复制
//            UserResp userResp = CopyUtil.copy(user, UserResp.class);
//            respList.add(userResp);
//        }
        PageResp<UserQueryResp> pageResp = new PageResp<>();
//        列表复制
        List<UserQueryResp> list = CopyUtil.copyList(userList, UserQueryResp.class);
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    }

    public void save(UserSaveReq req) {
        User user = CopyUtil.copy(req, User.class);
        if (ObjectUtils.isEmpty(req.getId())) {
            User userDB = selectByLoginName(req.getLoginName());
            if (ObjectUtils.isEmpty(userDB)) {
//            新增
                user.setId(snowFlake.nextId());
                userMapper.insert(user);
            } else {
//                用户名已经存在
                throw new BusinessException(BusinessExceptionCode.USER_LOGIN_NAME_EXIST);
            }
        } else {
//            更新
            user.setLoginName(null);
            user.setPassword(null);
            userMapper.updateByPrimaryKeySelective(user);
        }
    }

    public void delete(Long id) {
        userMapper.deleteByPrimaryKey(id);
    }

    public User selectByLoginName(String LoginName) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andLoginNameEqualTo(LoginName);
        List<User> userList = userMapper.selectByExample(userExample);
        if (CollectionUtils.isEmpty(userList)) {
            return null;
        } else {
            return userList.get(0);
        }
    }
    /*
    * 修改密码
    * */
    public void resetPassword(UserResetPasswordReq req) {
        User user = CopyUtil.copy(req, User.class);
        userMapper.updateByPrimaryKeySelective(user);
    }

}
