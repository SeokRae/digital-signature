package com.example.receiver.core.config;


import com.example.receiver.core.filter.SignatureAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

  private final SignatureAuthenticationFilter signatureAuthenticationFilter;

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

    // 5. 인증 및 인가
//    http
//            .authorizeRequests()
//            .antMatchers("/v1/echo_test").permitAll() // 공개 경로 설정
//            .anyRequest().authenticated(); // 나머지 요청은 인증 필요;
//
//    http
//            .addFilterBefore(signatureAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    // 6. CORS 설정
    http.cors();

    return http.build();
  }
}
