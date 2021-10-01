package com.dong.crowd.handler;

import com.dong.crowd.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
public class redisHandler {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @RequestMapping("/set/redis/key/value/remote")
    ResultEntity<String> setRedisKeyValueRemote(
            @RequestParam("key") String key,
            @RequestParam("value") String value
    ){
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        try {
            operations.set(key,value);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail(e.getMessage());
        }
    };

    @RequestMapping("/set/redis/key/value/remote/with/timeout")
    ResultEntity<String> setRedisKeyValueRemoteWithTimeout(
            @RequestParam("key") String key,
            @RequestParam("value") String value,
            @RequestParam("time") Long time,
            @RequestParam("timeUnit") TimeUnit timeUnit
    ){
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        try {
            operations.set(key,value, time, timeUnit);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail(e.getMessage());
        }
    };

    @RequestMapping("/get/redis/string/value/by/key")
    ResultEntity<String> getRedisStringValueByKey(
            @RequestParam("key") String key
    ){
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        try {
            String s = operations.get(key);
            return ResultEntity.successWithData(s);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail(e.getMessage());
        }
    };

    @RequestMapping("/remove/redis/key/remote")
    ResultEntity<String> removeRedisKeyRemote(
            @RequestParam("key") String key
    ){
        try {
            stringRedisTemplate.delete(key);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail(e.getMessage());
        }
    };

}
