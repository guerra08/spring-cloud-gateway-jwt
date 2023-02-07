package com.example.gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Routing {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public Routing(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p
                        .path("/auth/login")
                        .uri("http://localhost:8081/auth/login"))
                .route(p -> p
                        .path("/auth/register")
                        .uri("http://localhost:8081/auth/register"))
                .route(p -> p
                        .path("/auth/verify")
                        .uri("http://localhost:8081/auth/verify"))
                .route(p -> p
                        .path("/hello")
                        .filters(f -> f.filter(jwtAuthenticationFilter))
                        .uri("http://localhost:8082")
                ).build();
    }

}
