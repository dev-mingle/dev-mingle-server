package com.example.dm.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableAspectJAutoProxy
@EnableJpaAuditing
@ComponentScan("com.example")
@EntityScan("com.example")
@EnableJpaRepositories("com.example")
@ServletComponentScan("com.example")
@Configuration
public class ApplicationConfig {

    @PersistenceContext
    public EntityManager entityManager;

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .findAndRegisterModules() // JDK ServiceLoader 에 의해 기본적으로 제공되는 모듈들을 찾아 넣어줌
                .enable(SerializationFeature.INDENT_OUTPUT) // JSON 형태로 출력할때 인덴트 맞춰서 출력
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS) // Date 를 TimeStamp 형식으로 직렬화 해제
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false) // false = 역직렬화하는 대상에 대해 모르는 필드가 있어도 무시
                .registerModule(new JavaTimeModule());
    }

}
