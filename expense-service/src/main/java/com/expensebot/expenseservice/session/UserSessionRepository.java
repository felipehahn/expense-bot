package com.expensebot.expenseservice.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
public class UserSessionRepository {
    private static final String PREFIX = "session:";
    private static final Duration TTL = Duration.ofMinutes(10);

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void save(Long userId, UserSession session) {
        redisTemplate.opsForValue().set(PREFIX + userId, session, TTL);
    }

    public UserSession get(Long userId) {
        return (UserSession) redisTemplate.opsForValue().get(PREFIX + userId);
    }

    public void remove(Long userId) {
        redisTemplate.delete(PREFIX + userId);
    }
}
