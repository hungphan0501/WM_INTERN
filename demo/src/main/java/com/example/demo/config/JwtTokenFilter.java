package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtConfig jwtConfig;

    private final UserDetailsService userDetailsService;

    public JwtTokenFilter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);

        try {
            Claims claims = Jwts.parser().setSigningKey(jwtConfig.getSecretKey()).parseClaimsJws(token).getBody();
            String username = claims.getSubject();

            if (username != null) {
                User userDetails = (User) userDetailsService.loadUserByUsername(username);

                if (isValidToken(token, userDetails)) {
                    Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception e) {
            // Handle token validation failure
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean isValidToken(String token, User userDetails) {
        try {
            Claims claims = Jwts.parser().setSigningKey(jwtConfig.getSecretKey()).parseClaimsJws(token).getBody();
            String username = claims.getSubject();

            // Kiểm tra xem token có được phát hành cho người dùng cụ thể không
            if (!username.equals(userDetails.getUsername())) {
                return false;
            }

            // Kiểm tra hạn sử dụng của token
            Date expirationDate = claims.getExpiration();
            if (expirationDate == null || expirationDate.before(new Date())) {
                return false;
            }

            // Thêm các kiểm tra khác nếu cần

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
