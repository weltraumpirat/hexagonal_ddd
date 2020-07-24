FROM openjdk:13-jdk-alpine
ARG JAR_FILE=example-shop-*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
