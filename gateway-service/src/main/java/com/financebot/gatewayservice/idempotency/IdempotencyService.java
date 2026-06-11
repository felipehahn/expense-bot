package com.financebot.gatewayservice.idempotency;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class IdempotencyService {

    private final String CACHE_PREFIX = "idempotency:telegram:";

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final Duration TTL = Duration.ofHours(24);

    public boolean isNew(String eventId) {
        String key = CACHE_PREFIX + eventId;
        Boolean set = redisTemplate.opsForValue().setIfAbsent(key, "1", TTL);
        return Boolean.TRUE.equals(set);
    }

}
