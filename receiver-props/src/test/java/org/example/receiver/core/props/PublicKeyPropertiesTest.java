package org.example.receiver.core.props;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@SpringBootTest
class PublicKeyPropertiesTest {
	
	@Autowired
	private PublicKeyProperties publicKeyProperties;
	
	@DisplayName("public Key 리스트 조회 테스트")
	@Test
	void testCase1() {
		
		List<PublicKeyProperties.PublicKeyPath> pairs = publicKeyProperties.getPairs();
		
		assertThat(pairs).hasSize(3)
						.extracting("id", "publicKey")
						.containsExactly(
										tuple("20231123", "MCowBQYDK2VwAyEAS4iMOksLzDijLB+NGY5KGwJjnPxeP6XcZZ9kJbu/PGk="),
										tuple("20231204", "MCowBQYDK2VwAyEADRBmgSsv2hZzDQ6EfQR1pyXZLSzzo0RRxnWdMilZpBU="),
										tuple("20231205", "MCowBQYDK2VwAyEAwygQG0l57EGoUUjnHZtj73RMuLc132V4Ew8Ht6+m36w=")
						);
		
	}
}