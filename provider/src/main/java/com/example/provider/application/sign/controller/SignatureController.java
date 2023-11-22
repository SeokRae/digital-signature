package com.example.provider.application.sign.controller;

import com.example.provider.common.helper.SignatureMakeHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;


@Slf4j
@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class SignatureController {

  private final SignatureMakeHelper signatureMakeHelper;

  /**
   * privateKey, publicKey 생성
   *
   * @return
   */
  @PostMapping("/sign")
  public ResponseEntity<?> sign() {

    signatureMakeHelper.generateKeyPair();

    return ResponseEntity.ok().build();
  }
}
