#!/usr/bin/env sh
set -e
cd "$(dirname "$0")"
./gradlew clean build
echo "Build completed. Check build/libs/"
