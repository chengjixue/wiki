package edu.xuecj.wiki.service;

import edu.xuecj.wiki.domain.Test;
import edu.xuecj.wiki.mapper.TestMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author xuecj
 * @version 1.0
 * @date 2022/4/8 11:44
 */
@Service
public class TestService {
//    @Resource是jdk自带的注解 也可以用 @Autowired
    @Resource
    private TestMapper testMapper;
    public List<Test> list(){
        return testMapper.list();
    }

}
