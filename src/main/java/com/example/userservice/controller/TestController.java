package com.example.userservice.controller;

import com.example.userservice.vo.Greeting;
import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {

    private Environment env;
    private Greeting greeting;

    public TestController(Environment env, Greeting greeting) {
        this.env = env;
        this.greeting = greeting;
    }

    @GetMapping("/health_check")
    @Timed(value="users.status",longTask= true)
    public String status(){
        return String.format("It's Working in User Service on Port %s",
                env.getProperty("local.server.port"))
                +", port(server port)="+env.getProperty("server.port")
                +", token secret="+env.getProperty("token.secret")
                +", token expiration_time="+env.getProperty("token.expiration_time")
                ;
    }
    @GetMapping("/welcome")
    @Timed(value="users.welcome",longTask= true)
    public String welcome(){
//        return env.getProperty("greeting.message");
        return greeting.getMessage();

    }
    @PostMapping("/naverworks")
    public ResponseEntity<Map<String,String>> getMessage(@RequestBody LinkedHashMap requestmap){
        Map <String,String> map = new HashMap<>();
        log.info(requestmap.get("type").toString());
        log.info(requestmap.get("source").toString());
        log.info(requestmap.get("content").toString());
        LinkedHashMap content = (LinkedHashMap) requestmap.get("content");
        log.info(content.get("text").toString());
        return  ResponseEntity.status(HttpStatus.OK).body(map);
    }
    @PostMapping("/getMessageList")
    public ResponseEntity<Map <String,String> > getMessageList(@RequestBody LinkedHashMap requestmap){
        Map <String,String> map = new HashMap<>();
        log.info(requestmap.get("type").toString());
        log.info(requestmap.get("source").toString());
        log.info(requestmap.get("content").toString());
        LinkedHashMap content = (LinkedHashMap) requestmap.get("content");
        log.info(content.get("text").toString());
        return  ResponseEntity.status(HttpStatus.OK).body(map);
    }
}
