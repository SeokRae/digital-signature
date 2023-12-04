package com.example.receiver.common.helper;

import com.example.receiver.common.utils.FileUtils;
import com.example.receiver.core.props.PublicKeyPaths;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 키 관리(keyId, public-key path)
 */
@Slf4j
@Component
public class PublicKeyManager {
  private final Map<String, Resource> publicKeyResources = new HashMap<>();

  public PublicKeyManager(PublicKeyPaths publicKeyPaths) {
    initialize(publicKeyPaths);
  }

  private void initialize(PublicKeyPaths publicKeyPaths) {
    for (PublicKeyPaths.PublicKeyPath publicKeyPath : publicKeyPaths.getPairs()) {
      Resource publicKeyResource = FileUtils.readFileAsResource(publicKeyPath.getPath());
      publicKeyResources.put(publicKeyPath.getId(), publicKeyResource);
    }
    log.info("{}", publicKeyResources);
  }

  public Resource getPublicKey(String id) {
    Resource resource = publicKeyResources.get(id);
    log.info("resource: {} {}", id, resource);
    return resource;
  }
}
