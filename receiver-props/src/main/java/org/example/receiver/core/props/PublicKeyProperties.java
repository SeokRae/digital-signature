package org.example.receiver.core.props;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.List;
import java.util.Optional;

@Slf4j
@Getter
@ToString
@ConfigurationProperties(prefix = "signature")
public class PublicKeyProperties {

  private final List<PublicKeyPath> pairs;

  @ConstructorBinding
  public PublicKeyProperties(List<PublicKeyPath> pairs) {
    this.pairs = pairs;
    log.info("{}", pairs);
  }
  
  public boolean absent(String keyId) {
    return pairs.stream().noneMatch(pair -> pair.getId().equals(keyId));
  }
  
  public String publicKey(String keyId) {
    for(PublicKeyPath pair : pairs) {
      if(pair.getId().equals(keyId)) {
        return pair.getPublicKey();
      }
    }
    throw new RuntimeException("Invalid keyId");
  }
  @Getter
  @ToString
  public static class PublicKeyPath {
    private final String id;
    private final String publicKey;

    public PublicKeyPath(String id, String publicKey) {
      this.id = id;
      this.publicKey = publicKey;
    }
  }
}
