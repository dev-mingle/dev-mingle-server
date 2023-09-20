package com.example.dm.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableRedisRepositories
@EnableCaching(proxyTargetClass = true)
public class RedisCacheConfig implements CachingConfigurer {

    @Value("${spring.redis.host}")
    private String REDIS_HOST;

    @Value("${spring.redis.port}")
    private int REDIS_PORT;

    @Value("${spring.redis.cache-database}")
    private int REDIS_DATABASE;

    @Value("${spring.redis.password}")
    private String REDIS_PASSWORD;

    @Value("${spring.redis.ssl}")
    private Boolean REDIS_SSL;

    @Value("${api.redis.key-prefix.default}")
    private String REDIS_KEY_PREFIX;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration standaloneConfiguration = new RedisStandaloneConfiguration(REDIS_HOST, REDIS_PORT);
        standaloneConfiguration.setPassword(REDIS_PASSWORD);
        standaloneConfiguration.setDatabase(REDIS_DATABASE);
        LettuceClientConfiguration.LettuceClientConfigurationBuilder builder = LettuceClientConfiguration.builder();

        if (REDIS_SSL) {
            builder.useSsl();
        }
        return new LettuceConnectionFactory(standaloneConfiguration, builder.build());
    }

    /**
     * Redis Template 정의
     * key - String
     * value - Generic
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        return redisTemplate;
    }

    /**
     * Default Cache Manager 정의
     * key - String
     * value - Generic
     */
    @Bean
    @Override
    public CacheManager cacheManager() {
        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(redisConnectionFactory());

        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer(new ObjectMapper())))
                .prefixCacheNameWith(REDIS_KEY_PREFIX); // {redisKeyPrefix:키별칭::키이름}

        Map<String, RedisCacheConfiguration> cacheConfigurations = setEntryTTL(configuration);

        return builder.cacheDefaults(configuration).withInitialCacheConfigurations(cacheConfigurations).build();
    }

    /**
     * CacheManager 캐시 유효기간 설정
     * cache 별칭으로 key 구분 (@Cacheable - value)
     *
     * @param configuration
     * @return
     */
    private Map<String, RedisCacheConfiguration> setEntryTTL(RedisCacheConfiguration configuration) {
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();

        // refresh token - 30일
        cacheConfigurations.put("refresh-token", configuration.entryTtl(Duration.ofDays(30L)));

        return cacheConfigurations;
    }

}
