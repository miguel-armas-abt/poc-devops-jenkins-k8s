package com.demo.poc.entrypoint.k8scloud.service;

import com.demo.poc.commons.enums.OperationType;
import com.demo.poc.commons.helper.DriverHelper;
import com.demo.poc.commons.service.OperationService;
import com.demo.poc.commons.logging.Logger;
import com.demo.poc.entrypoint.k8scloud.spider.ConfigK8sCloudSpider;
import com.demo.poc.entrypoint.login.spider.LoginSpider;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.chrome.ChromeDriver;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class ConfigK8sCloudService implements OperationService {

  private final OperationType OPERATION = OperationType.CONFIG_K8S_CLOUD;

  private final DriverHelper driverHelper;
  private final LoginSpider loginSpider;
  private final ConfigK8sCloudSpider configK8sCloudSpider;

  @Override
  public void execute() {
    Logger.operationLog.accept(OPERATION);
    ChromeDriver driver = driverHelper.openBrowser();
    loginSpider.doLoginIfScreenIsPresent(driver);
    configK8sCloudSpider.addK8sCloud(driver);
    driver.quit();
  }

  @Override
  public boolean supports(OperationType operation) {
    return OPERATION.equals(operation);
  }
}