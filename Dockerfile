FROM maven:3-openjdk-17 AS maven
COPY ./pom.xml /pom.xml
COPY ./src ./src
RUN mvn clean package
#CMD exec /bin/bash -c "trap : TERM INT; sleep infinity & wait"

# Copy the jar and build image
FROM eclipse-temurin:18-jre AS value-added-service

ARG UID=1000
ARG GID=1000

WORKDIR /app

COPY --chown=${UID}:${GID} --from=maven target/value-added-service-*-exec.jar app.jar

USER ${UID}:${GID}

ENTRYPOINT ["java", "-Djava.util.logging.config.file=./logging.properties", "-jar", "app.jar"]

USER ${UID}:${GID}

# set the startup command to run your binary
CMD ["java", "-jar", "./app.jar"]

CMD exec /bin/bash -c "trap : TERM INT; sleep infinity & wait"


