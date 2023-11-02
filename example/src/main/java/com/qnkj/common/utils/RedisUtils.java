package com.qnkj.common.utils;

import com.qnkj.common.services.impl.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * create by 徐雁
 * create date 2021/7/24
 */

@Component
public class RedisUtils {
    @Autowired
    public RedisService redisService;

    private static RedisUtils utils;

    @PostConstruct
    public void init() {
        utils = this;
        utils.redisService = this.redisService;
    }

    public static Boolean expire(String key, Long time) {
        return utils.redisService.expire(key,time);
    }

    public static Long getExpire(String key) {
        return utils.redisService.getExpire(key);
    }

    public static Boolean hasKey(String key) {
        return utils.redisService.hasKey(key);
    }

    public static void del(String... key) {
        utils.redisService.del(key);
    }

    public static Object get(String key) {
        return utils.redisService.get(key);
    }

    public static Boolean set(String key, Object value) {
        return utils.redisService.set(key, value);
    }

    public static Boolean set(String key, Object value, Long time) {
        return utils.redisService.set(key, value, time);
    }

    public static Long incr(String key, Long delta) {
        return utils.redisService.incr(key, delta);
    }

    public static Long decr(String key, Long delta) {
        return utils.redisService.decr(key, delta);
    }

}
