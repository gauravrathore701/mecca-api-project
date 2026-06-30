#!/usr/bin/env bash
set -euo pipefail
cd /home/gaurav/Projects/mecca-api-project
git pull
mvn -q clean package -DskipTests
sudo systemctl restart mecca-api-project
