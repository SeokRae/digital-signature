package com.example.receiver.core.props;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.List;

@Slf4j
@Getter
@ToString
@ConfigurationProperties(prefix = "keys")
public class PublicKeyPaths {

  private final List<PublicKeyPath> pairs;

  @ConstructorBinding
  public PublicKeyPaths(List<PublicKeyPath> pairs) {
    this.pairs = pairs;
    log.info("{}", pairs);
  }

  @Getter
  @ToString
  public static class PublicKeyPath {
    private final String id;
    private final String path;

    public PublicKeyPath(String id, String path) {
      this.id = id;
      this.path = path;
    }
  }
}
