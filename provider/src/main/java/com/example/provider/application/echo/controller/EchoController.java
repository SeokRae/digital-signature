package com.example.provider.application.echo.controller;

import com.example.provider.application.echo.service.EchoService;
import com.example.provider.common.helper.JsonHelper;
import com.example.provider.common.helper.RestTemplateHelper;
import com.example.provider.common.helper.SignatureHelper;
import com.example.provider.common.utils.FileUtils;
import com.example.provider.core.props.ExternalUrlProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

@Slf4j
@RestController
@RequestMapping(path = "/v1")
@RequiredArgsConstructor
public class EchoController {

  private final EchoService echoService;

  /**
   * curl --request POST --url http://localhost:8081/v1/echo_test --header 'Content-Type: application/x-www-form-urlencoded' --data keyId=20231204
   * @param keyId 키 아이디로 관리를 검증하기 위한 값
   * @return
   */
  @PostMapping(path = "/echo_test")
  public ResponseEntity<EchoMessage> echo(@RequestParam(required = false) String keyId) {
    // 요청 Body 생성
    EchoMessage echoMessage = new EchoMessage(Instant.now().toEpochMilli(), keyId);
    echoMessage.set(generateFieldName(), "unexpectedValue");

    EchoMessage body = echoService.echoMessage(echoMessage);
    return ResponseEntity.ok()
            .body(body);
  }

  private String generateFieldName() {
    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    return "unexpected_field_" + now.format(formatter);
  }
}
