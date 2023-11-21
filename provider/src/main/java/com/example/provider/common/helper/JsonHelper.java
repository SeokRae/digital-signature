package com.example.provider.common.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SNAKE_CASE;

@Slf4j
@Component
@RequiredArgsConstructor
public class JsonHelper {

  private final ObjectMapper mapper;

  public String toJson(Object object) {
    try {
      return mapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Failed to convert object to JSON string", e);
    }
  }

  public <T> T fromJson(String json, Class<T> clazz) {
    try {
      ObjectMapper localMapper = mapper.copy();
      localMapper.setPropertyNamingStrategy(SNAKE_CASE);
      return localMapper.readValue(json, clazz);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Failed to convert JSON string to object", e);
    }
  }
}
