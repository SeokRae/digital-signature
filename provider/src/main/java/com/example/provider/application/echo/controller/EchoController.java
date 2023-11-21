package com.example.provider.application.echo.controller;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

  private final ExternalUrlProperties externalUrlProperties;
  private final RestTemplateHelper restTemplateHelper;
  private final JsonHelper jsonHelper;
  
  @PostMapping(path = "/echo_test")
  public ResponseEntity<?> echo() {
    // 요청 Body 생성
    EchoMessage echoMessage = new EchoMessage(Instant.now().toEpochMilli());
    echoMessage.set(generateFieldName(), "unexpectedValue");
    String requestBody = jsonHelper.toJson(echoMessage);

    // 요청 Body에 대한 Signature 생성
    PrivateKey privateKey = SignatureHelper.readPrivateByPem(FileUtils.getClassPathResource("privateKey.pem"));

    byte[] payload = requestBody.getBytes(StandardCharsets.UTF_8);
    byte[] signature = SignatureHelper.createSignature(privateKey, payload);

    // 요청 Header 생성
    MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
    header.add(HttpHeaders.CONTENT_TYPE, "application/json");
    String value = Base64.getEncoder().encodeToString(signature);
    log.info("signature: {}", value);
    header.add("signature", value);
    header.add(HttpHeaders.ACCEPT, "application/json");
    header.add(HttpHeaders.CONTENT_ENCODING, "UTF-8");

    ResponseEntity<EchoMessage> echoResponse = restTemplateHelper.postRestTemplate(
            externalUrlProperties.getEcho(),
            header,
            requestBody,
            EchoMessage.class
    );
    if(!echoResponse.getStatusCode().is2xxSuccessful()) {
      log.error("Echo API 호출 실패");
      throw new RuntimeException("Echo API 호출 실패");
    }
    log.info("Echo API 호출 성공");
    return ResponseEntity.status(echoResponse.getStatusCode()).body(echoResponse.getBody());
  }

  public String generateFieldName() {
    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    return "unexpected_field_" + now.format(formatter);
  }
}
