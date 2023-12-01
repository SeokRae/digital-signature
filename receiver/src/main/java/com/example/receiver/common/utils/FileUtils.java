package com.example.receiver.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public final class FileUtils {
  private FileUtils() {}

  public static Resource readFileAsResource(String resourceName) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    String folderPath = dateFormat.format(new Date());

    String projectRootPath = new File(System.getProperty("user.dir")).getPath();

    String filePath = folderPath + File.separator + resourceName;
    File file = new File(projectRootPath, filePath);

    log.info("{}", file.getAbsolutePath());
    if (!file.exists()) {
      File directory = new File(file.getParent());
      if (!directory.exists()) {
        directory.mkdirs(); // 폴더 생성
      }
      try {
        file.createNewFile(); // 파일 생성
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    return new FileSystemResource(file);
  }

  public static Resource readFileAsTestResource(String resourceName) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    String folderPath = dateFormat.format(new Date());

    String projectRootPath = new File(System.getProperty("user.dir")).getParent();

    String filePath = folderPath + File.separator + resourceName;
    File file = new File(projectRootPath, filePath);

    log.info("{}", file.getAbsolutePath());
    if (!file.exists()) {
      log.error("File not found: {}", filePath);
      return null; // 파일이 존재하지 않는 경우
    }

    return new FileSystemResource(file);
  }
}
