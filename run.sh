#! /bin/sh

docker rm -f example-shop
cp example-shop/target/example-shop-*.jar docker/
docker build -t example-shop docker
docker run --name example-shop -p 80:8080 example-shop
