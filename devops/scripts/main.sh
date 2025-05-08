#!/bin/bash
set -e

source ./commons.sh

print_title() {
  echo -e "\n########## ${CYAN} Ejecute una acción ${NC}##########\n"
}

script_caller() {
  $1
  print_title
}

print_title

options=(
  "Iniciar Jenkins"
  "Configurar Jenkins mediante scraping"
  "Desinstalar Jenkins"
  "Generar Jenkinsfiles"
  "Salir"
)

while true; do
  select option in "${options[@]}"; do
      case $REPLY in
        1) script_caller "./jenkins-init-service.sh"; break ;;
        2) script_caller "./jenkins-scraping-config-service.sh"; break ;;
        3) script_caller "./docker-commands.sh delete-compose"; break ;;
        4) script_caller "./jenkinsfile-csv-processor.sh"; break ;;
        5) exit; ;;
        *) echo -e "${RED}Opción inválida${NC}" >&2
      esac
  done
done