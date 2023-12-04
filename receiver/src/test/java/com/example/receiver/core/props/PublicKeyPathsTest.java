package com.example.receiver.core.props;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PublicKeyPathsTest {

  @DisplayName("PublicKeyPaths 객체 생성 테스트")
  @Test
  void testCase1() {
    // given
    List<PublicKeyPaths.PublicKeyPath> keys = new ArrayList<>();
    keys.add(new PublicKeyPaths.PublicKeyPath("20231123", "20231123/publicKey.pem"));

    // when
    PublicKeyPaths publicKeyPaths = new PublicKeyPaths(keys);

    // then
    int actual = publicKeyPaths.getPairs().size();
    assertThat(actual).isOne();
  }
}
