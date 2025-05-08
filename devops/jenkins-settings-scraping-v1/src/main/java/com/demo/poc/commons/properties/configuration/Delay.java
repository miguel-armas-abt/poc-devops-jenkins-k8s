package com.demo.poc.commons.properties.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Delay {

  private long basic;
  private long openBrowser;
  private long afterSuggestedPlugins;
  private long afterK8sPlugins;
}
