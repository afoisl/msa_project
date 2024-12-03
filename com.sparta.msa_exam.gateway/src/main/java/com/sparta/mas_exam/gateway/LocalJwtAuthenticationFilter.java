package com.sparta.mas_exam.gateway;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Objects;

@Slf4j
@Component
public class LocalJwtAuthenticationFilter implements GlobalFilter {

    @Value("${service.jwt.secret-key}")
    private String secretKey;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        if (path.equals("/auth/signIn") || path.equals("/auth/signUp")) {
            return chain.filter(exchange);  // /signIn 경로는 필터를 적용하지 않음
        }


        if (path.startsWith("/product") || path.startsWith("/order")) {
            String token = extractToken(exchange);

            if (token == null || !validateToken(token, exchange)) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                exchange.getResponse().getHeaders()
                        .add("X-Error-Message", "Unauthorized access, please login");
                return exchange.getResponse().setComplete();
            }
        }

        // 접속중인 포트 헤더에 추가
        exchange.getResponse().getHeaders().add("Server-Port",
               String.valueOf(Objects.requireNonNull(exchange.getRequest().getLocalAddress()).getPort()));

        return chain.filter(exchange);
    }

    private String extractToken(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    private boolean validateToken(String token, ServerWebExchange exchange) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
            Jws<Claims> claimsJws = Jwts.parser()
                    .verifyWith(key)
                    .build().parseSignedClaims(token);

            log.info("#####payload :: " + claimsJws.getPayload().toString());
            Claims claims = claimsJws.getBody();
            exchange.getRequest().mutate()
                    .header("X-User-Id", claims.get("user_id").toString())
                    .header("X-Role", claims.get("role").toString())
                    .build();
            // 추가적인 검증 로직 (예: 토큰 만료 여부 확인 등)을 여기에 추가할 수 있습니다.

            if (claims.getExpiration().before(new Date())) {
                log.error("Token has expired");
                return false;
            }

            return true;
        } catch (Exception e) {
            log.error("Invalid token: {}", e.getMessage());
            return false;
        }
    }
}