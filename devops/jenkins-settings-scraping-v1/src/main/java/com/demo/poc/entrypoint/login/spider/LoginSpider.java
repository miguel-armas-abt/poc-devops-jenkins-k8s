package com.demo.poc.entrypoint.login.spider;

import com.demo.poc.commons.properties.PropertiesReader;
import com.demo.poc.commons.properties.configuration.Configuration;
import com.demo.poc.commons.helper.DriverHelper;
import com.demo.poc.commons.helper.SleepHelper;
import com.demo.poc.commons.logging.Logger;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class LoginSpider {

  private final PropertiesReader propertiesReader;
  private final DriverHelper driverHelper;
  private final SleepHelper sleepHelper;

  public void doLoginIfScreenIsNotPresent(ChromeDriver driver) {
    if (!isLoginScreenPresent(driver)) {
      login(driverHelper.openBrowser());
    }
  }

  public void doLoginIfScreenIsPresent(ChromeDriver driver) {
    if(isLoginScreenPresent(driver))
      login(driver);
  }

  private void login(ChromeDriver driver) {
    Logger.startLog.accept(this.getClass().getSimpleName());
    sleepHelper.basicSleep();

    WebDriverWait wait = driverHelper.getWebDriverWait(driver);
    Configuration properties = propertiesReader.get();

    String username = properties.getLogin().getUsername();
    String password = properties.getLogin().getPassword();

    WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("j_username")));
    usernameField.sendKeys(username);

    WebElement passwordField = driver.findElement(By.id("j_password"));
    passwordField.sendKeys(password);

    WebElement signInButton = driver.findElement(By.cssSelector("button.jenkins-button.jenkins-button--primary"));
    signInButton.click();
    Logger.endLog.accept(this.getClass().getSimpleName());
  }

  public boolean isLoginScreenPresent(ChromeDriver driver) {
    WebDriverWait wait = driverHelper.getWebDriverWait(driver);
    try {
      wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[text()='Sign in to Jenkins']")));
      return true;
    } catch (Exception e) {
      log.warn("login screen isn't present");
      return false;
    }
  }
}
