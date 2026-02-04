#!/bin/bash
#set -eu

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
SERVER_DIR="$(cd "$SCRIPT_DIR/.." && pwd)"
cd "$SERVER_DIR"

: "${ENV_FILE:?ENV_FILE is required. e.g. ENV_FILE=./env/.env ./deploy/deploy.sh}"

DOCKER_APP_NAME="subway-repo"

echo "PWD=$(pwd)"
echo "Using env file: $ENV_FILE"

# check and run db
if [ -z "$(docker compose -p subway-db ps -q)" ]; then
  echo "db 배포 시작 : $(date '+%Y-%m-%d %H:%M:%S')"
  docker compose -p subway-db -f docker/docker-compose.db.yml --env-file "$ENV_FILE" up -d
fi

# check and run grafana
if [ -z "$(docker compose -p grafana-container ps -q)" ]; then
  echo "grafana 실행 시작 : $(date '+%Y-%m-%d %H:%M:%S')"
  docker compose -p grafana-container -f docker/docker-compose.grafana.yml up -d
fi


# --- 배포 실행 ---
# CHECK BLUE
if [ -z "$(docker compose -p "$DOCKER_APP_NAME"-blue ps -q)" ]; then
  echo "blue 배포 시작 : $(date '+%Y-%m-%d %H:%M:%S')"

  docker compose -p "${DOCKER_APP_NAME}-blue" \
    -f docker/docker-compose.blue.yml \
    --env-file "$ENV_FILE" \
    up -d --build
  sleep 20
  if [ -n "$(docker compose -p "$DOCKER_APP_NAME"-green ps -q)" ]; then
    echo "blue 컨테이너 실행 확인. green 중단 시작"
    docker compose -p "${DOCKER_APP_NAME}-green" down
    echo "green 컨테이너 종료 완료"
  fi
  echo "blue 배포 완료 : $(date '+%Y-%m-%d %H:%M:%S')"
else
  echo "green 배포 시작 : $(date '+%Y-%m-%d %H:%M:%S')"

  docker compose -p "${DOCKER_APP_NAME}-green" \
    -f docker/docker-compose.green.yml \
    --env-file "$ENV_FILE" \
    up -d --build
  sleep 20
  if [ -n "$(docker compose -p "$DOCKER_APP_NAME"-blue ps -q)" ]; then
    echo "green 컨테이너 실행 확인. blue 중단 시작"
    docker compose -p "${DOCKER_APP_NAME}-blue" down
    echo "blue 컨테이너 종료 완료"
  fi
  echo "green 배포 완료 : $(date '+%Y-%m-%d %H:%M:%S')"
fi

