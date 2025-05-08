#!/bin/bash
set -euo pipefail

source ./commons.sh
source ./variables.env

SCRIPT_NAME="${BASH_SOURCE[0]}"
print_timestamp "$SCRIPT_NAME started"

DESTINATIONS=(
  "$JENKINS_SETTINGS_SCRAPING_PATH/src/main/resources/drivers"
  "$JENKINS_PIPELINE_CREATION_SCRAPING_PATH/src/main/resources/drivers"
)

PLATFORM="win64"
CHANNEL="Stable"
ZIP_FILE="chromedriver.zip"

exists_previous_driver() {
  local dest="$1"
  if [[ -f "$dest/chromedriver.exe" ]]; then
    echo "true"
  else
    echo "false"
  fi
}

is_superior_version() {
  local new="${1##* }" old="${2##* }"
  [[ "$new" == "$old" ]] && { echo "false"; return; }

  if [[ "$(printf '%s\n%s\n' "$old" "$new" | sort -V | head -1)" == "$old" ]]; then
    echo "true"
  else
    echo "false"
  fi
}

create_destination_folders() {
  for dest in "${DESTINATIONS[@]}"; do
    mkdir -p "$dest"
  done
}

get_latest_chromedriver_url() {
  local json
  if ! json=$(curl -sf "$CHROMEDRIVER_VERSIONS_URL"); then
    echo -e "${RED}Unable to fetch version metadata${NC}"
    exit 1
  fi

  local url
  url=$(echo "$json" | jq -r \
    ".channels.\"$CHANNEL\".downloads.chromedriver[] | select(.platform == \"$PLATFORM\") | .url")

  if [[ -z "$url" ]]; then
    echo -e "${RED}Couldn't get chromedriver URL for $PLATFORM${NC}"
    exit 1
  fi

  echo "$url"
}

download_and_unzip_chromedriver() {
  local url=$1
  if ! curl -sfL -o "$ZIP_FILE" "$url"; then
    echo -e "${RED}Failed to download chromedriver.zip${NC}"
    exit 1
  fi

  if ! unzip -o "$ZIP_FILE" >/dev/null 2>&1; then
    echo -e "${RED}Failed to unzip chromedriver.zip${NC}"
    exit 1
  fi
}

get_chromedriver_path() {
  local path
  path=$(find . -type f -name "chromedriver.exe" | head -n 1)
  if [[ ! -f "$path" ]]; then
    echo -e "${RED}chromedriver.exe not found${NC}"
    exit 1
  fi
  echo "$path"
}

copy_to_destinations() {
  local src_path="$1"
  local new_version
  new_version=$("$src_path" --version 2>/dev/null || echo "")

  for dest in "${DESTINATIONS[@]}"; do
    if [[ "$(exists_previous_driver "$dest")" == "true" ]]; then
      local old_version
      old_version=$("$dest/chromedriver.exe" --version 2>/dev/null || echo "")

      if [[ "$(is_superior_version "$new_version" "$old_version")" == "true" ]]; then
        cp "$src_path" "$dest/"
        echo -e "${GREEN}Updated chromedriver.exe${NC}"
      else
        echo -e "${YELLOW}Skip updating chromedriver.exe${NC}"
      fi

    else
      cp "$src_path" "$dest/"
      echo -e "${GREEN}Installed chromedriver.exe${NC}"
    fi
  done
}

cleanup_temp_files() {
  local driver_path="$1"
  rm -f "$ZIP_FILE"
  rm -f "$driver_path"
  local dir
  dir=$(dirname "$driver_path")
  [[ "$dir" != "." ]] && rm -rf "$dir"
}

update_chromedriver() {
  create_destination_folders
  local url
  url=$(get_latest_chromedriver_url)
  download_and_unzip_chromedriver "$url"
  local driver_path
  driver_path=$(get_chromedriver_path)
  copy_to_destinations "$driver_path"
  cleanup_temp_files "$driver_path"
}

update_chromedriver
