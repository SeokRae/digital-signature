package com.example.provider.common.helper;


import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

@Slf4j
@Component
public final class SignatureHelper {
  public static final String ALGORITHM = "Ed25519";

  /**
   * 개인키 PEM 파일을 읽어 개인키 객체를 생성
   *
   * @param privateKeyResource
   * @return
   */
  public static PrivateKey readPrivateByPem(final Resource privateKeyResource) {
    try (final InputStream stream = privateKeyResource.getInputStream()) {
      final PEMParser pemParser = new PEMParser(new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8)));
      final Object readObject = pemParser.readObject();
      if (readObject instanceof PrivateKeyInfo) {
        PrivateKeyInfo privateKeyInfo = (PrivateKeyInfo) readObject;
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
        return converter.getPrivateKey(privateKeyInfo);
      } else {
        throw new IllegalArgumentException(String.format("Provided key is invalid: %s.", privateKeyResource.getFilename()));
      }
    } catch (IOException e) {
      throw new IllegalArgumentException(String.format("Provided key is invalid: %s.", privateKeyResource.getFilename()), e);
    }
  }

  /**
   * 개인키를 통한 디지털 서명(signature) 생성
   * @param privateKey
   * @param message
   * @return
   */
  public static byte[] createSignature(PrivateKey privateKey, byte[] message) {
    try {
      Signature signature = Signature.getInstance(ALGORITHM);
      signature.initSign(privateKey);
      signature.update(message);
      return signature.sign();
    } catch (NoSuchAlgorithmException e) {
      log.error("NoSuchAlgorithmException {}", e.getMessage());
      throw new RuntimeException(e);
    } catch (SignatureException e) {
      log.error("SignatureException {}", e.getMessage());
      throw new RuntimeException(e);
    } catch (InvalidKeyException e) {
      log.error("InvalidKeyException {}", e.getMessage());
      throw new RuntimeException(e);
    }
  }
}
