#!/bin/bash
set -e

source ./commons.sh
source ./variables.env

SCRIPT_NAME="${BASH_SOURCE[0]}"
print_log "$SCRIPT_NAME started"

execute_scrapping() {
  local operation=$1

  local original_dir
  original_dir="$(pwd)"
  cd "$JENKINS_SETTINGS_SCRAPING_PATH"

  export JAVA_HOME=$JAVA_HOME
  command="$JAVA_HOME/bin/java -Doperation=$operation -jar $JENKINS_SETTINGS_SCRAPING_JAR"
  print_log "$command"
  eval "$command"

  cd "$original_dir"
}

operation=$1
execute_scrapping "$operation"