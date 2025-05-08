package com.demo.poc.entrypoint.unlock.spider;

import com.demo.poc.commons.properties.PropertiesReader;
import com.demo.poc.commons.helper.SleepHelper;
import com.demo.poc.commons.logging.Logger;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class UnlockSpider {

  private static final String PASSWORD_FIELD = "security-token";

  private final PropertiesReader propertiesReader;
  private final SleepHelper sleepHelper;

  public void unlock(ChromeDriver driver) {
    Logger.startLog.accept(this.getClass().getSimpleName());
    sleepHelper.basicSleep();

    String unlockPassword = propertiesReader.get().getLogin().getUnlockPassword();
    WebElement passwordField = driver.findElement(By.id(PASSWORD_FIELD));
    passwordField.sendKeys(unlockPassword);

    WebElement continueButton = driver.findElement(By.cssSelector("input.btn.btn-primary.set-security-key"));
    continueButton.click();
    Logger.endLog.accept(this.getClass().getSimpleName());
  }
}
