package com.example.provider.application.echo.controller;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.ToString;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 에코 테스트용 DTO
 */
@Getter
@ToString
public class EchoMessage {

  private long timestamp;
  private String keyId;
  /**
   * 예상되지 않은 필드를 저장하기 위한 Map
   */
  @JsonIgnore
  private final Map<String, Object> additionalProperties = new LinkedHashMap<>();

  public EchoMessage() {
  }

  public EchoMessage(long timestamp) {
    this(timestamp, new SimpleDateFormat("yyyyMMdd").format(new Date()));
  }

  public EchoMessage(long timestamp, String keyId) {
    this.timestamp = timestamp;
    this.keyId = keyId;
  }

  @JsonAnyGetter
  public Map<String, Object> any() {
    return additionalProperties;
  }

  @JsonAnySetter
  public void set(String name, Object value) {
    additionalProperties.put(name, value);
  }
}
