package com.example.gateway;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter implements GatewayFilter {

    private static final String TOKEN_PREFIX = "Bearer ";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        if (!request.getHeaders().containsKey("Authorization")) {
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        WebClient webClient = WebClient.create("http://localhost:8081/auth");
        final String token = request.getHeaders().getOrEmpty("Authorization").get(0).substring(TOKEN_PREFIX.length());
        var statusCodeMono = webClient.get()
                .uri("/verify?token=" + token)
                .exchangeToMono(response ->
                    Mono.just(response.statusCode())
                );
        return statusCodeMono.flatMap(statusCode -> {
            if (!statusCode.is2xxSuccessful()) {
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }
            else return chain.filter(exchange);
        });
    }
}
