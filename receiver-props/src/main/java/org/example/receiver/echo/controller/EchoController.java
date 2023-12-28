package org.example.receiver.echo.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.receiver.echo.domain.EchoMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(path = "/v1")
public class EchoController {

  @PostMapping(path = "/echo_test")
  public ResponseEntity<EchoMessage> echo(@RequestBody EchoMessage echoMessage) {
    log.info("echoMessage: {}", echoMessage);
    return ResponseEntity
            .ok()
            .body(echoMessage);
  }

}
