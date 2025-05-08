package com.demo.poc.entrypoint.k8scloud.spider;

import com.demo.poc.commons.helper.SleepHelper;
import com.demo.poc.commons.properties.PropertiesReader;
import com.demo.poc.commons.properties.configuration.Cloud;
import com.demo.poc.commons.properties.configuration.K8s;
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
public class ConfigK8sCloudSpider {

  private final PropertiesReader propertiesReader;
  private final DriverHelper driverHelper;
  private final SleepHelper sleepHelper;
  private final JenkinsManagementSpider jenkinsManagementSpider;

  public void addK8sCloud(ChromeDriver driver) {
    Logger.startLog.accept(this.getClass().getSimpleName());
    sleepHelper.basicSleep();

    fillK8sCloudName(driver);

    WebDriverWait wait = driverHelper.getWebDriverWait(driver);
    K8s k8s = propertiesReader.get().getK8s();
    Cloud cloud = k8s.getCloud();

    WebElement kubernetesUrlField = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[checkurl='/manage/descriptorByName/org.csanchez.jenkins.plugins.kubernetes.KubernetesCloud/checkServerUrl']")));
    kubernetesUrlField.sendKeys(cloud.getForwardedServerUrl());

    WebElement serverCertificateField = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("textarea[checkurl='/manage/descriptorByName/org.csanchez.jenkins.plugins.kubernetes.KubernetesCloud/checkServerCertificate']")));
    serverCertificateField.sendKeys(cloud.getCertificate());

    WebElement disableHttpsCheckLabel = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//label[span[text()='Disable https certificate check']]")));
    disableHttpsCheckLabel.click();

    WebElement credentialsDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("select[name='_.credentialsId']")));
    Select select = new Select(credentialsDropdown);
    select.selectByValue(k8s.getCredentials().getId());

    WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.jenkins-submit-button.jenkins-button--primary")));
    saveButton.click();
    Logger.endLog.accept(this.getClass().getSimpleName());
  }

  private void fillK8sCloudName(ChromeDriver driver) {
    addNewCloud(driver);

    WebDriverWait wait = driverHelper.getWebDriverWait(driver);
    Cloud cloud = propertiesReader.get().getK8s().getCloud();

    WebElement cloudNameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name")));
    cloudNameField.sendKeys(cloud.getName());

    WebElement kubernetesLabel = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("label[for='org.csanchez.jenkins.plugins.kubernetes.KubernetesCloud']")));
    kubernetesLabel.click();

    WebElement createButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.jenkins-button")));
    createButton.click();
  }

  private void addNewCloud(ChromeDriver driver) {
    goToClouds(driver);

    WebDriverWait wait = driverHelper.getWebDriverWait(driver);

    WebElement newCloudButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a.content-block__link[href='new']")));
    newCloudButton.click();
  }

  private void goToClouds(ChromeDriver driver) {
    jenkinsManagementSpider.goToManageJenkins(driver);

    WebDriverWait wait = driverHelper.getWebDriverWait(driver);
    WebElement credentialsLink = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='cloud']")));
    credentialsLink.click();
  }

}
