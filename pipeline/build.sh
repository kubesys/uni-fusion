#! /bin/bash
###############################################
##
##  Copyright (2023, ) Institute of Software
##      Chinese Academy of Sciences
##          wuheng@iscas.ac.cn
##
###############################################

mirror="pipeline"
version="1.0"

\cp /root/.kube/config docker/config
\cp /usr/bin/kubectl docker/kubectl

repo="g-ubjg5602-docker.pkg.coding.net/iscas-system/containers"

docker buildx create --name mybuilder --driver docker-container
docker buildx use mybuilder
docker run --privileged --rm tonistiigi/binfmt --install all

docker buildx build docker/ --platform linux/arm64,linux/amd64 -t $repo/$mirror:v$version --push -f docker/Dockerfile
