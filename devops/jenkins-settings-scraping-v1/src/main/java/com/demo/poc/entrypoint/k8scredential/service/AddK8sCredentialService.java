package com.demo.poc.entrypoint.k8scredential.service;

import com.demo.poc.commons.enums.OperationType;
import com.demo.poc.commons.helper.DriverHelper;
import com.demo.poc.commons.service.OperationService;
import com.demo.poc.commons.logging.Logger;
import com.demo.poc.entrypoint.k8scredential.spider.AddK8sCredentialSpider;
import com.demo.poc.entrypoint.login.spider.LoginSpider;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.chrome.ChromeDriver;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class AddK8sCredentialService implements OperationService {

  private static final OperationType OPERATION = OperationType.ADD_K8S_CREDENTIALS;

  private final DriverHelper driverHelper;
  private final LoginSpider loginSpider;
  private final AddK8sCredentialSpider addK8SCredentialSpider;

  @Override
  public void execute() {
    Logger.operationLog.accept(OPERATION);
    ChromeDriver driver = driverHelper.openBrowser();
    loginSpider.doLoginIfScreenIsPresent(driver);
    addK8SCredentialSpider.addK8sCredentials(driver);
    driver.quit();
  }

  @Override
  public boolean supports(OperationType operation) {
    return OPERATION.equals(operation);
  }
}
