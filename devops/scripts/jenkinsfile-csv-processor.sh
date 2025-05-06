#!/bin/bash

source ./commons.sh
source ./variables.env

generate_jenkinsfile() {
  local component_name=$1
  component_path="$BACKEND_HOME/$component_name"

  mkdir -p "$component_path"

  k8s_credential_id=$(yq '.configuration.k8s.credentials.id' "$JENKINS_SCRAPING_FILE")
  k8s_cloud_server_url=$(yq '.configuration.k8s.cloud.forwardedServerUrl' "$JENKINS_SCRAPING_FILE")

  template=""
  if [[ -f "$component_path/pom.xml" ]]; then
    template="$JENKINSFILE_JAVA_TEMPLATE"
  else
    template="$JENKINSFILE_GENERIC_TEMPLATE"
  fi

  jenkinsfile=$(<"$template")
  jenkinsfile="${jenkinsfile//@component_type/$component_type}"
  jenkinsfile="${jenkinsfile//@component_name/$component_name}"
  jenkinsfile="${jenkinsfile//@k8s_credential_id/$k8s_credential_id}"
  jenkinsfile="${jenkinsfile//@k8s_cloud_server_url/$k8s_cloud_server_url}"

  echo "$jenkinsfile" > "$component_path/Jenkinsfile"
  echo -e "${CHECK_SYMBOL} $component_name"
}

iterate_csv_records() {

  firstline=true
  while IFS=',' read -r component_name || [ -n "$component_name" ]; do
    # Ignore headers
    if $firstline; then
        firstline=false
        continue
    fi

    # Ignore comments
    if [[ $component_name != "#"* ]]; then
      generate_jenkinsfile "$component_name"
    fi

  done < <(sed 's/\r//g' "$BACKEND_COMPONENTS_CSV")
}

iterate_csv_records