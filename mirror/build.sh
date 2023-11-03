#! /bin/bash
###############################################
##
##  Copyright (2023, ) Institute of Software
##      Chinese Academy of Sciences
##          wuheng@iscas.ac.cn
##
###############################################

MAVEN="maven:3.6.3-openjdk-17"

###############################################
##
##  Source to Jar
##
###############################################
docker run -it --net host --rm -v /root/.m2:/root/.m2 -v "$(pwd)":/usr/src/mymaven -w /usr/src/mymaven $MAVEN mvn clean install -Dmaven.test.skip


###############################################
##
##  Jar to local image
##
###############################################

mirror="mirror"
version=$(cat pom.xml | grep version | head -1 | awk -F">" '{print$2}' | awk -F"<" '{print$1}')
withjar="jar-with-dependencies"

\cp target/$mirror-$version-$withjar.jar docker/kube-$mirror.jar

repo="g-ubjg5602-docker.pkg.coding.net/iscas-system/containers"
#repo="registry.cn-beijing.aliyuncs.com/dosproj"

docker buildx create --name mybuilder --driver docker-container
docker buildx use mybuilder
docker run --privileged --rm tonistiigi/binfmt --install all

docker buildx build docker/ --platform linux/arm64,linux/amd64 -t $repo/$mirror:v$version --push -f docker/Dockerfile
