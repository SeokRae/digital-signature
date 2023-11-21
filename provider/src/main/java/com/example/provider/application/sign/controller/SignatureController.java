package com.example.provider.application.sign.controller;

import com.example.provider.common.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import static com.example.provider.common.helper.SignatureHelper.convertPrivateKeyToPem;
import static com.example.provider.common.helper.SignatureHelper.convertPublicKeyToPem;

@Slf4j
@RestController
@RequestMapping("/v1")
public class SignatureController {

  private static final String PRIVATE_KEY_PATH = "/Users/seok/IdeaProjects/work/digital-signature/provider/src/main/resources/privateKey.pem";
  private static final String PUBLIC_KEY_PATH = "/Users/seok/IdeaProjects/work/digital-signature/receiver/src/main/resources/publicKey.pem";
  private static final String ALGORITHM = "Ed25519";

  /**
   * privateKey, publicKey 생성
   *
   * @return
   * @throws NoSuchAlgorithmException
   */
  @PostMapping("/sign")
  public ResponseEntity<?> sign() throws NoSuchAlgorithmException {
    KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
    KeyPair keyPair = keyGen.generateKeyPair();

    String privateKeyToPem = convertPrivateKeyToPem(keyPair.getPrivate());
    log.info("{}", privateKeyToPem);
    String publicKeyToPem = convertPublicKeyToPem(keyPair.getPublic());
    log.info("{}", publicKeyToPem);
    try {
      FileUtils.movePemToFile(privateKeyToPem, PRIVATE_KEY_PATH);
      FileUtils.movePemToFile(publicKeyToPem, PUBLIC_KEY_PATH);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return ResponseEntity.ok().build();
  }
}
