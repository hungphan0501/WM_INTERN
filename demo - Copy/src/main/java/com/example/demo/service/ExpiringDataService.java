package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ExpiringDataService {

    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    public ExpiringDataService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * Ở tham số thứ tư trong hàm sẽ là thời gian mà data được lưu trữ trong redis
     * TimeUnit là đối tượng để mình cài đặt theo chuẩn thời gian mong muốn (seconds, minutes, hours,day,..)
     * **/
    public void saveDataWithExpiration(String key, String value, long seconds) {
        redisTemplate.opsForValue().set(key, value, seconds, TimeUnit.SECONDS);
    }

    public void saveListDataWithExpiration(String key, List<String> value, long seconds) {
        redisTemplate.opsForList().leftPushAll(key, value);
        redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
    }


    public List<String> getData(String key) {
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    /**
     * - Strings:
     *
     * Ghi dữ liệu: SET key value
     * Đọc dữ liệu: GET key
     *
     * - Hashes:
     *
     * Ghi dữ liệu: HSET hash_key field value
     * Đọc dữ liệu: HGET hash_key field
     *
     * - Lists:
     *
     * Ghi dữ liệu: LPUSH list_key value (Thêm vào đầu), RPUSH list_key value (Thêm vào cuối)
     * Đọc dữ liệu: LRANGE list_key start stop
     *
     * -Sets:
     *
     * Ghi dữ liệu: SADD set_key member
     * Đọc dữ liệu: SMEMBERS set_key
     *
     * - Sorted Sets:
     *
     * Ghi dữ liệu: ZADD sorted_set_key score member
     * Đọc dữ liệu: ZRANGE sorted_set_key start stop
     **/

}
