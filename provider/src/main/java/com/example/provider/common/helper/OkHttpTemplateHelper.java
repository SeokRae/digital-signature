package com.example.provider.common.helper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class OkHttpTemplateHelper {

  private final OkHttpClient okHttpClient;
  private final JsonHelper jsonHelper;

  public <T> T postForEntity(String url, Headers headers, String body, Class<T> responseType) {

    RequestBody requestBody = RequestBody.create(body, MediaType.parse("application/json; charset=utf-8"));

    Request request = new Request.Builder()
            .url(url)
            .headers(headers)
            .post(requestBody)
            .build();

    try (Response response = okHttpClient.newCall(request).execute()) {
      if (!response.isSuccessful()) {
        throw new IOException("Unexpected code " + response);
      }

      // Jackson을 사용하여 JSON 응답을 T 타입의 객체로 변환
      ResponseBody responseBody = response.body();
      return jsonHelper.fromJson(responseBody.string(), responseType);
    } catch (IOException e) {
      log.error("{}", e.getMessage(), e);
    }

    return null;
  }
}
