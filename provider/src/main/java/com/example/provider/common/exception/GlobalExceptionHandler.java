package com.example.provider.common.exception;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(value = {HttpClientErrorException.class})
  public Errors handleException(Exception e) {
    log.warn("{}", e.getMessage());
    return new Errors(HttpStatus.BAD_REQUEST.value(), e.getMessage());
  }

  @Getter
  public static class Errors {
    private final int code;
    private final String message;

    public Errors(int code, String message) {
      this.code = code;
      this.message = message;
    }
  }
}
