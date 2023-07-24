package com.example.userservice;

import feign.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient // 유레카 클리언트로 등록
@EnableFeignClients  // restTemplete을 추상화 한 인터페이스  sprong cloud netflix 라이브러리에서제공
public class UserServiceApplication {


    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }



    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Bean
    @LoadBalanced  // 유레카에 등록된 이름을 주소로 하여 찾아간다.
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }


    // 페인클라이언트 호출시 로그
    @Bean
    public Logger.Level feignLoggerLevel(){

        return Logger.Level.FULL;
    }



    // @Component로 등록시 어차피 빈으로 등록되기때문에 여기 @Bean로 안해줘도된다
//    @Bean
//    public FeignErrorDecoder feignErrorDecoder(){
//        return new FeignErrorDecoder(environment);
//    }
}
