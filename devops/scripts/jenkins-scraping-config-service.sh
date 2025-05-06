#!/bin/bash

source ./commons.sh
source ./variables.env

execute_jenkins_settings_scrapping() {
  local original_dir
  original_dir="$(pwd)"
  cd $JENKINS_SETTINGS_SCRAPING_PATH

  export JAVA_HOME=$JAVA_HOME
  command="$JAVA_HOME/bin/java -jar $JENKINS_SETTINGS_SCRAPING_JAR"
  echo "$(get_timestamp) .......... $command" >> "./../../$LOG_FILE"
  eval "$command"

  cd "$original_dir"
}

install_jenkins_settings_scrapping() {
  local original_dir
  original_dir="$(pwd)"

  command="mvn clean install -Dmaven.home=\"$MAVEN_HOME\" -Dmaven.repo.local=\"$MAVEN_REPOSITORY\""
  echo "$(get_timestamp) .......... $command" >> "./../../$LOG_FILE"
  cd "$JENKINS_SETTINGS_SCRAPING_PATH"
  eval $command

  cd "$original_dir"
}

./chromedriver-downloader.sh
install_jenkins_settings_scrapping
execute_jenkins_settings_scrapping