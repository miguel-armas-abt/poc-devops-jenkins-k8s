package com.demo.poc.commons.service;

import com.demo.poc.commons.enums.OperationType;
import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Slf4j
public class OperationSelectorService {

  private final Set<OperationService> operationServices;

  @Inject
  public OperationSelectorService(Set<OperationService> operationServices) {
    this.operationServices = operationServices;
  }

  public OperationService selectService(String operationType) {
    return operationServices.stream()
        .filter(service -> service.supports(OperationType.parse(operationType)))
        .findFirst()
        .orElseThrow(() -> new RuntimeException("no such operation service for: " + operationType));
  }
}