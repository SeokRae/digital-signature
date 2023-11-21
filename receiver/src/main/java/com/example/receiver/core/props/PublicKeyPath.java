package com.example.receiver.core.props;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Slf4j
@Getter
@ToString
@ConfigurationProperties(prefix = "encrypt")
public class PublicKeyPath {
  private final String path;

  @ConstructorBinding
  public PublicKeyPath(String path) {
    this.path = path;
    log.info("{}", path);
  }
}
