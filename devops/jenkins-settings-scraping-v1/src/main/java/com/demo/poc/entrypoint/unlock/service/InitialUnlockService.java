package com.demo.poc.entrypoint.unlock.service;

import com.demo.poc.commons.enums.OperationType;
import com.demo.poc.commons.helper.DriverHelper;
import com.demo.poc.commons.service.OperationService;
import com.demo.poc.commons.logging.Logger;
import com.demo.poc.entrypoint.unlock.spider.SuggestedPluginSpider;
import com.demo.poc.entrypoint.unlock.spider.UnlockSpider;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.chrome.ChromeDriver;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class InitialUnlockService implements OperationService {

  private static final OperationType OPERATION = OperationType.INITIAL_UNLOCK;

  private final UnlockSpider unlockSpider;
  private final SuggestedPluginSpider suggestedPluginSpider;
  private final DriverHelper providerService;

  @Override
  public void execute() {
    Logger.operationLog.accept(OPERATION);
    ChromeDriver driver = providerService.openBrowser();
    unlockSpider.unlock(driver);
    suggestedPluginSpider.customizeJenkins(driver);
    driver.quit();
  }

  @Override
  public boolean supports(OperationType operation) {
    return OPERATION.equals(operation);
  }
}
