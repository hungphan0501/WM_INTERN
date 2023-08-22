package com.example.demo.service;

import com.example.demo.dto.UserRequest;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Locale;

@Service
public class UserService {

    @Autowired
    @Lazy
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Transactional
    public User save(UserRequest user) {
        String role = user.getType().toUpperCase();
        role = (role.equals("ADMIN") || role.equals("USER") || role.equals("CUSTOMER")) ? role : "USER";
        int level = (user.getLevel() >= 1 && user.getLevel() <= 5) ? user.getLevel() : 0;
        level = (role.equals("CUSTOMER")) ? level : 0;
        User currentUser = new User(user.getEmail(), user.getFullName(), user.getUsername(),
                passwordEncoder.encode(user.getPassword()), Arrays.asList(new Role("ROLE_" + role)), user.getAddress(), level);

        return userRepository.save(currentUser);
    }

    public boolean checkUserRole(Long userId, String roleName) {
        User user = userRepository.findById(userId).orElse(null);
        return user != null && user.getRoles().stream()
                .anyMatch(role -> role.getName().equals(roleName));
    }

    public boolean login(String username, String password) {
        System.out.println("Username: " + username + "\tPassword:" + password);
        User user = userRepository.findByUsername(username);
        if (user != null) {
            String encodedPassword = user.getPassword();
            if (passwordEncoder.matches(password, encodedPassword)) {
                return true;
            }
        }
        return false;
    }
}
