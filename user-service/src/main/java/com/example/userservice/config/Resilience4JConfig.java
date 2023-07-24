package com.example.userservice.config;


import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;


// 서킷브레이커를 구현한 리슬리안스 4j
 // 서비스가 통신상 문제가 생겼을경우 처리 해주는것  문제있으면 open
//Resilience4j를 Router에 지정하기 위해서, api-gateway의 필터에서 지정하시면 될 것 같습니다
@Configuration
public class Resilience4JConfig {




    // 커스텀  디폴트 설정
    @Bean
    public Customizer<Resilience4JCircuitBreakerFactory> globalCustomConfigureation(){


        // 커스텀하게 설정
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .failureRateThreshold(4)
                .waitDurationInOpenState(Duration.ofMillis(4000))
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED) // 기본 카운트
                .slidingWindowSize(2)
                .build();

        TimeLimiterConfig timeLimiterConfig = TimeLimiterConfig.custom()
                .timeoutDuration(Duration.ofSeconds(4))
                .build();


        return factory ->  factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                .circuitBreakerConfig(circuitBreakerConfig)
                .timeLimiterConfig(timeLimiterConfig)
                .build()
        );

    }


    /**
     * 아래처럼 커스텀하게 여러개를 만들수있고 사용시
     *  해당 서비스에서 이렇게 생성하면 된다
     * CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitBreaker1");
     * CircuitBreaker circuitBreaker2 = circuitBreakerFactory.create("circuitBreaker2");
     * @return
     */

    @Bean
    public Customizer<Resilience4JCircuitBreakerFactory> specificCustomConfiguration1() {
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .failureRateThreshold(6).waitDurationInOpenState(Duration.ofMillis(1000))
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .slidingWindowSize(3).build();

        TimeLimiterConfig timeLimiterConfig = TimeLimiterConfig.custom()
                .timeoutDuration(Duration.ofSeconds(4)).build();

        return factory -> factory.configure(builder -> builder
                .circuitBreakerConfig(circuitBreakerConfig)
                .timeLimiterConfig(timeLimiterConfig).build(), "circuitBreaker1");
    }

    @Bean
    public Customizer<Resilience4JCircuitBreakerFactory> specificCustomConfiguration2() {
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .failureRateThreshold(8).waitDurationInOpenState(Duration.ofMillis(1000))
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .slidingWindowSize(4).build();

        TimeLimiterConfig timeLimiterConfig = TimeLimiterConfig.custom()
                .timeoutDuration(Duration.ofSeconds(4)).build();

        return factory -> factory.configure(builder -> builder.circuitBreakerConfig(circuitBreakerConfig)
                        .timeLimiterConfig(timeLimiterConfig).build(),
                "circuitBreaker2");
    }
}
