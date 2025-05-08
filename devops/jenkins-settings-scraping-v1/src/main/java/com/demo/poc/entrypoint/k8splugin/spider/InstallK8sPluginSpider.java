package com.demo.poc.entrypoint.k8splugin.spider;

import com.demo.poc.commons.properties.PropertiesReader;
import com.demo.poc.commons.helper.DriverHelper;
import com.demo.poc.commons.helper.SleepHelper;
import com.demo.poc.commons.spider.JenkinsManagementSpider;
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
public class InstallK8sPluginSpider {

  private final PropertiesReader propertiesReader;
  private final DriverHelper driverHelper;
  private final JenkinsManagementSpider jenkinsManagementSpider;
  private final SleepHelper sleepHelper;

  public void searchAndInstallK8sPlugin(ChromeDriver driver) {
    Logger.startLog.accept(this.getClass().getSimpleName());
    sleepHelper.basicSleep();

    searchPlugin(driver, "Kubernetes", "kubernetes", "plugin.kubernetes.default");
    clickOnInstallPlugin(driver);
    clickOnRestartJenkins(driver);
    Logger.endLog.accept(this.getClass().getSimpleName());
  }

  private void searchPlugin(ChromeDriver driver, String pluginName, String pluginId, String label) {
    goToAvailablePlugins(driver);

    WebDriverWait wait = driverHelper.getWebDriverWait(driver);

    WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("filter-box")));
    searchBox.clear();
    searchBox.sendKeys(pluginName);

    String query = "//tr[@data-plugin-id='" + pluginId + "']//label[@for='" + label + "']";

    WebElement pluginLabel = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(query)));
    pluginLabel.click();
  }

  private void goToAvailablePlugins(ChromeDriver driver) {
    goToPlugins(driver);

    WebDriverWait wait = driverHelper.getWebDriverWait(driver);
    WebElement availablePluginsLink = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a.task-link-no-confirm[href='/manage/pluginManager/available']")));
    availablePluginsLink.click();
  }

  private void goToPlugins(ChromeDriver driver) {
    jenkinsManagementSpider.goToManageJenkins(driver);

    WebDriverWait wait = driverHelper.getWebDriverWait(driver);
    WebElement pluginsLink = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='pluginManager']")));
    pluginsLink.click();
  }

  private void clickOnInstallPlugin(ChromeDriver driver) {
    WebDriverWait wait = driverHelper.getWebDriverWait(driver);
    WebElement installButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("button-install")));
    installButton.click();
  }

  private void clickOnRestartJenkins(ChromeDriver driver) {
    WebDriverWait wait = driverHelper.getWebDriverWait(driver);

    WebElement restartLabel = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[contains(text(), 'Restart Jenkins when installation is complete and no jobs are running')]")));
    driver.executeScript("arguments[0].scrollIntoView(true);", restartLabel);

    restartLabel.click();

    sleepHelper.sleep(propertiesReader.get().getDelay().getAfterK8sPlugins());
  }
}
