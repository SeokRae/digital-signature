package com.example.provider.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
public final class FileUtils {
  private FileUtils() {
    // Utility 클래스이므로 생성자를 private으로 설정하여 인스턴스화 방지
  }

  public static void saveResourceToClassPath(String resourceName, String content) {
    // 현재 날짜를 기준으로 년월일 형식의 폴더 경로 생성
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    String folderPath = dateFormat.format(new Date());

    // 'resources' 폴더 내의 경로 생성
    File folder = new File(folderPath);

    if (!folder.exists()) {
      folder.mkdirs(); // 폴더가 존재하지 않으면 생성
    }

    // 파일 경로 생성
    String filePath = folderPath + File.separator + resourceName;

    try (FileWriter writer = new FileWriter(filePath)) {
      writer.write(content);
      log.info("File saved to: {}", filePath);
    } catch (IOException e) {
      log.error("IOException {}", e.getMessage());
      e.printStackTrace();
      // 예외 처리
    }
  }

  public static Resource readFileAsResource(String resourceName) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    String folderPath = dateFormat.format(new Date());

    String filePath = folderPath + File.separator + resourceName;
    File file = new File(filePath);

    if (!file.exists()) {
      log.error("File not found: {}", filePath);
      return null; // 파일이 존재하지 않는 경우
    }

    return new FileSystemResource(file);
  }
}
