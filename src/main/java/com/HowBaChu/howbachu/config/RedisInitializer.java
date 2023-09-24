package com.HowBaChu.howbachu.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisInitializer implements CommandLineRunner {

    private final RedisTemplate<String, String> redisTemplate;

    public void clearRedisData() {
        redisTemplate.execute((RedisCallback<Object>) connection -> {
            connection.flushAll();
            return null;
        });
    }

    @Override
    public void run(String... args) throws Exception {
        clearRedisData();
    }
}