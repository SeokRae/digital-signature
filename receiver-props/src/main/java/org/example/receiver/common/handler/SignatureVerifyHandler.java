package org.example.receiver.common.handler;


import com.google.crypto.tink.subtle.Ed25519Verify;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Base64;

@Slf4j
public final class SignatureVerifyHandler {
	
	private SignatureVerifyHandler() {
	}
	
	public static class SignatureVerificationBuilder {
		private final String publicKey;
		private String signature;
		private String message;
		
		public SignatureVerificationBuilder(String publicKey) {
			if (StringUtils.isBlank(publicKey)) {
				throw new IllegalArgumentException("Invalid public key file.");
			}
			this.publicKey = publicKey;
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
			log.info("initializing verifier : {}", publicKey);
			byte[] publicKeyData = Base64.getDecoder().decode(publicKey);
			try (ASN1InputStream asn1InputStream = new ASN1InputStream(publicKeyData)) {
				SubjectPublicKeyInfo subjectPublicKeyInfo = SubjectPublicKeyInfo.getInstance(asn1InputStream.readObject());
				return new Ed25519Verify(subjectPublicKeyInfo.getPublicKeyData().getBytes());
			}
		}
		
		public boolean build() {
			log.info("signature verifying");
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
