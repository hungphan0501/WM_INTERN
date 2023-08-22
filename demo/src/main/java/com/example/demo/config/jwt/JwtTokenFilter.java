package com.example.demo.config.jwt;

import com.example.demo.component.TokenCache;
import com.example.demo.service.UserDetailsServiceImpl;
import com.example.demo.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
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
import java.util.List;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtConfig jwtConfig;

    @Lazy
    private final UserDetailsServiceImpl userDetailsService;
    @Autowired
    UserService userService;

    @Autowired
    TokenCache tokenCache;

    public JwtTokenFilter(UserDetailsServiceImpl userDetailsService) {
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
            if (isValidToken(token)) {
                Claims claims = Jwts.parser().setSigningKey(jwtConfig.getSecretKey()).parseClaimsJws(token).getBody();
                Long userId = claims.get("userId", Long.class);
                Date expirationDate = claims.getExpiration();

                if (expirationDate == null || expirationDate.before(new Date())) {
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    return;
                }
                List<String> publicUrlsList = jwtConfig.publicUrlsList();
                if (publicUrlsList.contains(request.getRequestURI())) {
                    chain.doFilter(request, response);
                    return;
                }
                if (userId != null) {
                    User userDetails = (User) userDetailsService.loadUserByUsername(userService.getUserById(userId).getUsername());
                    Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    System.out.println(authentication.getPrincipal());
                    if (!token.equals(tokenCache.getLatestToken())) {
                        response.setStatus(HttpStatus.UNAUTHORIZED.value());
                        return;
                    }
                }
            } else {
                throw new ExpiredJwtException(null, null, "Token has expired");
            }
        } catch (ExpiredJwtException e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        } catch (Exception e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }
        chain.doFilter(request, response);
    }

    private boolean isValidToken(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(jwtConfig.getSecretKey()).parseClaimsJws(token).getBody();
            Date expirationDate = claims.getExpiration();
            if (expirationDate == null || expirationDate.before(new Date())) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }


}
