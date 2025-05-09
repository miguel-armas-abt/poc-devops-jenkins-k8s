package com.demo.poc.entrypoint.pipeline.service;

import com.demo.poc.commons.enums.OperationType;
import com.demo.poc.commons.helper.DriverHelper;
import com.demo.poc.commons.service.OperationService;
import com.demo.poc.entrypoint.login.spider.LoginSpider;
import com.demo.poc.entrypoint.pipeline.spider.PipelineCreationSpider;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.chrome.ChromeDriver;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class PipelineCreationService implements OperationService {

  private static final OperationType OPERATION = OperationType.PIPELINE_CREATION;

  private final DriverHelper driverHelper;
  private final LoginSpider loginSpider;
  private final PipelineCreationSpider pipelineCreationSpider;

  @Override
  public void execute() {
    ChromeDriver driver = driverHelper.openBrowser();
    loginSpider.doLoginIfScreenIsPresent(driver);
    pipelineCreationSpider.configureNewPipeline(driver);
  }

  @Override
  public boolean supports(OperationType operation) {
    return OPERATION.equals(operation);
  }
}
