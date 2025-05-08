package com.demo.poc.commons.properties.configuration;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Configuration {

  private String driverPath;
  private Login login;
  private K8s k8s;
  private Delay delay;
  private Pipeline newPipeline;
}
