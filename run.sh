#! /bin/sh

docker rmi -f example-shop shop-gui admin-gui shop-lb
cp example-shop/target/example-shop-*.jar docker/
cp -R shop-gui/build/ docker/shop
cp -R admin-gui/build/ docker/admin
docker-compose --file docker/docker-compose.yml up --force-recreate
