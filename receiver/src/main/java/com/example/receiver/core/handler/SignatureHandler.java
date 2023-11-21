package com.example.receiver.core.handler;


import com.example.receiver.common.utils.FileUtils;
import com.example.receiver.core.props.PublicKeyPath;
import com.google.crypto.tink.subtle.Ed25519Verify;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.PublicKey;
import java.security.Security;
import java.util.Base64;


@Slf4j
@Component
public class SignatureHandler {

  private final Resource publicKeyResource;

  public SignatureHandler(PublicKeyPath publicKeyPath) {
    Security.addProvider(new BouncyCastleProvider());
    this.publicKeyResource = FileUtils.getClassPathResource(publicKeyPath.getPath());
    this.initializeVerifier();
  }

  public boolean verify(String signature, String message) {
    try {
      Ed25519Verify verifier = initializeVerifier();
      log.info("signature : {}", signature);
      log.info("message : {}", message);
      byte[] signatureBytes = Base64.getDecoder().decode(signature);
      byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
      verifier.verify(signatureBytes, messageBytes);
      return true;
    } catch (GeneralSecurityException e) {
      log.error("Exception {}", e.getMessage());
      return false;
    }
  }

  private Ed25519Verify initializeVerifier() {
    try (final InputStream stream = publicKeyResource.getInputStream();
         final PEMParser pemParser = new PEMParser(new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8)))) {
      final Object readObject = pemParser.readObject();

      if (readObject instanceof SubjectPublicKeyInfo) {
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
        PublicKey publicKey = converter.getPublicKey((SubjectPublicKeyInfo) readObject);
        log.info("publicKey : {}", Base64.getEncoder().encodeToString(publicKey.getEncoded()));
        SubjectPublicKeyInfo subjectPublicKeyInfo = SubjectPublicKeyInfo.getInstance(ASN1Primitive.fromByteArray(publicKey.getEncoded()));
        return new Ed25519Verify(subjectPublicKeyInfo.getPublicKeyData().getBytes());
      } else {
        throw new IllegalArgumentException("Invalid public key format.");
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
