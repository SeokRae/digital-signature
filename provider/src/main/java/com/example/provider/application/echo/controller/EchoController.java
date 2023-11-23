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

  private final EchoService echoService;
  @PostMapping(path = "/echo_test")
  public ResponseEntity<?> echo() {
    // 요청 Body 생성
    EchoMessage echoMessage = new EchoMessage(Instant.now().toEpochMilli());
    echoMessage.set(generateFieldName(), "unexpectedValue");

    return ResponseEntity.ok()
            .body(echoService.echoMessage(echoMessage));
  }

  private String generateFieldName() {
    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    return "unexpected_field_" + now.format(formatter);
  }
}
