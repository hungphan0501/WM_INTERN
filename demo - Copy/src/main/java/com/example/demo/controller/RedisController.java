package com.example.demo.controller;

import com.example.demo.service.ExpiringDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class RedisController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private ExpiringDataService expiringDataService;

    //Non Expiring Data
    @GetMapping("/none-expiring/save-data/{key}/{value}")
    public String saveDataNonExpiring(@PathVariable String key, @PathVariable String value) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.set(key, value);
        return "Non expiring Data Has been saved";
    }

    @GetMapping("/none-expiring/read-data/{key}")
    public String readDataNonExpiring(@PathVariable String key) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String value = ops.get(key);
        return "Value from Redis: " + value;
    }

    @GetMapping("/none-expiring/save-list")
    public String saveListDataNonExpiring(@RequestParam String key, @RequestParam List<String> values) {
        redisTemplate.opsForList().leftPushAll(key, values);
        return "Added numbers to the list with key " + key;
    }

    @GetMapping("/none-expiring/get-list")
    public List<String> getNonExpiringData(@RequestParam String key) {
        ListOperations<String, String> listOperations = redisTemplate.opsForList();
        return listOperations.range(key, 0, -1);
    }

    //Expiring Data

    @GetMapping("/expiring/save-data/{key}/{value}")
    public String saveExpiringData(@PathVariable String key, @PathVariable String value) {
        expiringDataService.saveDataWithExpiration(key, value, 60); // Expires in 60 seconds
        return "Expiring Data Has been saved";
    }

    @GetMapping("/expiring/read-data/{key}")
    public String readDataExpiring(@PathVariable String key) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String value = ops.get(key);
        return "Value from Redis: " + value;
    }

    @GetMapping("/expiring/save-list")
    public String saveExpiringList(@RequestParam String key, @RequestParam List<String> values) {
        expiringDataService.saveListDataWithExpiration(key, values, 500);
        return "Expiring List Data Has been saved";
    }

    @GetMapping("/expiring/read-list-data/{key}")
    public List<String> getExpiringData(@PathVariable String key) {
        return expiringDataService.getData(key);
    }




}
