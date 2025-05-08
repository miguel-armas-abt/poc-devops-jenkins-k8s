#!/bin/bash
set -e

source ./commons.sh
source ./variables.env

SCRIPT_NAME="${BASH_SOURCE[0]}"
print_timestamp "$SCRIPT_NAME started"

execute_jenkins_settings_scrapping() {
  local operation=$1

  local original_dir
  original_dir="$(pwd)"
  cd "$JENKINS_SETTINGS_SCRAPING_PATH"

  export JAVA_HOME=$JAVA_HOME
  command="$JAVA_HOME/bin/java -Doperation=$operation -jar $JENKINS_SETTINGS_SCRAPING_JAR"
  print_timestamp "$command"
  eval "$command"

  cd "$original_dir"
}

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

execute_jenkins_settings_scrapping "INITIAL_UNLOCK"
execute_jenkins_settings_scrapping "INSTALL_K8S_PLUGIN"
execute_jenkins_settings_scrapping "ADD_K8S_CREDENTIALS"
execute_jenkins_settings_scrapping "CONFIG_K8S_CLOUD"