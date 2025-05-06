#!/bin/bash

source ./commons.sh
source ./variables.env

DESTINATIONS=(
  "$JENKINS_SETTINGS_SCRAPING_PATH/src/main/resources/drivers"
  "$JENKINS_PIPELINE_CREATION_SCRAPING_PATH/src/main/resources/drivers"
)

PLATFORM="win64"
CHANNEL="Stable"
ZIP_FILE="chromedriver.zip"

create_destination_folders() {
  for dest in "${DESTINATIONS[@]}"; do
    mkdir -p "$dest"
  done
}

get_latest_chromedriver_url() {
  local json=$(curl -sf "$CHROMEDRIVER_VERSIONS_URL") || {
    echo -e "${RED}Unable to fetch version metadata${NC}"
    exit 1
  }

  local url=$(echo "$json" | jq -r \
    ".channels.\"$CHANNEL\".downloads.chromedriver[] | select(.platform == \"$PLATFORM\") | .url")

  if [ -z "$url" ]; then
    echo -e "${RED}Couldn't get chromedriver URL for $PLATFORM ${NC}"
    exit 1
  fi

  echo "$url"
}

download_and_unzip_chromedriver() {
  local url=$1
  if ! curl -L -o "$ZIP_FILE" "$url"; then
    echo -e "${RED}Failed to download chromedriver.zip${NC}"
    exit 1
  fi

  if ! unzip -o "$ZIP_FILE" >/dev/null 2>&1; then
    echo -e "${RED}Failed to unzip chromedriver.zip${NC}"
    exit 1
  fi
}

get_chromedriver_path() {
  local path=$(find . -type f -name "chromedriver.exe" | head -n 1)
  if [ ! -f "$path" ]; then
    echo -e "${RED}chromedriver.exe not found${NC}"
    exit 1
  fi
  echo "$path"
}

copy_to_destinations() {
  local path=$1
  for dest in "${DESTINATIONS[@]}"; do
    cp "$path" "$dest/"
    echo -e "${GREEN}chromedriver.exe updated in: $dest ${NC}"
  done
}

cleanup_temp_files() {
  local driver_path=$1
  rm -f "$ZIP_FILE"
  rm -f "$driver_path"
  local dir=$(dirname "$driver_path")
  [ "$dir" != "." ] && rm -rf "$dir"
}

update_chromedriver() {
  create_destination_folders
  local url=$(get_latest_chromedriver_url)
  download_and_unzip_chromedriver "$url"
  local driver_path=$(get_chromedriver_path)
  copy_to_destinations "$driver_path"
  cleanup_temp_files "$driver_path"
}

update_chromedriver
