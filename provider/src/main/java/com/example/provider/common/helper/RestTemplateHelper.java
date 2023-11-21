package com.example.provider.common.helper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class RestTemplateHelper {
  private final RestTemplate restTemplate;
  
  public <T> ResponseEntity<T> postRestTemplate(
          String url, MultiValueMap<String, String> header, String body, Class<T> responseType
  ) {

    // body
    HttpEntity<String> requestEntity = new HttpEntity<>(body, header);

    log.info("url: {}, header: {}, contents: {}", url, header, body);
    return restTemplate.exchange(url, HttpMethod.POST, requestEntity, responseType);
  }

}
