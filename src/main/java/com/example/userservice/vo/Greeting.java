package com.example.userservice.vo;

import jdk.nashorn.internal.objects.annotations.Constructor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data //setter getter
@AllArgsConstructor
@NoArgsConstructor
public class Greeting {
    @Value("${greeting.message}")
    private String message;
}
