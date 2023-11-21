package com.example.provider.common.helper;


import com.google.crypto.tink.subtle.Ed25519Verify;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.openssl.PEMException;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.util.io.pem.PemObject;
import org.springframework.core.io.Resource;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

@Slf4j
public final class SignatureHelper {

  public static final String ALGORITHM = "Ed25519";
  public static final String PRIVATE_KEY = "PRIVATE KEY";

  private SignatureHelper() {
  }

  // 개인 키를 PEM 형식으로 변환
  public static String convertPrivateKeyToPem(PrivateKey privateKey) {
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

  // 공개 키를 PEM 형식으로 변환
  public static String convertPublicKeyToPem(PublicKey publicKey) {
    log.info("publicKey.getEncoded().length : {}", publicKey.getEncoded().length);
    StringWriter stringWriter = new StringWriter();
    try (JcaPEMWriter pemWriter = new JcaPEMWriter(stringWriter)) {
      pemWriter.writeObject(publicKey);
    } catch (IOException e) {
      log.error("IOException {}", e.getMessage());
    }
    return stringWriter.toString();
  }

  // 개인 키로 메시지 서명 생성
  public static byte[] createSignature(PrivateKey privateKey, byte[] message) {
    try {
      Signature signature = Signature.getInstance(ALGORITHM);
      signature.initSign(privateKey);
      signature.update(message);
      byte[] sign = signature.sign();
      log.info("sign.data : {}, sign.length : {}", new String(sign, StandardCharsets.UTF_8), sign.length);
      return sign;
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

  // 공개 키로 서명 검증하는 메서드 (기존의 createEd25519Verify 메서드 사용)
  public static boolean verifySignature(String publicKeyPem, byte[] message, byte[] signature) {
    log.info("signature.length : {}, encodeToString.length() : {}, encodeToString.data() : {} ", signature.length, Base64.getEncoder().encodeToString(signature).length(), Base64.getEncoder().encodeToString(signature));
    PEMParser pemParser = new PEMParser(new StringReader(publicKeyPem));

    try {
      SubjectPublicKeyInfo publicKeyInfo = (SubjectPublicKeyInfo) pemParser.readObject();
      JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
      PublicKey publicKey = converter.getPublicKey(publicKeyInfo);
      Signature sig = Signature.getInstance(ALGORITHM);
      sig.initVerify(publicKey);
      sig.update(message);
      return sig.verify(signature);
    } catch (PEMException e) {
      log.error("PEMException {}", e.getMessage());
    } catch (NoSuchAlgorithmException e) {
      log.error("NoSuchAlgorithmException {}", e.getMessage());
    } catch (SignatureException e) {
      log.error("SignatureException {}", e.getMessage());
    } catch (InvalidKeyException e) {
      log.error("InvalidKeyException {}", e.getMessage());
    } catch (IOException e) {
      log.error("IOException {}", e.getMessage());
    }
    return false;
  }

  public static boolean verifyEd25519Signature(String publicKeyPem, byte[] message, byte[] signature) {
    PEMParser pemParser = new PEMParser(new StringReader(publicKeyPem));

    try {
      SubjectPublicKeyInfo publicKeyInfo = (SubjectPublicKeyInfo) pemParser.readObject();
      JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
      PublicKey publicKey = converter.getPublicKey(publicKeyInfo);
      SubjectPublicKeyInfo subjectPublicKeyInfo = SubjectPublicKeyInfo.getInstance(ASN1Primitive.fromByteArray(publicKey.getEncoded()));
      Ed25519Verify ed25519Verify = new Ed25519Verify(subjectPublicKeyInfo.getPublicKeyData().getBytes());
      log.info("signature.length : {}", signature.length);
      ed25519Verify.verify(signature, message);
      return true;
    } catch (PEMException e) {
      log.error("PEMException {}", e.getMessage());
    } catch (NoSuchAlgorithmException e) {
      log.error("NoSuchAlgorithmException {}", e.getMessage());
    } catch (SignatureException e) {
      log.error("SignatureException {}", e.getMessage());
    } catch (InvalidKeyException e) {
      log.error("InvalidKeyException {}", e.getMessage());
    } catch (IOException e) {
      log.error("IOException {}", e.getMessage());
    } catch (GeneralSecurityException e) {
      log.error("GeneralSecurityException {}", e.getMessage());
    }
    return false;
  }

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

  // 공개 키 리소스 가져오기: 함수는 Resource 타입의 publicKeyResource 매개변수를 받습니다. 이는 공개 키를 포함하고 있는 리소스(파일, URL 등)를 나타냅니다.
  public static Ed25519Verify createEd25519Verify(final Resource publicKeyResource) {
    try (final InputStream stream = publicKeyResource.getInputStream()) {
      // PEM 파일 파싱: 공개 키는 PEM 형식으로 저장되어 있다고 가정하며, 이를 읽기 위해 PEMParser를 사용합니다. PEM 형식은 공개 키를 인코딩하는 표준 형식 중 하나입니다.
      final PEMParser pemParser = new PEMParser(new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8)));
      final Object readObject = pemParser.readObject();
      if (readObject instanceof SubjectPublicKeyInfo) {
        SubjectPublicKeyInfo publicKeyInfo = (SubjectPublicKeyInfo) readObject;
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
        PublicKey publicKey = converter.getPublicKey(publicKeyInfo);
        SubjectPublicKeyInfo subjectPublicKeyInfo = SubjectPublicKeyInfo.getInstance(ASN1Primitive.fromByteArray(publicKey.getEncoded()));
        return new Ed25519Verify(subjectPublicKeyInfo.getPublicKeyData().getBytes());
      } else {
        throw new IllegalArgumentException(String.format("Provided key is invalid: %s.", publicKeyResource.getFilename()));
      }
    } catch (IOException e) {
      throw new IllegalArgumentException(String.format("Provided key is invalid: %s.", publicKeyResource.getFilename()), e);
    }
  }

  public static boolean verifySignature(Ed25519Verify verifier, byte[] data, byte[] signature) {
    try {
      verifier.verify(data, signature);
      return true; // 서명이 유효함
    } catch (SignatureException e) {
      return false; // 서명이 유효하지 않음
    } catch (GeneralSecurityException e) {
      throw new RuntimeException(e);
    }
  }
}
