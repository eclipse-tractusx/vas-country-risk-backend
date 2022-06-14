# Dependencies
FROM maven:3-openjdk-11 AS maven
ARG BUILD_TARGET=irs-api

WORKDIR /build

COPY .mvn .mvn
COPY pom.xml .

# the --mount option requires BuildKit.
RUN --mount=type=cache,target=/root/.m2 mvn -B clean package -pl :$BUILD_TARGET -am -DskipTests


