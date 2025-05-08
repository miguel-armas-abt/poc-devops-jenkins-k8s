package com.demo.poc.commons.properties;

import com.demo.poc.commons.properties.configuration.Configuration;
import com.demo.poc.commons.utils.YamlReader;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class PropertiesReader {

  private final Configuration configuration;

  public PropertiesReader() {
    ApplicationProperties properties = YamlReader.read("application.yaml", ApplicationProperties.class);
    this.configuration = properties.getConfiguration();
  }

  public Configuration get() {
    return this.configuration;
  }

}
