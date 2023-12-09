package com.example.receiver.core.props;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
@ActiveProfiles(value = "test")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PublicKeyPathsLoadTest {

  @Autowired
  private PublicKeyPaths publicKeyPaths;

  @DisplayName("키 데이터 조회 테스트")
  @Test
  void 키_조회() {
    // given

    // when
    List<PublicKeyPaths.PublicKeyPath> pairs = publicKeyPaths.getPairs();

    // then
    assertThat(pairs).isNotEmpty().hasSize(2);
  }
}
