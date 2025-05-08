package com.demo.poc.commons.helper;

import com.demo.poc.commons.properties.configuration.Configuration;
import com.google.inject.Inject;
import com.demo.poc.commons.properties.PropertiesReader;
import java.time.Duration;

import lombok.RequiredArgsConstructor;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class DriverHelper {

  private final PropertiesReader properties;
  private final SleepHelper service;

  public ChromeDriver openBrowser() {
    ChromeDriver driver = this.configureNewDriverAndGet();
    Configuration properties = this.properties.get();
    driver.get(properties.getLogin().getUri());
    service.sleep(properties.getDelay().getOpenBrowser());
    return driver;
  }

  private ChromeDriver configureNewDriverAndGet() {
    System.setProperty("webdriver.chrome.driver", properties.get().getDriverPath());
    ChromeOptions options = new ChromeOptions();
    options.addArguments("--remote-allow-origins=*");
    return new ChromeDriver(options);
  }

  public WebDriverWait getWebDriverWait(ChromeDriver driver) {
    long delay = properties.get().getDelay().getBasic();
    return new WebDriverWait(driver, Duration.ofMillis(delay));
  }
}
