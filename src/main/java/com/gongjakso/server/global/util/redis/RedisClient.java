package com.gongjakso.server.global.util.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class RedisClient {

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * Redis에 값 삽입하는 메소드
     * @param key - 삽입하고자 하는 데이터의 key
     * @param value - 삽입하고자 하는 데이터의 value
     * @param timeout - 삽입하고자 하는 데이터의 유효 시간
     */
    public void setValue(String key, String value, Long timeout) {
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        values.set(key, value, Duration.ofMinutes(timeout));
    }

    /**
     * Redis에서 key값을 기반으로 value를 찾아서 반환하는 메소드
     * @param key - value를 찾을 데이터의 key 값
     * @return - 해당 데이터의 value 반환
     */
    public String getValue(String key) {
        ValueOperations<String, Object> values = redisTemplate.opsForValue();

        if(values.get(key) == null) {
            return "";
        }

        return values.get(key).toString();
    }

    /**
     * Redis에서 key에 해당하는 데이터를 삭제하는 메소드
     * @param key - 삭제할 데이터의 key를 의미
     */
    public void deleteValue(String key) {
        redisTemplate.delete(key);
    }
}
