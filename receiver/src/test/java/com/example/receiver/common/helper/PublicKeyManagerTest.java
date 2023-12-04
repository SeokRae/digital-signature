package com.example.receiver.common.helper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

@SpringBootTest
class PublicKeyManagerTest {

  @Autowired
  private PublicKeyManager publicKeyManager;

  @Test
  void key_public_key_test() {
    Resource publicKey = publicKeyManager.getPublicKey("20231123");
    System.out.println("publicKey = " + publicKey);
  }
}
