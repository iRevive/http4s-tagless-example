#!/usr/bin/env bash

if [ -z "$1" ]; then
  echo "CI SBT docker image name is missing"
  exit 1
fi

sbt_image=$1

set -euf pipefail

echo "Pulling CI SBT image $sbt_image"
docker pull $sbt_image || true

echo "Executing tests"

docker network rm http-service-ci-network || true
docker network create -d bridge http-service-ci-network

docker run --rm \
    -v /var/run/docker.sock:/var/run/docker.sock \
    --mount src="$(pwd)",target=/opt/workspace,type=bind \
    --network=http-service-ci-network \
    -e DOCKER_NETWORK=http-service-ci-network \
    -e DOCKER_REGISTRY_IMAGE=$CI_REGISTRY_IMAGE \
    $sbt_image \
    sbt ci