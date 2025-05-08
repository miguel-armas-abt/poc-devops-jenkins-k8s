package com.demo.poc.entrypoint.k8splugin.service;

import com.demo.poc.commons.enums.OperationType;
import com.demo.poc.commons.helper.DriverHelper;
import com.demo.poc.commons.service.OperationService;
import com.demo.poc.commons.logging.Logger;
import com.demo.poc.entrypoint.login.spider.LoginSpider;
import com.demo.poc.entrypoint.k8splugin.spider.InstallK8sPluginSpider;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.chrome.ChromeDriver;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class InstallK8SPluginService implements OperationService {

  private static final OperationType OPERATION = OperationType.INSTALL_K8S_PLUGIN;

  private final DriverHelper driverHelper;
  private final LoginSpider loginSpider;
  private final InstallK8sPluginSpider installK8sPluginSpider;

  @Override
  public void execute() {
    Logger.operationLog.accept(OPERATION);
    ChromeDriver driver = driverHelper.openBrowser();
    loginSpider.doLoginIfScreenIsPresent(driver);
    installK8sPluginSpider.searchAndInstallK8sPlugin(driver);
    driver.quit();
  }

  @Override
  public boolean supports(OperationType operation) {
    return OPERATION.equals(operation);
  }
}
