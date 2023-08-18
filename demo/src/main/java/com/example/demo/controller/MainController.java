package com.example.demo.controller;

import com.example.demo.config.jwt.JwtTokenProvider;
import com.example.demo.dto.UserResponse;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class MainController {
    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    UserService userService;

    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo(@RequestHeader(value = "Authorization") String token) {
        Long userId = jwtTokenProvider.getUserIdFromToken(token);
        if(userId != null){
            User user = userService.getUserById(userId);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            UserResponse userResponse = new UserResponse(user.getUsername(),user.getEmail(),user.getFullName(),user.getAddress());
            return ResponseEntity.ok(userResponse);
        }
        return ResponseEntity.badRequest().body("error");

    }

}
