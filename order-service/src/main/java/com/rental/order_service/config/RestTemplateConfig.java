package com.rental.order_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Tách RestTemplate ra config riêng thay vì khai báo trong main class.
 * Theo chuẩn demo_lam_NMH (import-service/config/RestTemplateConfig.java).
 */
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
