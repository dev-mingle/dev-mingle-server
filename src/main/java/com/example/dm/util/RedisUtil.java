package com.example.dm.util;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class RedisUtil {

    private final RedisTemplate redisTemplate;

    private ValueOperations<String, Object> values;

    @PostConstruct
    public void init() {
        this.values = this.redisTemplate.opsForValue();
    }

    /**
     * 캐시 저장 - TTL 지정x
     */
    public void setValue(String key, Object value) {
        values.set(key, value);
    }

    /**
     * 캐시 저장 - TTL 지정o
     */
    public void setValue(String key, Object value, Duration duration) {
        values.set(key, value, duration);
    }

    /**
     * 해당 키가 존재하는지 확인
     */
    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * key로 value 찾기
     *
     * @return Object value
     */
    public Object getValue(String key) {
        if (hasKey(key)) {
            return values.get(key);
        }
        return null;
    }

    /**
     * key로 value 찾기
     *
     * @return String value
     */
    public String getStringValue(String key) {
        if (hasKey(key)) {
            // value serializer 를 String Serializer 로 교체
            redisTemplate.setValueSerializer(new StringRedisSerializer());
            values = redisTemplate.opsForValue();

            return String.valueOf(values.get(key));
        }
        return null;
    }

    /**
     * 캐시 삭제
     */
    public void deleteValue(List<String> key) {
        redisTemplate.delete(key);
    }

    /**
     * keyPattern 으로 KeyList 검색
     */
    public List<String> getKeyList(String keyPattern) {
        List<String> keyList = new ArrayList<>();
        redisTemplate.keys(keyPattern).forEach(key -> keyList.add((String) key));

        return keyList;
    }

}
