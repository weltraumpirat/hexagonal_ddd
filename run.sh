#! /bin/sh

cp example-shop-order/target/example-shop-order-*.jar docker/
cp -R shop-gui/build/ docker/shop
cp -R admin-gui/build/ docker/admin
docker-compose --file docker/docker-compose.yml build
docker-compose --file docker/docker-compose.yml up
