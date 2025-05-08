package com.demo.poc.commons.logging;

import com.demo.poc.commons.constants.Color;
import com.demo.poc.commons.enums.OperationType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Logger {

  public static Consumer<OperationType> operationLog = operation
      -> log.info(Color.YELLOW + "{}" + Color.RESET, operation.getDescription());

  public static Consumer<String> startLog = className
      -> log.info(Color.PURPLE + "{} started" + Color.RESET, className);

  public static Consumer<String> endLog = className
      -> log.info(Color.PURPLE + "{} finished" + Color.RESET, className);
}
