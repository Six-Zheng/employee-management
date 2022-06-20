package pers.zhengxiaojie.application.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author ZhengXiaojie
 * @date 2021/11/19
 */
@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

//    @Method 设置键名、键值以及其过期时间
//    @Parameter
//    key 键名
//    value 键值
//    timeUnit 时间单位
//    timeUnitValue 时间单位值
    public void setKeyAndValue(String key, Object value, TimeUnit timeUnit, int timeUnitValue) {

        redisTemplate.opsForValue().set(key, value, timeUnitValue, timeUnit);
    }

//    @Method 获取键值
//    @Parameter
//    key 键名
    public Object getValue(String key) {

        Object value = redisTemplate.opsForValue().get(key);

        return value;
    }

//    @Method 删除键值
//    @Parameter
//    key 键名
    public void deleteKey(String key) {

        redisTemplate.delete(key);
    }
}
