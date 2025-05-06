#!/bin/bash

source ./commons.sh
source ./variables.env

./docker-commands.sh build-image
./docker-commands.sh up-compose
./docker-commands.sh wait-container
./scraping-settings-filler.sh