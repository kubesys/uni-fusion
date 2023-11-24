img="frontend"
version="0.0.4"
repo="g-ubjg5602-docker.pkg.coding.net/iscas-system/containers"

docker buildx create --name mybuilder --driver docker-container
docker buildx use mybuilder
docker run --privileged --rm tonistiigi/binfmt --install all

docker buildx build . --platform linux/arm64,linux/amd64 -t $repo/$img:v$version --push -f Dockerfile
