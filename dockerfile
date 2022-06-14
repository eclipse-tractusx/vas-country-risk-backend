# Dependencies
FROM maven:3-openjdk-11 AS maven
RUN mvn spring-boot:build-image


