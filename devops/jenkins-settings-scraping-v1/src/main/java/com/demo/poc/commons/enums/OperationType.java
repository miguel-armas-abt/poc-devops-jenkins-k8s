package com.demo.poc.commons.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum OperationType {
  INITIAL_UNLOCK("Desbloqueo inicial de Jenkins e instalación de plugins sugeridos"),
  INSTALL_K8S_PLUGIN("Instalación del plugin K8S"),
  LOGIN("Inicio de sesión"),
  ADD_K8S_CREDENTIALS("Agregar nueva credencial de K8S"),
  CONFIG_K8S_CLOUD("Configurar K8S cloud");

  private final String description;

  public static OperationType parse(String operation) {
    return Arrays.stream(OperationType.values())
        .filter(type -> type.name().equalsIgnoreCase(operation))
        .findFirst()
        .orElseThrow(() -> new RuntimeException("no such operation type: " + operation));
  }
}
