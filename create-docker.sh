#!/usr/bin/env bash

rm -rf ./build/
./gradlew installDist
docker build -t script-kotlin .