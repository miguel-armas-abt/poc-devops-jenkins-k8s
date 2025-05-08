package com.demo.poc.entrypoint.login.service;

import com.demo.poc.commons.enums.OperationType;
import com.demo.poc.commons.helper.DriverHelper;
import com.demo.poc.commons.service.OperationService;
import com.demo.poc.commons.logging.Logger;
import com.demo.poc.entrypoint.login.spider.LoginSpider;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.chrome.ChromeDriver;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class LoginService implements OperationService {

  private static final OperationType OPERATION = OperationType.LOGIN;

  private final DriverHelper driverHelper;
  private final LoginSpider loginSpider;

  @Override
  public void execute() {
    Logger.operationLog.accept(OPERATION);
    ChromeDriver driver = driverHelper.openBrowser();
    loginSpider.doLoginIfScreenIsPresent(driver);
  }

  @Override
  public boolean supports(OperationType operation) {
    return OPERATION.equals(operation);
  }
}
