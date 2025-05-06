#!/bin/bash

source ./commons.sh
source ./variables.env

execute_jenkins_settings_scrapping() {
  local original_dir
  original_dir="$(pwd)"
  cd $JENKINS_SCRAPING_COMPONENT

  export JAVA_HOME=$JAVA_HOME
  command="$JAVA_HOME/bin/java -jar $JENKINS_SCRAPING_JAR"
  echo "$(get_timestamp) .......... $command" >> "./../../$LOG_FILE"
  eval "$command"

  cd "$original_dir"
}

install_jenkins_settings_scrapping() {
  local original_dir
  original_dir="$(pwd)"

  command="mvn clean install -Dmaven.home=\"$MAVEN_HOME\" -Dmaven.repo.local=\"$MAVEN_REPOSITORY\""
  echo "$(get_timestamp) .......... $command" >> "./../../$LOG_FILE"
  cd "$JENKINS_SCRAPING_COMPONENT"
  eval $command

  cd "$original_dir"
}

install_jenkins_settings_scrapping
execute_jenkins_settings_scrapping