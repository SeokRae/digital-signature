package org.example.receiver.core.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

  @Bean
  public SecurityFilterChain configure(HttpSecurity http) throws Exception {
    http
            .formLogin().disable() // 기본 로그인 폼을 비활성화
            .httpBasic().disable() //  HTTP Basic 인증을 비활성화
            .rememberMe().disable() // RememberMe 기능을 비활성화
            .logout().disable() // 로그아웃 기능을 비활성화
            .csrf().disable(); // CSRF 보호 기능을 비활성화

    // 1. 프록시 헤더 처리 (iframe, embed, object 등)
    http
            .headers()
            .frameOptions().sameOrigin();

    // 3. 세션 관리
    http
//                .anonymous().disable() // 익명 사용자 허용하지 않음
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    // 6. CORS 설정
    http.cors();

    return http.build();
  }
}
