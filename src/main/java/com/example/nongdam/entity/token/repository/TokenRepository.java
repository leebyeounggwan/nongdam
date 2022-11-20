package com.example.nongdam.entity.token.repository;

import com.example.nongdam.entity.token.entity.RefreshToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Slf4j
@Repository
@RequiredArgsConstructor
public class TokenRepository {
    private final RedisTemplate<String, RefreshToken> tokenRedisTemplate;
    private final static Duration TOKEN_CACHE_TTL = Duration.ofHours(3);

    public void setToken(RefreshToken token) {
        String key = getKey(token.getToken());
        //log.info("Set Token to Redis {} : {}", key, token);
        tokenRedisTemplate.opsForValue().set(key, token, TOKEN_CACHE_TTL);
    }

    public RefreshToken getToken(String token) {
        String key = getKey(token);
        RefreshToken refreshToken = tokenRedisTemplate.opsForValue().get(key);
        //log.info("Get data from Redis {}:{}", key, token);
        return refreshToken;
    }

    public String getKey(String token) {
        return "token: " + token;
    }

}
