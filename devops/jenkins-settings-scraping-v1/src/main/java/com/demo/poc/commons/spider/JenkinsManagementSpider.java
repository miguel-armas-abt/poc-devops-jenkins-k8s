package com.demo.poc.commons.spider;

import com.demo.poc.commons.constants.Color;
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
public class JenkinsManagementSpider {

  private final DriverHelper driverHelper;
  private final SleepHelper sleepHelper;

  public void goToManageJenkins(ChromeDriver driver) {
    log.info(Color.PURPLE + "start {}" + Color.RESET, this.getClass().getSimpleName());
    sleepHelper.basicSleep();

    WebDriverWait wait = driverHelper.getWebDriverWait(driver);
    WebElement dashboardLink = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a.model-link")));
    dashboardLink.click();

    WebElement manageJenkinsLink = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a.task-link-no-confirm[href='/manage']")));
    manageJenkinsLink.click();
  }
}
