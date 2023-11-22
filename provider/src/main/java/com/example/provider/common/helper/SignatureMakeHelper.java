package com.example.provider.common.helper;


import com.example.provider.common.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.util.io.pem.PemObject;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringWriter;
import java.security.*;

@Slf4j
@Component
public final class SignatureMakeHelper {
  /**
   * 샘플 프로젝트 resources 디렉토리에 pem 파일 생성하기 위한 경로로 로컬 경로 설정
   */
  private static final String PRIVATE_KEY_PATH = "/Users/seok/IdeaProjects/work/digital-signature/provider/src/main/resources/privateKey.pem";
  private static final String PUBLIC_KEY_PATH = "/Users/seok/IdeaProjects/work/digital-signature/receiver/src/main/resources/publicKey.pem";
  private static final String PRIVATE_KEY = "PRIVATE KEY";
  private static final String ALGORITHM = "Ed25519";

  public void generateKeyPair() {
    try {
      KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
      KeyPair keyPair = keyGen.generateKeyPair();

      String privateKeyToPem = convertPrivateKeyToPem(keyPair.getPrivate());
      log.info("{}", privateKeyToPem);
      String publicKeyToPem = convertPublicKeyToPem(keyPair.getPublic());
      log.info("{}", publicKeyToPem);

      FileUtils.movePemToFile(privateKeyToPem, PRIVATE_KEY_PATH);
      FileUtils.movePemToFile(publicKeyToPem, PUBLIC_KEY_PATH);

    } catch (NoSuchAlgorithmException e) {
      log.error("알고리즘 확인 필요: {}", e.getMessage());
      throw new RuntimeException(e);
    } catch (IOException e) {
      log.info("파일 생성 실패: {}", e.getMessage());
      throw new RuntimeException(e);
    }
  }

  /**
   * 개인키 pem 파일로 생성 (Ed25519)
   *
   * @param privateKey
   * @return
   */
  public String convertPrivateKeyToPem(PrivateKey privateKey) {
    log.info("privateKey.getEncoded().length : {}", privateKey.getEncoded().length);
    PemObject pemObject = new PemObject(PRIVATE_KEY, privateKey.getEncoded());
    StringWriter stringWriter = new StringWriter();
    try (JcaPEMWriter pemWriter = new JcaPEMWriter(stringWriter)) {
      pemWriter.writeObject(pemObject);
    } catch (IOException e) {
      log.error("IOException {}", e.getMessage());
    }
    return stringWriter.toString();
  }

  /**
   * 공개키 pem 파일로 생성 (Ed25519)
   *
   * @param publicKey
   * @return
   */
  public String convertPublicKeyToPem(PublicKey publicKey) {
    log.info("publicKey.getEncoded().length : {}", publicKey.getEncoded().length);
    StringWriter stringWriter = new StringWriter();
    try (JcaPEMWriter pemWriter = new JcaPEMWriter(stringWriter)) {
      pemWriter.writeObject(publicKey);
    } catch (IOException e) {
      log.error("IOException {}", e.getMessage());
    }
    return stringWriter.toString();
  }

}
