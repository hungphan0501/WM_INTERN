package com.example.demo.controller;

import com.example.demo.config.jwt.JwtTokenProvider;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    JwtTokenProvider jwtTokenProvider;





}