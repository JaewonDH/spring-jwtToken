package com.jwt.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
// CrossOrigin(인증x) 사용하지 않기 위해 사용하는 필터 시큐리티 필터에 등록 인증
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter(){
        UrlBasedCorsConfigurationSource source= new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*"); // 모든 ip 응답 허용
        config.addAllowedHeader("*");  // 모든 header에 응답 허용
        config.addAllowedMethod("*"); // post, get put delete pathc 허용
        source.registerCorsConfiguration("/api**",config);
        return new CorsFilter(source);
    }
}