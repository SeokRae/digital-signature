package org.example.receiver.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class FileSupport {
  private static final Logger log = LoggerFactory.getLogger(FileSupport.class);

  public static Resource readFileAsTestResource(String resourceName) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    String folderPath = dateFormat.format(new Date());

    String projectRootPath = new File(System.getProperty("user.dir")).getParent();

    String filePath = "20231205" + File.separator + resourceName;
    File file = new File(projectRootPath, filePath);

    log.info("{}", file.getAbsolutePath());
    if (!file.exists()) {
      log.error("File not found: {}", filePath);
      return null; // 파일이 존재하지 않는 경우
    }

    return new FileSystemResource(file);
  }
}
