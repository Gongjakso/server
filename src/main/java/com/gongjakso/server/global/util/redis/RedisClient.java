package com.gongjakso.server.global.util.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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


    /**
     * Redis에서 key에 해당하는 값을 증가시키는 메소드
     * @param key - 증가시킬 데이터의 key
     */
    public void incrementValue(String key) {
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        values.increment(key);
    }

    /**
     * Redis에서 특정 패턴에 맞는 키들을 검색하는 메소드
     * @param pattern - 검색할 키의 패턴
     * @return - 패턴에 맞는 키들의 리스트
     */
    public List<String> scanKeys(String pattern) {
        List<String> keys = new ArrayList<>();
        ScanOptions scanOptions = ScanOptions.scanOptions().match(pattern).count(1000).build(); // 한 번에 1000개씩 검색
        Cursor<byte[]> cursor = redisTemplate.getConnectionFactory().getConnection().scan(scanOptions);

        while (cursor.hasNext()) {
            keys.add(new String(cursor.next(), StandardCharsets.UTF_8));
        }

        return keys;
    }
}
