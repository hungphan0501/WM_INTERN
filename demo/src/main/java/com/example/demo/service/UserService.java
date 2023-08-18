package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

//    @Transactional(readOnly = true)
    public User getUserByUsername(String username){
        return userRepository.findByUsername(username);
    }

//    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

//    @Transactional(readOnly = true)
    public User getUserById(Long id){
        return userRepository.findById(id).orElse(null);
    }

    @Transactional
    public User save(User user) {
        userRepository.save(user);
        return user;
    }
}
