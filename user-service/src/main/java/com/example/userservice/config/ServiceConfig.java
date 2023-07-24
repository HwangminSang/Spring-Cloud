package com.example.userservice.config;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class ServiceConfig {


    @Value(value = "${service.check}")
    private String service;



}
