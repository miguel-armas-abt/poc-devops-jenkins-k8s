package com.demo.poc.entrypoint.unlock.spider;

import com.demo.poc.commons.logging.Logger;
import com.demo.poc.entrypoint.login.spider.LoginSpider;
import com.demo.poc.commons.properties.PropertiesReader;
import com.demo.poc.commons.properties.configuration.Configuration;
import com.demo.poc.commons.helper.DriverHelper;
import com.demo.poc.commons.helper.SleepHelper;
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
public class SuggestedPluginSpider {

  private static final String INSTALL_PLUGINS_BTN = "a.btn.btn-primary.btn-lg.btn-huge.install-recommended";
  private static final String IFRAME_SETUP_FIRST_USER = "setup-first-user";
  private static final String IFRAME_CONFIGURE_INSTANCE = "setup-configure-instance";

  private final PropertiesReader propertiesReader;
  private final DriverHelper driverHelper;
  private final LoginSpider loginSpider;
  private final SleepHelper sleepHelper;

  public void customizeJenkins(ChromeDriver driver) {
    Logger.startLog.accept(this.getClass().getSimpleName());
    sleepHelper.basicSleep();

    loginSpider.doLoginIfScreenIsPresent(driver);
    installPlugins(driver);
    createAdminUser(driver);
    uriConfig(driver);
    startJenkins(driver);
    Logger.endLog.accept(this.getClass().getSimpleName());
  }

  private void installPlugins(ChromeDriver driver) {
    if(isInstallPluginsScreenPresent(driver)) {
      WebDriverWait wait = driverHelper.getWebDriverWait(driver);

      WebElement installButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(INSTALL_PLUGINS_BTN)));
      installButton.click();

      waitProgressBar(driver);

    } else {
      log.warn("Plugins installation screen not found.");
    }
  }

  private void waitProgressBar(ChromeDriver driver) {
    WebDriverWait wait = driverHelper.getWebDriverWait(driver);
    Configuration properties = propertiesReader.get();

    WebElement progressBarContainer = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".progress")));
    int containerWidth = progressBarContainer.getSize().getWidth();

    WebElement progressBar = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".progress-bar")));
    boolean isProgressComplete = false;

    while (!isProgressComplete) {
      String widthValue = progressBar.getCssValue("width").replace("px", "");
      double progressWidth = Double.parseDouble(widthValue);
      double progressPercentage = (progressWidth / containerWidth) * 100;

      if (progressPercentage > 99) {
        isProgressComplete = true;
        sleepHelper.sleep(properties.getDelay().getAfterSuggestedPlugins());
      } else {
        sleepHelper.basicSleep();
      }
    }
  }

  private void createAdminUser(ChromeDriver driver) {
    if(isCreateAdminUserScreenPresent(driver)) {
      WebDriverWait wait = driverHelper.getWebDriverWait(driver);
      Configuration properties = propertiesReader.get();

      String username = properties.getLogin().getUsername();
      String password = properties.getLogin().getPassword();
      String email = properties.getLogin().getEmail();

      //go to iframe
      wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id(IFRAME_SETUP_FIRST_USER)));

      WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
      usernameField.sendKeys(username);

      WebElement passwordField = driver.findElement(By.name("password1"));
      passwordField.sendKeys(password);

      WebElement confirmPasswordField = driver.findElement(By.name("password2"));
      confirmPasswordField.sendKeys(password);

      WebElement fullNameField = driver.findElement(By.name("fullname"));
      fullNameField.sendKeys(username);

      WebElement emailField = driver.findElement(By.name("email"));
      emailField.sendKeys(email);

      //exit iframe
      driver.switchTo().defaultContent();

      WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.btn.btn-primary.save-first-user")));
      saveButton.click();

    } else {
      log.warn("Admin user creation screen not detected.");
    }
  }

  private void uriConfig(ChromeDriver driver) {
    if (isURIConfigScreenPresent(driver)) {
      WebDriverWait wait = driverHelper.getWebDriverWait(driver);
      wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id(IFRAME_CONFIGURE_INSTANCE)));

      //exit iframe
      driver.switchTo().defaultContent();

      WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.btn.btn-primary.save-configure-instance")));
      saveButton.click();

    } else {
      log.warn("URI config screen not detected.");
    }
  }

  private void startJenkins(ChromeDriver driver) {
    if (isStartingScreenPresent(driver)) {
      WebDriverWait wait = driverHelper.getWebDriverWait(driver);
      WebElement startButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.btn.btn-primary.install-done")));
      startButton.click();

    } else {
      log.warn("Starting Jenkins screen not detected.");
    }
  }

  private boolean isInstallPluginsScreenPresent(ChromeDriver driver) {
    WebDriverWait wait = driverHelper.getWebDriverWait(driver);
    try {
      wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(INSTALL_PLUGINS_BTN)));
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  private boolean isCreateAdminUserScreenPresent(ChromeDriver driver) {
    WebDriverWait wait = driverHelper.getWebDriverWait(driver);
    try {
      wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id(IFRAME_SETUP_FIRST_USER)));
      //exit iframe
      driver.switchTo().defaultContent();
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  private boolean isURIConfigScreenPresent(ChromeDriver driver) {
    WebDriverWait wait = driverHelper.getWebDriverWait(driver);
    try {
      wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id(IFRAME_CONFIGURE_INSTANCE)));
      //exit iframe
      driver.switchTo().defaultContent();
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  private boolean isStartingScreenPresent(ChromeDriver driver) {
    WebDriverWait wait = driverHelper.getWebDriverWait(driver);
    try {
      wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[text()='Jenkins is ready!']")));
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
