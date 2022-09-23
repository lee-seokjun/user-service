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
@EnableDiscoveryClient//registry eureka server
@EnableFeignClients
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        //autowired 하기 위해 Bean 생성
        return new BCryptPasswordEncoder();
    }
    @Bean
    @LoadBalanced//service간 기존 ip:port 에서 service명으로 동작하도록
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
    public Logger.Level feignLoggerLevel(){
        return Logger.Level.FULL;
    }
}
