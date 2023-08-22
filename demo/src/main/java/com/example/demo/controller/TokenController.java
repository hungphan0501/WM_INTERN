package com.example.demo.controller;

import com.example.demo.config.jwt.JwtConfig;
import com.example.demo.config.jwt.JwtTokenProvider;
import com.example.demo.dto.DeviceEntType;
import com.example.demo.dto.TokenEntType;
import com.example.demo.dto.TokenResponse;
import io.jsonwebtoken.ExpiredJwtException;
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
            TokenResponse currentToken = jwtTokenProvider.generateToken(deviceData, token);
            if (currentToken != null) {
                return ResponseEntity.ok(currentToken);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("An error occurred while generating device token.");
            }

        } catch (Exception e) {
            if (e instanceof ExpiredJwtException) {
                e.printStackTrace();
                // Xử lý ngoại lệ hết hạn token
                TokenResponse newToken = jwtTokenProvider.generateToken(deviceData, null);
                if (newToken != null) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body("Token has expired. New token generated.");
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("An error occurred while generating device token.");
                }
            } else {
                // Xử lý các ngoại lệ khác
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("An error occurred while generating device token.");
            }
        }
    }

    @GetMapping("/banner")
    public String getBanner() {
        return "This is a banner";
    }



}

