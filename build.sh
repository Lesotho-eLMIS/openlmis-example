#!/bin/sh

# Sync with Transifex
/transifex/sync_transifex.sh \
  --resource openlmis-example.messages \
  --pattern 'src/main/resources/messages_<lang>.properties' \
  --source-file src/main/resources/messages_en.properties

# Run Gradle build
rm -rf ~/.gradle/caches/*/b9a74716da7699b23e293944812f2443/
rm -rf ~/.gradle/caches/
gradle clean build 
