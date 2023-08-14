package com.example.demo.controller;

import com.example.demo.config.JwtConfig;
import com.example.demo.config.JwtTokenProvider;
import com.example.demo.dto.DeviceEntType;
import com.example.demo.dto.TokenEntType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TokenController {

    @Autowired
    JwtConfig jwtConfig;
    private final JwtTokenProvider jwtTokenProvider;

    public TokenController(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/token")
    public ResponseEntity<?> generateDeviceToken(@RequestBody DeviceEntType deviceData,
                                                 @RequestHeader(value = "Authorization", required = false) String token) {
        try {
            TokenEntType currentToken = jwtTokenProvider.generateDeviceToken(deviceData, token);

            return ResponseEntity.ok(currentToken);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while generating device token.");
        }
    }




}

