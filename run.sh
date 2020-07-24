#! /bin/sh

docker rmi -f example-shop
cp example-shop/target/example-shop-*.jar docker/
docker-compose --file docker/docker-compose.yml up --force-recreate
