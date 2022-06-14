# Dependencies
FROM maven:3-openjdk-11 AS maven
ARG BUILD_TARGET=value-added-service

WORKDIR /build

COPY .mvn .mvn
COPY pom.xml .

# the --mount option requires BuildKit.
RUN --mount=type=cache,target=/root/.m2 mvn -B clean package -pl :$BUILD_TARGET -am -DskipTests


# Copy the jar and build image
FROM eclipse-temurin:11-jre AS irs-api

ARG UID=1000
ARG GID=1000

WORKDIR /app

COPY --chown=${UID}:${GID} --from=maven /build/irs-api/target/irs-api-*-exec.jar app.jar

USER ${UID}:${GID}

ENTRYPOINT ["java", "-Djava.util.logging.config.file=./logging.properties", "-jar", "app.jar"]