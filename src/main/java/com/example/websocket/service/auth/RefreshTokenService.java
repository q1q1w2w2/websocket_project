package com.example.websocket.service.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RedisTemplate<String, String> redisTemplate;

    public void deleteRefreshToken(String subject) {
        log.info("Delete Refresh Token");
        redisTemplate.delete("refreshToken:" + subject);
    }

}
