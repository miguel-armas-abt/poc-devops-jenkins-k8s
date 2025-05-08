package com.demo.poc.commons.properties;

import com.demo.poc.commons.properties.configuration.Configuration;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationProperties {

  private Configuration configuration;
}
