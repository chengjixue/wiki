package edu.xuecj.wiki.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author xuecj
 * @version 1.0
 * @date 2022/4/25 11:12
 */
@Component
public class RedisUtil {
    private static final Logger LOG = LoggerFactory.getLogger(RedisUtil.class); // 日志
    @Autowired
    private RedisTemplate redisTemplate;

    /*
     * true:不存在，存放一个key
     * false：存在
     * @param key
     * @param second
     * @return
     * validateRepeat方法校验key是否存在
     * */
    public boolean validateRepeat(String key, int second) {
        if (redisTemplate.hasKey(key)) {
                LOG.info("key:{}已存在", key);
                return false;
            }else{
                LOG.info("key不存在，放入：{}，过期{}秒", key, second);
                redisTemplate.opsForValue().set(key, key, second, TimeUnit.SECONDS);
                return true;
            }
        }
    }


