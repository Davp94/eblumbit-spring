package com.blumbit.eblumbit.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController 
public class HealthCheckController {

    @GetMapping("/health") 
    public String healthCheck() {
        return "Service is running!";
    }
    
}
