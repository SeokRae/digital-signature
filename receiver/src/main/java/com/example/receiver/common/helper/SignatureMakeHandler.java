package com.example.receiver.common.helper;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

@Slf4j
public final class SignatureMakeHandler {
  private static final String ALGORITHM = "Ed25519";

  private SignatureMakeHandler() {
  }

  public static PrivateKey readPrivateByPem(final Resource privateKeyResource) {
    try (final InputStream stream = privateKeyResource.getInputStream()) {
      final PEMParser pemParser = new PEMParser(new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8)));
      final Object readObject = pemParser.readObject();
      if (readObject instanceof PrivateKeyInfo) {
        PrivateKeyInfo privateKeyInfo = (PrivateKeyInfo) readObject;
        log.info("privateKeyInfo: {}", Base64.getEncoder().encodeToString(privateKeyInfo.getEncoded()));
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
        PrivateKey privateKey = converter.getPrivateKey(privateKeyInfo);
        log.info("privateKey: {}", Base64.getEncoder().encodeToString(privateKey.getEncoded()));
        return privateKey;
      } else {
        throw new IllegalArgumentException(String.format("Provided key is invalid: %s.", privateKeyResource.getFilename()));
      }
    } catch (IOException e) {
      throw new IllegalArgumentException(String.format("Provided key is invalid: %s.", privateKeyResource.getFilename()), e);
    }
  }

  // 개인 키로 메시지 서명 생성
  public static byte[] createSignature(PrivateKey privateKey, byte[] message) {
    Signature signature;
    try {
      signature = Signature.getInstance(ALGORITHM);
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
    try {
      signature.initSign(privateKey);
    } catch (InvalidKeyException e) {
      throw new RuntimeException(e);
    }
    try {
      signature.update(message);
    } catch (SignatureException e) {
      throw new RuntimeException(e);
    }
    try {
      return signature.sign();
    } catch (SignatureException e) {
      throw new RuntimeException(e);
    }
  }
}
