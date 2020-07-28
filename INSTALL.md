# Installation

## Prerequisites

The system is built using JDK13 and Maven (Java parts), as well as NodeJS > 10, with Yarn. 
All other dependencies should be installed from the build scripts.

The intention is for the final products to be deployed as Docker images and run behind a load balancer. For this, you need a current version of Docker installed on your system. 

All else fails, I suppose you could also run each app individually, i.e. without containers, but I would not recommend that, 
unless during development or for testing. You would likely have to adjust quite a few paths manually. 

## The quick and easy way

`./run.sh && ./build.sh`

Should run all Maven and Yarn builds, create all the necessary Docker images and deploy everything via docker-compose. 

## The long way (manual process)

1\. Run the Java build
```shell script
mvn clean install
```   
2\. Copy the compiled jar into the `docker folder`
```shell script
cp example-shop/target/example-shop-*.jar docker/
```
3\. Change into the `shop-gui` folder
```shell script
cd shop-gui
```
4\. Run the JavaScript build
```shell script
yarn && yarn build
```
5\. Copy the resulting `build` folder as `shop` into the `docker` folder:
```shell script
cp -R build/ ../docker/shop
```
6\. Change into the `admin-gui` folder
```shell script
cd ../admin-gui
```
7\. Run the JavaScript build
```shell script
yarn && yarn build
```
8\. Copy the resulting `build` folder as `admin` into the `docker` folder:
```shell script
cp -R build/ ../docker/admin
```
9\. Change back into the main folder
```shell script
cd ..
```
10\. Build the docker images
```shell script
docker-compose --file docker/docker-compose.yml build
``` 
11\. Run the docker containers
```shell script
docker-compose --file docker/docker-compose.yml up
```
