package com.example.demo.component;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class TokenCache {

    private final Cache<String, Boolean> tokenCache;
    private final List<String> tokenList;
    private String latestToken;

    public TokenCache() {
        tokenCache = Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.HOURS)
                .maximumSize(1000)
                .build();
        tokenList = new LinkedList<>();
    }

    public boolean isNewToken(String token) {
        return tokenCache.getIfPresent(token) == null;
    }

    public void addToken(String token) {
        if (!isNewToken(token)) {
            tokenCache.put(token, true);
        }
        latestToken = token;
        tokenList.add(token); // Thêm token vào danh sách
    }

    public String getLatestToken() {
        return latestToken;
    }

}
