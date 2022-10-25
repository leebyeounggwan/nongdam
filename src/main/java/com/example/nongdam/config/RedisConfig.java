package com.example.nongdam.config;

import com.example.nongdam.entity.RefreshToken;
import io.lettuce.core.RedisURI;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnection;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableRedisRepositories
@RequiredArgsConstructor
public class RedisConfig {

    private final RedisProperties redisProperties;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisURI redisURI = RedisURI.create(redisProperties.getUrl());
        RedisConfiguration configuration = LettuceConnectionFactory.createRedisConfiguration(redisURI);
        LettuceConnectionFactory factory = new LettuceConnectionFactory(configuration);
        factory.afterPropertiesSet();
        return factory;
    }

    @Bean
    public RedisTemplate<String, RefreshToken> tokenRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, RefreshToken> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(RefreshToken.class));

        return redisTemplate;
    }

//    @Bean
//    public RedisTemplate<?, ?> redisTemplate( RedisConnectionFactory redisConnectionFactory) {
//
//        RedisTemplate<?, ?> redisTemplate = new RedisTemplate<>();
//        // 레디스 서버와의 연결정보
//        redisTemplate.setConnectionFactory(redisConnectionFactory);
//
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(RefreshToken.class));
//        return redisTemplate;
//    }

}
