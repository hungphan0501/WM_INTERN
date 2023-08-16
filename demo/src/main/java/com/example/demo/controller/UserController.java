package com.example.demo.controller;

import com.example.demo.config.jwt.JwtTokenFilter;
import com.example.demo.config.jwt.JwtTokenProvider;
import com.example.demo.dto.UserInfoDTo;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo(@RequestHeader(value = "Authorization") String token) {
        Long userId = jwtTokenProvider.getUserIdFromToken(token);
        if(userId != null){
            User user = userService.getUserById(userId);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            UserInfoDTo userDto = new UserInfoDTo();
            userDto.setUsername(user.getUsername());
            userDto.setEmail(user.getEmail());
            return ResponseEntity.ok(userDto);
        }
        return ResponseEntity.badRequest().body("error");

    }
}
