package com.example.receiver.common.utils;

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

@Slf4j
public final class FileUtils {
  private FileUtils() {}

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
