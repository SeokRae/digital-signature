package org.example.receiver.core.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.example.receiver.common.handler.SignatureVerifyHandler;
import org.example.receiver.echo.domain.EchoMessage;
import org.example.receiver.support.FileSupport;
import org.junit.jupiter.api.*;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ActiveProfiles;

import java.security.PrivateKey;
import java.security.Security;
import java.time.Instant;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DisplayName("signature make and verify test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SignatureVerifyHandlerTest {
	private static final String PRIVATE_KEY_PEM = "privateKey.pem";
	private static final String PUBLIC_KEY_ID = "20231205";
	private ObjectMapper objectMapper;
	private String signatureHeader;
	private String message;
	
	@BeforeEach
	void setUp() {
		objectMapper = new ObjectMapper();
		Security.addProvider(new BouncyCastleProvider());
	}
	
	@Order(1)
	@Test
	void fileExistsTest() {
		
		Resource resource = FileSupport.readFileAsTestResource(PRIVATE_KEY_PEM);
		assertThat(resource.exists()).isTrue();
		
	}
	
	@Order(2)
	@Test
	void makeSignature() throws JsonProcessingException {
		// given
		Resource resource = FileSupport.readFileAsTestResource(PRIVATE_KEY_PEM);
		PrivateKey privateKey = SignatureMakeHandler.readPrivateByPem(resource);
		
		EchoMessage echoMessage = new EchoMessage(Instant.now().toEpochMilli());
		
		// when
		message = objectMapper.writeValueAsString(echoMessage);
		byte[] signature = SignatureMakeHandler.createSignature(privateKey, objectMapper.writeValueAsBytes(echoMessage));
		signatureHeader = Base64.getEncoder().encodeToString(signature);
		
		// then
		assertThat(signatureHeader).isNotBlank();
	}
	
	@Order(3)
	@Test
	void verify() {
		// given
		String publicKey = "MCowBQYDK2VwAyEAwygQG0l57EGoUUjnHZtj73RMuLc132V4Ew8Ht6+m36w=";;
		// when
		boolean verifiedSignature = new SignatureVerifyHandler.SignatureVerificationBuilder(publicKey)
						.withSignature(signatureHeader)
						.withMessage(message)
						.build();
		
		// then
		assertThat(verifiedSignature).isTrue();
	}
}
