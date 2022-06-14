FROM maven:3-openjdk-11 AS maven
COPY ./pom.xml /pom.xml
COPY ./src ./src
RUN mvn clean package


# our final base image
FROM openjdk:11-slim as RUN
ARG UID=7000
ARG GID=7000
# set deployment directory
WORKDIR /dft
# copy over the built artifact from the maven image
COPY --chown=${UID}:${GID} --from=build target/*.jar ./app.jar
RUN chown ${UID}:${GID} /dft
USER ${UID}:${GID}
# set the startup command to run your binary
CMD ["java", "-jar", "./app.jar"]


