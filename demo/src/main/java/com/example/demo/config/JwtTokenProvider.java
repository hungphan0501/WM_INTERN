package com.example.demo.config;

import com.example.demo.dto.DeviceEntType;
import com.example.demo.dto.TokenEntType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
public class JwtTokenProvider {

    private final JwtConfig jwtConfig;

    @Autowired
    public JwtTokenProvider(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    public String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtConfig.getValidityInMs());

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, jwtConfig.getSecretKey())
                .compact();
    }

    public TokenEntType generateDeviceToken(DeviceEntType deviceData, String oldToken) {
        String deviceId = deviceData.getDeviceId();
        String deviceType = deviceData.getType();
        String deviceUserId = deviceData.getUserId();

        Date now = new Date();
        Date expirationDate;

        if (oldToken != null) {
            // Kiểm tra và gia hạn token cũ nếu có
            if (validateToken(oldToken)) {
                Claims oldTokenClaims = Jwts.parser()
                        .setSigningKey(jwtConfig.getSecretKey())
                        .parseClaimsJws(oldToken)
                        .getBody();
                expirationDate = oldTokenClaims.getExpiration();
            } else {
                // Token cũ không hợp lệ, tạo mới token với thời hạn mới
                expirationDate = new Date(now.getTime() + jwtConfig.getValidityInMs());
            }
        } else {
            // Không có token cũ, tạo mới token với thời hạn mới
            expirationDate = new Date(now.getTime() + jwtConfig.getValidityInMs());
        }

        // Cập nhật thời gian hết hạn của token cũ (gia hạn)
        Claims claims = Jwts.claims()
                .setExpiration(expirationDate)
                .setIssuedAt(now);

        String token = Jwts.builder()
                .setClaims(claims)
                .claim("deviceId", deviceId)
                .claim("deviceType", deviceType)
                .claim("deviceUserId", deviceUserId)
                .signWith(SignatureAlgorithm.HS256, jwtConfig.getSecretKey())
                .compact();

        return new TokenEntType("SessionValue", token, deviceType, expirationDate.getTime(), deviceId);
    }



    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtConfig.getSecretKey())
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtConfig.getSecretKey()).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

