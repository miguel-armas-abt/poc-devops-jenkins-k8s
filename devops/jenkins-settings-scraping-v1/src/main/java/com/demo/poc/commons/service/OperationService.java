package com.demo.poc.commons.service;

import com.demo.poc.commons.enums.OperationType;

public interface OperationService {

  void execute();

  boolean supports(OperationType operation);
}
