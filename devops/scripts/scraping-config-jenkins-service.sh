#!/bin/bash
set -e

source ./commons.sh
source ./variables.env

SCRIPT_NAME="${BASH_SOURCE[0]}"
print_timestamp "$SCRIPT_NAME started"

install_jenkins_settings_scrapping() {
  local original_dir
  original_dir="$(pwd)"

  command="mvn clean install -Dmaven.home=\"$MAVEN_HOME\" -Dmaven.repo.local=\"$MAVEN_REPOSITORY\""
  print_timestamp "$command"
  cd "$JENKINS_SETTINGS_SCRAPING_PATH"
  eval "$command"

  cd "$original_dir"
}

echo -e "${PURPLE}You need to have Chrome updated to its latest version${NC}"

./chromedriver-downloader.sh
install_jenkins_settings_scrapping

./scraping-execution-service.sh "INITIAL_UNLOCK"
./scraping-execution-service.sh "INSTALL_K8S_PLUGIN"
./scraping-execution-service.sh "ADD_K8S_CREDENTIALS"
./scraping-execution-service.sh "CONFIG_K8S_CLOUD"