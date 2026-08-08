package com.studyslot.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // 비밀번호 BCrypt 해쉬 암호화
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // /admin으로 시작하는 경로만 인증 필요
                        .requestMatchers("/admin/**").authenticated()
                        // 그 외 나머지는 전부 인증 없이 접근 허용
                        .anyRequest().permitAll()
                )
                .csrf(csrf -> csrf.disable()); // 개발 중엔 편의상 비활성화, 나중에 필요하면 다시 켜기

        return http.build();
    }
}