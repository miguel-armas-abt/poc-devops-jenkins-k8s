package com.demo.poc.commons.custom.utils;

import static org.mockserver.model.Header.header;

import com.demo.poc.commons.core.constants.Symbol;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.mockserver.model.Header;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HeadersGenerator {

  public static Header generateTraceId() {
    return header("traceId", UUID.randomUUID().toString().replaceAll(Symbol.MIDDLE_DASH, StringUtils.EMPTY));
  }

  public static Header contentType(String mime) {
    return header("Content-Type", mime);
  }
}
