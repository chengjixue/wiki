package edu.xuecj.wiki.service;

import edu.xuecj.wiki.domain.Demo;
import edu.xuecj.wiki.mapper.DemoMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author xuecj
 * @version 1.0
 * @date 2022/4/8 11:44
 */
@Service
public class DemoService {
//    @Resource是jdk自带的注解 也可以用 @Autowired
    @Resource
    private DemoMapper demoMapper;
    public List<Demo> list(){
        return demoMapper.selectByExample(null);
    }

}
