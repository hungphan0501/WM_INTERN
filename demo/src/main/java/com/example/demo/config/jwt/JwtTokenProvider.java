package com.example.demo.config.jwt;

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
                .claim("tokenType","login")
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

        if (oldToken != null && oldToken.startsWith("Bearer ")) {
            String cleanToken = oldToken.replace("Bearer ", "");
            // Kiểm tra và gia hạn token cũ nếu có
            if (validateToken(cleanToken,"device")) {
                Claims oldTokenClaims = Jwts.parser()
                        .setSigningKey(jwtConfig.getSecretKey())
                        .parseClaimsJws(cleanToken)
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
                .claim("tokenType","device")
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

    public boolean validateToken(String token, String tokenType) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtConfig.getSecretKey())
                    .parseClaimsJws(token)
                    .getBody();

            String actualTokenType = claims.get("tokenType", String.class);

            if (!tokenType.equals(actualTokenType)) {
                return false; // Loại token không khớp
            }

            Date expirationDate = claims.getExpiration();
            if (expirationDate == null || expirationDate.before(new Date())) {
                return false; // Token đã hết hạn
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }


}

