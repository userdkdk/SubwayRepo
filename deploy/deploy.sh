#!/bin/bash
#set -eu

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
SERVER_DIR="$(cd "$SCRIPT_DIR/.." && pwd)"
cd "$SERVER_DIR"

: "${ENV_FILE:?ENV_FILE is required. e.g. ENV_FILE=./env/.env ./deploy/deploy.sh}"

DOCKER_APP_NAME="subway-repo"


echo "PWD=$(pwd)"
echo "Using env file: $ENV_FILE"

# check db
EXIST_DB=$(docker-compose -p subway-db -f docker/docker-compose.db.yml ps | grep -E "Up|running")
if [ -z "$EXIST_DB" ]; then
  echo "db 배포 시작 : $(date '+%Y-%m-%d %H:%M:%S')"
  docker-compose -p subway-db -f docker/docker-compose.db.yml --env-file "$ENV_FILE" up -d
fi


# --- 배포 실행 ---
# CHECK BLUE
EXIST_BLUE=$(docker-compose -p "${DOCKER_APP_NAME}-blue" -f docker/docker-compose.blue.yml ps | grep -E "Up|running")

if [ -z "$EXIST_BLUE" ]; then
  echo "blue 배포 시작 : $(date '+%Y-%m-%d %H:%M:%S')"

  docker-compose -p "${DOCKER_APP_NAME}-blue" \
    -f docker/docker-compose.blue.yml \
    --env-file "$ENV_FILE" \
    up -d --build
  sleep 20
  NEW_CONTAINER_STATUS=$(docker-compose -p "${DOCKER_APP_NAME}-blue" -f docker/docker-compose.blue.yml ps | grep -E "Up|running")
  if [ -n "$NEW_CONTAINER_STATUS" ]; then
    echo "blue 컨테이너 실행 확인. green 중단 시작"
    docker-compose -p "${DOCKER_APP_NAME}-green" \
        -f docker/docker-compose.green.yml \
        --env-file "$ENV_FILE" \
        down
  fi
  echo "blue 배포 완료 : $(date '+%Y-%m-%d %H:%M:%S')"
else
  echo "green 배포 시작 : $(date '+%Y-%m-%d %H:%M:%S')"

  docker-compose -p "${DOCKER_APP_NAME}-green" \
    -f docker/docker-compose.green.yml \
    --env-file "$ENV_FILE" \
    up -d --build
  sleep 20
  NEW_CONTAINER_STATUS=$(docker-compose -p "${DOCKER_APP_NAME}-green" -f docker/docker-compose.green.yml ps | grep -E "Up|running")
  if [ -n "$NEW_CONTAINER_STATUS" ]; then
    docker-compose -p "${DOCKER_APP_NAME}-blue" \
        -f docker/docker-compose.blue.yml \
        --env-file "$ENV_FILE" \
        down
  fi
  echo "green 배포 완료 : $(date '+%Y-%m-%d %H:%M:%S')"
fi

