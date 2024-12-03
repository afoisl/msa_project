package com.sparta.mas_exam.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class AuthConfig {

    // SecurityFilterChain 빈을 정의합니다. 이 메서드는 Spring Security의 보안 필터 체인을 구성합니다.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF 보호를 비활성화합니다. CSRF 보호는 주로 브라우저 클라이언트를 대상으로 하는 공격을 방지하기 위해 사용됩니다.
                .csrf(AbstractHttpConfigurer::disable)
                // 요청에 대한 접근 권한을 설정합니다.
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/auth/sign-in").permitAll()
                        .requestMatchers("/auth/sign-up").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        // 그 외의 모든 요청은 인증이 필요합니다.
                        .anyRequest().authenticated()
                )
                // 세션 관리 정책을 정의합니다. 여기서는 세션을 사용하지 않도록 STATELESS로 설정합니다.
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin) // H2 콘솔사용
                );

        // 설정된 보안 필터 체인을 반환합니다.
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
