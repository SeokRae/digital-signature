package com.example.provider.core.props;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Slf4j
@Getter
@ToString
@ConfigurationProperties(prefix = "external")
public class ExternalUrlProperties {

  private final String echo;

  @ConstructorBinding
  public ExternalUrlProperties(String echo) {
    this.echo = echo;
  }
}
