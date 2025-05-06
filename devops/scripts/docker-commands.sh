#!/bin/bash

source ./commons.sh
source ./variables.env

wait_for_container() {
  local container_name=$1
  local max_retries=60
  local retries=0

  until [ "$(docker inspect -f '{{.State.Health.Status}}' "$container_name" 2>/dev/null)" == "healthy" ]; do
      if [ $retries -ge $max_retries ]; then
        echo -e "${RED}Timeout${NC}"
        return 1
      fi

      arrow_loader "Waiting for container $container_name"

      retries=$((retries + 1))
      sleep 0.5
  done

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

  echo "El valor de la operación debe coincidir con: ${valid_operations[*]}"
  return 1
}

process_operation() {
    local operation=$1

    connect_minikube="docker network connect minikube $JENKINS_IMAGE"
    disconnect_minikube="docker network disconnect minikube $JENKINS_IMAGE"

    image="$JENKINS_REPOSITORY/$JENKINS_IMAGE:$JENKINS_TAG"

    command="clear"

    if [[ $operation == "build-image" ]]; then
      command="docker build -f $JENKINS_DOCKERFILE -t $image --no-cache ."
    fi

    if [[ $operation == "up-compose" ]]; then
      #-d: in background
      command="docker-compose -f $JENKINS_DOCKER_COMPOSE_FILE up -d"
      eval "$connect_minikube"
    fi

    if [[ $operation == "delete-compose" ]]; then
      eval "$disconnect_minikube"
      command="docker-compose -f $JENKINS_DOCKER_COMPOSE_FILE down -v"
    fi

    if [[ $operation == "wait-container" ]]; then
      container=$(docker ps --filter ancestor="$image" --format "{{.Names}}" | head -n 1)
      echo -e "Wait for container ${YELLOW}$container${NC}"
      wait_for_container "$container"
    fi

    echo "$(get_timestamp) .......... $command" >> "./../../$LOG_FILE"
    eval "$command"
}

operation=$1
validate_operation "$operation" || exit 1
process_operation "$operation"