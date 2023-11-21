package com.example.receiver.core.config;


import com.example.receiver.core.filter.CustomLoggingFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

  @Bean
  public FilterRegistrationBean<CustomLoggingFilter> loggingFilter() {
    FilterRegistrationBean<CustomLoggingFilter> registrationBean = new FilterRegistrationBean<>();

    registrationBean.setFilter(new CustomLoggingFilter());
    registrationBean.addUrlPatterns("/v1/*"); // 필터를 적용할 URL 패턴 설정

    return registrationBean;
  }
}
