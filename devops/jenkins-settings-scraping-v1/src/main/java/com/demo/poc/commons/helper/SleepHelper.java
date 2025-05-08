package com.demo.poc.commons.helper;

import com.demo.poc.commons.properties.PropertiesReader;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class SleepHelper {

  private final PropertiesReader properties;

  public void basicSleep() {
    this.sleep(this.properties.get().getDelay().getBasic());
  }

  public void sleep(long delay) {
    try {
      Thread.sleep(delay);
    } catch (InterruptedException exception) {
      throw new RuntimeException(exception);
    }
  }
}