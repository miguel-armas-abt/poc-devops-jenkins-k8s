package com.demo.poc;

import com.demo.poc.commons.constants.Color;
import com.demo.poc.commons.service.OperationSelectorService;
import com.demo.poc.commons.service.OperationService;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.demo.poc.commons.config.ComponentsConfig;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public class Main {

  private static final String OPERATION_PROPERTY = "operation";

  public static void main(String[] args) {
    Injector injector = Guice.createInjector(new ComponentsConfig());

    OperationSelectorService selectorService = injector.getInstance(OperationSelectorService.class);
    String operation = System.getProperty(OPERATION_PROPERTY);

    if(Objects.isNull(operation) || operation.isBlank())
      throw new RuntimeException("operation cannot be null");

    log.info(Color.GREEN + "operation: {}" + Color.RESET, operation);
    OperationService operationService = selectorService.selectService(operation);
    operationService.execute();
  }
}