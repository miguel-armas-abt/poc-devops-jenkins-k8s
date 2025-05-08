package com.demo.poc.entrypoint.k8scredential.spider;

import com.demo.poc.commons.helper.SleepHelper;
import com.demo.poc.commons.properties.PropertiesReader;
import com.demo.poc.commons.properties.configuration.Credentials;
import com.demo.poc.commons.helper.DriverHelper;
import com.demo.poc.commons.spider.JenkinsManagementSpider;
import com.demo.poc.commons.logging.Logger;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class AddK8sCredentialSpider {

  private final PropertiesReader propertiesReader;
  private final DriverHelper driverHelper;
  private final JenkinsManagementSpider jenkinsManagementSpider;
  private final SleepHelper sleepHelper;

  public void addK8sCredentials(ChromeDriver driver) {
    Logger.startLog.accept(this.getClass().getSimpleName());
    sleepHelper.basicSleep();

    goToGlobalCredentials(driver);

    WebDriverWait wait = driverHelper.getWebDriverWait(driver);
    Credentials credentials = propertiesReader.get().getK8s().getCredentials();

    WebElement kindDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("select.jenkins-select__input")));
    Select select = new Select(kindDropdown);
    select.selectByVisibleText(credentials.getKind());

    WebElement secretField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("_.secret")));
    secretField.sendKeys(credentials.getK8sAuthToken());

    WebElement idField = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[checkurl='/manage/descriptorByName/org.jenkinsci.plugins.plaincredentials.impl.StringCredentialsImpl/checkId']")));
    idField.sendKeys(credentials.getId());

    WebElement createButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.jenkins-submit-button")));
    createButton.click();

    WebElement dashboardLink = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a.model-link[href='/']")));
    dashboardLink.click();
    Logger.endLog.accept(this.getClass().getSimpleName());
  }

  private void goToGlobalCredentials(ChromeDriver driver) {
    goToCredentials(driver);

    WebDriverWait wait = driverHelper.getWebDriverWait(driver);
    WebElement globalLink = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='/manage/credentials/store/system/domain/_/']")));
    globalLink.click();

    WebElement addCredentialsLink = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='/manage/credentials/store/system/domain/_/newCredentials']")));
    addCredentialsLink.click();
  }

  private void goToCredentials(ChromeDriver driver) {
    jenkinsManagementSpider.goToManageJenkins(driver);

    WebDriverWait wait = driverHelper.getWebDriverWait(driver);
    WebElement credentialsLink = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='credentials']")));
    credentialsLink.click();
  }

}
