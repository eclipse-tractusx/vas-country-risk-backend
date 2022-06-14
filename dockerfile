#FROM registry.app.corpintra.net/microgateway-plant/openjdk
FROM openjdk:8-jdk-alpine


## Modify the ports used by NGINX by default
ARG JAR_FILE=target/*.jar

# Copy JAR file
COPY ${JAR_FILE} app.jar


ENTRYPOINT ["java","-jar","/app.jar"]