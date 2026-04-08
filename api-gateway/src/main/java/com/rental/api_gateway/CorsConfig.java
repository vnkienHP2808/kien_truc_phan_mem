package com.rental.api_gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

/**
 * CorsConfig – cho phép FE React gọi API Gateway.
 *
 * Các origin được phép:
 *   - http://localhost:3000  (fe_management – dev mode)
 *   - http://localhost:3001  (order_custome – dev mode)
 *   - http://localhost:5173  (Vite dev server nếu dùng)
 *
 * Khi chạy Docker: FE nginx proxy /api/ → api-gateway nội bộ,
 * CORS không cần thiết nhưng vẫn giữ để dev mode hoạt động.
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "http://localhost:3001",
                "http://localhost:5173"
        ));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}