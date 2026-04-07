package com.rental.order_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Refactor: bỏ @Bean RestTemplate ra khỏi main class,
 * chuyển vào RestTemplateConfig theo chuẩn demo_lam_NMH.
 */
@SpringBootApplication
public class OrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
}
