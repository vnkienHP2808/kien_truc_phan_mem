package com.rental.api_gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.uri;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;

/**
 * Java-based route config
 * Eureka + CORS
 *
 * Cổng 8080 (gateway):
 *   /api/khach-hang/**  → customer-service :8081
 *   /api/trang-phuc/**  → costume-service  :8082
 *   /api/phieu-thue/**  → order-service    :8083
 */
@Configuration
public class GatewayConfig {

    @Bean
    public RouterFunction<ServerResponse> customerServiceRoute() {
        return route("customer-service")
                .route(RequestPredicates.path("/api/khach-hang/**"), http())
                .before(uri("http://localhost:8081"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> costumeServiceRoute() {
        return route("costume-service")
                .route(RequestPredicates.path("/api/trang-phuc/**"), http())
                .before(uri("http://localhost:8082"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> orderServiceRoute() {
        return route("order-service")
                .route(RequestPredicates.path("/api/phieu-thue/**"), http())
                .before(uri("http://localhost:8083"))
                .build();
    }
}
