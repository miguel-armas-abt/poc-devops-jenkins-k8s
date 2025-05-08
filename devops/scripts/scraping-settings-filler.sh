#!/bin/bash
set -euo pipefail

source ./commons.sh
source ./variables.env

SCRIPT_NAME="${BASH_SOURCE[0]}"
print_timestamp "$SCRIPT_NAME started"

forward_to_public_cluster_server() {
  local local_cluster_server=$1

  echo -e "${PURPLE}You need to have a stable version of ngrok!${NC}"
  command="$NGROK_EXE http $local_cluster_server"
  print_to_file "$command"
  eval start "$command"
  sleep 2

  public_cluster_server=$(curl -s "$NGROK_API_TUNNELS" | yq '.tunnels[0].public_url')

  if [ -n "$public_cluster_server" ]; then
    echo -e "${YELLOW}public cluster server:${NC} $public_cluster_server${NC}"
    sed -i "s|\(forwardedServerUrl:\s*\).*|\1$public_cluster_server|" "$JENKINS_SETTINGS_SCRAPING_FILE"
  else
    echo -e "${RED}public cluster server not found${NC}"
  fi
}

set_certificate_and_cluster_server_in_scraping_settings_file() {
  certificate_authority=$(kubectl config view --raw | yq '.clusters[0].cluster."certificate-authority"')
  local_cluster_server=$(kubectl config view --raw | yq '.clusters[0].cluster.server')

  escaped_certificate_authority=$(printf '%s' "$certificate_authority" | sed 's|\\|\\\\|g')

  if [ -n "$certificate_authority" ]; then
    echo -e "${YELLOW}certificate-authority:${NC} $certificate_authority${NC}"
    sed -i "s|\(certificate:\s*\).*|\1$escaped_certificate_authority|" "$JENKINS_SETTINGS_SCRAPING_FILE"
  else
    echo -e "${RED}certificate authority not found${NC}"
  fi

  echo

  if [ -n "$local_cluster_server" ]; then
    echo -e "${YELLOW}cluster server:${NC} $local_cluster_server${NC}"
    sed -i "s|\(localServerUrl:\s*\).*|\1$local_cluster_server|" "$JENKINS_SETTINGS_SCRAPING_FILE"
  else
    echo -e "${RED}cluster server not found${NC}"
  fi

  forward_to_public_cluster_server "$local_cluster_server"
}

set_k8s_authentication_token_in_scraping_settings_file() {
  kubectl apply -f "$K8S_JENKINS_AUTH_FILE"

  k8s_authentication_token=$(kubectl describe secret "$K8S_JENKINS_SERVICE_ACCOUNT_SECRET" | grep 'token:' | awk '{print $2}')

  if [ -n "$k8s_authentication_token" ]; then
    echo -e "${YELLOW}jenkins k8s authentication token:${NC} $k8s_authentication_token${NC}"
    sed -i "s|\(k8sAuthToken:\s*\).*|\1$k8s_authentication_token|" "$JENKINS_SETTINGS_SCRAPING_FILE"
  else
    echo -e "${RED}jenkins k8s authentication token not found${NC}"
  fi
}

set_unlock_jenkins_password_in_scraping_settings_file() {
  validate_if_container_running "$JENKINS_CONTAINER_NAME"

  password=$(docker logs "$JENKINS_CONTAINER_NAME" 2>&1 | grep -o "^[0-9a-f]\{32\}$")
  if [ -n "$password" ]; then
    echo -e "${YELLOW}unlock jenkins password:${NC} $password${NC}"
    sed -i "s|\(unlockPassword:\s*\).*|\1$password|" "$JENKINS_SETTINGS_SCRAPING_FILE"
  else
    echo -e "${RED}unlock jenkins password not found${NC}"
  fi
}

set_unlock_jenkins_password_in_scraping_settings_file
set_k8s_authentication_token_in_scraping_settings_file
set_certificate_and_cluster_server_in_scraping_settings_file