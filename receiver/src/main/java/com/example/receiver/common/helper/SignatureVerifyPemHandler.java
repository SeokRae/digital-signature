package com.example.receiver.common.helper;


import com.google.crypto.tink.subtle.Ed25519Verify;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.PublicKey;
import java.util.Base64;

@Slf4j
public final class SignatureVerifyPemHandler {

  private SignatureVerifyPemHandler() {
  }

  public static class SignatureVerificationBuilder {
    private final Resource publicKeyResource;
    private String signature;
    private String message;

    public SignatureVerificationBuilder(Resource publicKeyResource) {
      if (!publicKeyResource.exists()) {
        throw new IllegalArgumentException("Invalid public key file.");
      }
      this.publicKeyResource = publicKeyResource;
    }

    public SignatureVerificationBuilder withSignature(String signature) {
      if (StringUtils.isBlank(signature)) {
        throw new IllegalArgumentException("Invalid signature.");
      }
      this.signature = signature;
      return this;
    }

    public SignatureVerificationBuilder withMessage(String message) {
      if (StringUtils.isBlank(message)) {
        throw new IllegalArgumentException("Invalid message.");
      }
      this.message = message;
      return this;
    }

    private Ed25519Verify initializeVerifier() throws IOException {
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
      }
    }

    public boolean build() {
      try {
        Ed25519Verify verifier = initializeVerifier();
        log.info("signature : {}", signature);
        log.info("message : {}", message);
        byte[] signatureBytes = Base64.getDecoder().decode(signature);
        byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
        verifier.verify(signatureBytes, messageBytes);
        return true;
      } catch (IOException | GeneralSecurityException e) {
        log.error("Exception {}", e.getMessage());
        return false;
      }
    }
  }
}
