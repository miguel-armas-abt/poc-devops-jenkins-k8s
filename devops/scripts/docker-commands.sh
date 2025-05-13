#!/bin/bash
set -e

source ./commons.sh
source ./variables.env

SCRIPT_NAME="${BASH_SOURCE[0]}"
print_log "$SCRIPT_NAME started"

wait_for_container() {
  local container_name=$1
  local max_retries=$JENKINS_WAIT_RETRIES
  local retries=0

  until [[ "$(is_container_running "$container_name")" == "true" ]]; do
    if [ $retries -ge "$max_retries" ]; then
      printf "\n"
      echo -e "${RED}timeout waiting for $container_name${NC}"
      return 1
    fi

    arrow_loader "waiting for container $container_name"
    retries=$((retries + 1))
    sleep 0.5
  done

  sleep "$JENKINS_STARTUP_DELAY"
  printf "\n"
  echo -e "${GREEN}$container_name is running${NC}"
  return 0
}

validate_operation() {
  local operation=$1

  local valid_operations=("build-image" "up-compose" "delete-compose" "wait-container")

  for valid_operation in "${valid_operations[@]}"; do
    if [[ "$operation" == "$valid_operation" ]]; then
      return 0
    fi
  done

  echo "operation must be match: ${valid_operations[*]}"
  return 1
}

process_operation() {
    local operation=$1
    image="$JENKINS_REPOSITORY/$JENKINS_CONTAINER_NAME:$JENKINS_TAG"

    if [[ $operation == "build-image" ]]; then
      command="docker build -f $JENKINS_DOCKERFILE -t $image ."
      print_log_and_eval "$command"
    fi

    if [[ $operation == "up-compose" ]]; then
      #-d: in background
      command="docker-compose -f $JENKINS_DOCKER_COMPOSE_FILE up -d"
      print_log_and_eval "$command"
      command="docker network connect minikube $JENKINS_CONTAINER_NAME"
      print_log_and_eval "$command"
    fi

    if [[ $operation == "delete-compose" ]]; then
      command="docker network disconnect minikube $JENKINS_CONTAINER_NAME"
      print_log_and_eval "$command"
      command="docker-compose -f $JENKINS_DOCKER_COMPOSE_FILE down -v"
      print_log_and_eval "$command"
    fi

    if [[ $operation == "wait-container" ]]; then
      wait_for_container "$JENKINS_CONTAINER_NAME"
    fi
}

operation=$1
validate_operation "$operation" || exit 1
process_operation "$operation"