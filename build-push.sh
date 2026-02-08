#!/bin/bash
set -e
# if [ -f .env ]; then
#   export $(grep -v '^#' .env | xargs)
# else
#   echo ".env file not found!"
#   exit 1
# fi
# IMAGE_TAG=$(date +%Y%m%d-%H%M%S)
# if grep -q '^IMAGE_TAG=' .env; then
#   sed -i "s/^IMAGE_TAG=.*/IMAGE_TAG=${IMAGE_TAG}/" .env
# else
#   echo "" >> .env
#   echo "IMAGE_TAG=${IMAGE_TAG}" >> .env
# fi

# export IMAGE_TAG
IMAGE_NAME="institute-listing"
echo "Debug variables:"
echo "DOCKER_USERNAME is set: ${DOCKER_USERNAME:+yes}"
echo "DOCKER_PASSWORD is set: ${DOCKER_PASSWORD:+yes}"
REMOTE_IMAGE=$DOCKER_USERNAME/$IMAGE_NAME
echo "Using image tag: $IMAGE_TAG"
# 1️⃣ Docker login
echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin

# 2️⃣ Clean old images safely
REPOS="institute-listing abishek1022/institute-listing"

echo "Cleaning old images..."
for REPO in $REPOS; do
  IMAGE_IDS=$(docker images --format '{{.Repository}} {{.ID}}' | awk -v repo="$REPO" '$1 == repo {print $2}' | sort -u)
  docker ps -q --filter ancestor=$IMAGE_IDS | xargs -r docker stop
  docker ps -q --filter ancestor=$IMAGE_IDS | xargs -r docker rm
  if [ -n "$IMAGE_IDS" ]; then
    echo "Found images for $REPO: $IMAGE_IDS"
    for IMAGE_ID in $IMAGE_IDS; do
      echo "Removing image $IMAGE_ID ($REPO)..."
      docker rmi -f "$IMAGE_ID" || true
    done
  else
    echo "No images found for $REPO"
  fi
done


docker build -t $IMAGE_NAME:$IMAGE_TAG .

docker tag $IMAGE_NAME:$IMAGE_TAG $REMOTE_IMAGE:$IMAGE_TAG

docker push $REMOTE_IMAGE:$IMAGE_TAG
docker rmi $IMAGE_NAME:$IMAGE_TAG
echo " Image pushed:$REMOTE_IMAGE:$IMAGE_TAG"
