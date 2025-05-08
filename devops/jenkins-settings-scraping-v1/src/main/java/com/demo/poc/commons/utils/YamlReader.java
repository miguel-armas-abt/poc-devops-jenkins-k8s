package com.demo.poc.commons.utils;

import java.io.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class YamlReader {

  public static <T> T read(String filePath, Class<T> modelClass) {
    try (InputStream inputStream = new FileInputStream(filePath)) {
      return initYaml().loadAs(inputStream, modelClass);
    } catch (IOException exception) {
      throw new RuntimeException("Error reading file: " + filePath, exception);
    }
  }

  private static Yaml initYaml() {
    DumperOptions options = new DumperOptions();
    options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

    return new Yaml(options);
  }
}
