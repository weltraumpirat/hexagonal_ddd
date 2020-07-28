#! /bin/sh
mvn clean install
cd shop-gui
yarn build
cd ../admin-gui
yarn build
