package com.example.demo.service;


import com.example.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

@Service
public class LoginService {

//    private final Jedis jedis = new Jedis("localhost",6379);
//
//    public void loginUser(User user) {
//        // Ghi dữ liệu vào Redis
//        jedis.hset("users", user.getUsername(), user.getPassword());
//    }
//
//    public boolean validateUser(String username, String password) {
//        // Đọc dữ liệu từ Redis và kiểm tra thông tin đăng nhập
//        String storedPassword = jedis.hget("users", username);
//        return storedPassword != null && storedPassword.equals(password);
//    }
}