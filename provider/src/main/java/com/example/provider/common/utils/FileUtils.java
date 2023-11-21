package com.example.provider.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Slf4j
public final class FileUtils {
  private FileUtils() {
    // Utility 클래스이므로 생성자를 private으로 설정하여 인스턴스화 방지
  }

  public static void movePemToFile(String pemContent, String finalPath) throws IOException {
    Path finalFilePath = Paths.get(finalPath);

    log.info("finalFilePath : {}", finalFilePath);
    // 디렉토리가 존재하는지 확인하고, 없으면 생성합니다.
    Path directories = Files.createDirectories(finalFilePath.getParent());
    log.info("directories : {}", directories);

    // PEM 내용을 파일에 작성합니다. 파일이 없으면 생성합니다.
    Files.write(finalFilePath, pemContent.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
  }

  public static String readSystemResource(final String location) {
    try {
      Path path = Paths.get(ClassLoader.getSystemResource(location).toURI());
      return readResource(path);
    } catch (URISyntaxException e) {
      throw new RuntimeException("Error reading system resource: " + location, e);
    }
  }

  public static Resource getResource(final String location) {
    return new FileSystemResource(location);
  }

  public static Resource getClassPathResource(final String location) {
    return new ClassPathResource(location);
  }

  private static String readResource(final Path path) {
    try {
      return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new UncheckedIOException("Error reading file: " + path, e);
    }
  }
}
