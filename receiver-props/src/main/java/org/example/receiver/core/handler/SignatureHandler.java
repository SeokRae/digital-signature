package org.example.receiver.core.handler;


import com.google.crypto.tink.subtle.Ed25519Verify;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.ASN1Exception;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.example.receiver.core.props.PublicKeyProperties;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.Security;
import java.util.Base64;


@Slf4j
@Component
public class SignatureHandler {
	
	private final PublicKeyProperties publicKeyManager;
	
	public SignatureHandler(PublicKeyProperties publicKeyManager) {
		Security.addProvider(new BouncyCastleProvider());
		this.publicKeyManager = publicKeyManager;
	}
	
	public boolean absent(String keyId) {
		return publicKeyManager.absent(keyId);
	}
	
	public boolean verify(String keyId, String signature, String message) {
		try {
			Ed25519Verify verifier = initializeVerifier(keyId);
			log.info("signature : {}", signature);
			log.info("payload : {}", message);
			byte[] signatureBytes = Base64.getDecoder().decode(signature);
			byte[] payload = message.getBytes(StandardCharsets.UTF_8);
			verifier.verify(signatureBytes, payload);
			return true;
		} catch (GeneralSecurityException e) {
			log.error("Exception {}", e.getMessage());
			return false;
		}
	}
	
	private Ed25519Verify initializeVerifier(String keyId) {
		try {
			String publicKey = publicKeyManager.publicKey(keyId);
			log.info("initializing verifier with the public key: {}", publicKey);
			SubjectPublicKeyInfo subjectPublicKeyInfo = processPublicKey(publicKey);
			return new Ed25519Verify(subjectPublicKeyInfo.getPublicKeyData().getBytes());
		} catch (Exception e) {
			log.error("Verifier initialization failed: " + e.getMessage(), e);
			throw new RuntimeException("Verifier initialization failed", e);
		}
	}
	
	private SubjectPublicKeyInfo processPublicKey(String publicKey) throws IOException {
		byte[] publicKeyData = Base64.getDecoder().decode(publicKey);
		try (ASN1InputStream asn1InputStream = new ASN1InputStream(publicKeyData)) {
			return SubjectPublicKeyInfo.getInstance(asn1InputStream.readObject());
		} catch (IllegalArgumentException | ASN1Exception e) {
			log.error("Error parsing public key: " + e.getMessage(), e);
			throw new RuntimeException("Public key parsing failed", e);
		}
	}
}
