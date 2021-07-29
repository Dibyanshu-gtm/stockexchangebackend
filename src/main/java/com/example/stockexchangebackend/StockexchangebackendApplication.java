package com.example.stockexchangebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class StockexchangebackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(StockexchangebackendApplication.class, args);
    }

}
